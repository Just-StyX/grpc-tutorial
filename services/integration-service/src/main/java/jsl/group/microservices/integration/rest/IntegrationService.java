package jsl.group.microservices.integration.rest;

import jsl.group.commons.models.*;

public interface IntegrationService {
    String createProduct(ProductRequest productRequest);
    ProductResponse updateProduct(ProductRequest productRequest);
    ProductResponse getProduct(String productId);
    String deleteProduct(String productId);
    InventoryResponse addProductCount(ProductDetailsRequest productDetailsRequest);
    InventoryResponse subtractProductCount(ProductDetailsRequest productDetailsRequest);
}
