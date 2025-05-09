package jsl.group.commons.models;

import java.math.BigDecimal;

public record ShippingItems(
        String productId, String itemName, BigDecimal itemPrice, int quantity, BigDecimal amount
) {
}
