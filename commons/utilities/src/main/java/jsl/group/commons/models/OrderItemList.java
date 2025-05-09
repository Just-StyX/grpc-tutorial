package jsl.group.commons.models;

import jsl.group.commons.event.EventType;

import java.util.List;

public record OrderItemList(
        List<OrderItem> orderItemList, EventType eventType
) {
}
