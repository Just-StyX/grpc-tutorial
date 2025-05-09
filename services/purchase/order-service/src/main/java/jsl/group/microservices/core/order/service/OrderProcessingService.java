package jsl.group.microservices.core.order.service;

import jsl.group.commons.event.EventType;
import jsl.group.commons.models.OrderItem;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OrderProcessingService {
    Mono<Void> purchaseItems(List<OrderItem> orderItemList, EventType eventType);
}
