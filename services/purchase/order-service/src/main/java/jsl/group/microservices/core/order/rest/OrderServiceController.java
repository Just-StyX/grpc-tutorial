package jsl.group.microservices.core.order.rest;

import jsl.group.commons.models.OrderItemList;
import jsl.group.microservices.core.order.service.OrderProcessingService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
public class OrderServiceController {
    private final OrderProcessingService orderProcessingService;

    public OrderServiceController(OrderProcessingService orderProcessingService) {
        this.orderProcessingService = orderProcessingService;
    }

    @PostMapping(value = "/purchase", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> purchaseItems(@RequestBody OrderItemList orderItemList) {
        return orderProcessingService.purchaseItems(orderItemList.orderItemList(), orderItemList.eventType()).then();
    }
}
