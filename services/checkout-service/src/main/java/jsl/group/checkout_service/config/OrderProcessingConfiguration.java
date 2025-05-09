package jsl.group.checkout_service.config;

import jsl.group.checkout_service.service.CheckoutService;
import jsl.group.commons.event.Event;
import jsl.group.commons.event.EventType;
import jsl.group.commons.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Configuration
public class OrderProcessingConfiguration {
    private static final Logger log = LoggerFactory.getLogger(OrderProcessingConfiguration.class);
    private final StreamBridge streamBridge;
    private final CheckoutService checkoutService;

    public OrderProcessingConfiguration(StreamBridge streamBridge, CheckoutService checkoutService) {
        this.streamBridge = streamBridge;
        this.checkoutService = checkoutService;
    }

    @Bean
    public Consumer<Event<Integer, List<OrderItem>>> orderProcessing() {
        return integerListEvent -> {
            switch (integerListEvent.getEventType()) {
                case PURCHASE -> {
                    List<OrderItem> orderItemList = integerListEvent.getData();
                    List<ItemDetails> itemDetailsList = new ArrayList<>(orderItemList.size());
                    for (OrderItem orderItem: orderItemList) {
                        itemDetailsList.add(new ItemDetails(orderItem.productRequest().getId(), orderItem.quantity()));
                    }
                    List<ItemDetailsResponse> itemDetailsResponseList = checkoutService.checkAvailability(itemDetailsList);
                    log.info("Availability response: {}", itemDetailsResponseList);
                    List<String> itemsInStockIds = itemDetailsResponseList.stream().filter(ItemDetailsResponse::inStock)
                            .map(ItemDetailsResponse::productId).toList();
                    List<ShippingItems> shippingItemsList = orderItemList.stream().map(item -> {
                        BigDecimal amount = item.productRequest().getProductPrice().multiply(BigDecimal.valueOf(item.quantity()));
                        return new ShippingItems(
                                item.productRequest().getId(), item.productRequest().getProductName(), item.productRequest().getProductPrice(), item.quantity(), amount
                        );
                    }).filter(available -> itemsInStockIds.contains(available.productId())).toList();

                    if (!shippingItemsList.isEmpty()) {
                        Event<Integer, List<ShippingItems>> event = new Event<>(EventType.SHIPPING, 1, shippingItemsList);
                        Message<? extends Event<?, ?>> message = MessageBuilder.withPayload(event)
                                .setHeader("partitionKey", event.getKey()).build();
                        streamBridge.send("shipping-out-0", message);
                        List<ProductDetailsRequest> productDetailsRequests = shippingItemsList.stream().map(
                                details -> new ProductDetailsRequest(details.productId(), details.itemName(), details.quantity())
                        ).toList();
                        List<InventoryResponse> inventoryResponseList = checkoutService.inventoryCleanUp(productDetailsRequests);
                        log.info("Remaining inventory of Items: {}", inventoryResponseList);
                    }
                }

                case CANCEL -> {
                    // TODO
                    log.info("Cancelling order {}", integerListEvent);
                }

                default -> {
                    String errorMessage = "Incorrect Event Type: " + integerListEvent.getEventType();
                    throw new RuntimeException("Event not processed: " + errorMessage);
                }
            }
            log.info("Order processing done!");
        };
    }
}
