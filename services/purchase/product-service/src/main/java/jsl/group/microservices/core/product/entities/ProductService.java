package jsl.group.microservices.core.product.entities;

public interface ProductService {
    ProductEntity createProduct(ProductEntity productEntity);
    ProductEntity updateProduct(String productId, ProductEntity productEntity);
    ProductEntity getProduct(String productId);
    String deleteProduct(String productId);
}
