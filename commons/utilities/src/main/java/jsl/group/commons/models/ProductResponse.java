package jsl.group.commons.models;

import java.math.BigDecimal;

public record ProductResponse(String productId, String productName, String productBarcode, BigDecimal productPrice, String productDescription) {
}
