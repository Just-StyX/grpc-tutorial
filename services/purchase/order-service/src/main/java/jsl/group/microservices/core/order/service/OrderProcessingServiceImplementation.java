package jsl.group.microservices.core.order.service;

import jsl.group.commons.event.Event;
import jsl.group.commons.event.EventType;
import jsl.group.commons.models.OrderItem;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrderProcessingServiceImplementation implements OrderProcessingService {
    private final StreamBridge streamBridge;

    public OrderProcessingServiceImplementation(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    @Async
    public Mono<Void> purchaseItems(List<OrderItem> orderItemList, EventType eventType) {
        return Mono.fromRunnable(() -> {
            sendMessage(new Event<>(eventType, 0, orderItemList));
        }).then();
    }

    private void sendMessage(Event<?, ?> event) {
        Message<? extends Event<?, ?>> message = MessageBuilder.withPayload(event).setHeader("partitionKey", event.getKey()).build();
        streamBridge.send("orders-out-0", message);
    }
}
