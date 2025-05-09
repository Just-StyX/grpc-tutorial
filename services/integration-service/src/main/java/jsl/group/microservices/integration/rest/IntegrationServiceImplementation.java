package jsl.group.microservices.integration.rest;

import jsl.group.commons.InventoryServiceGrpc;
import jsl.group.commons.InventoryServiceOuterClass;
import jsl.group.commons.ProductServiceGrpc;
import jsl.group.commons.ProductServiceOuterClass;
import jsl.group.commons.models.InventoryResponse;
import jsl.group.commons.models.ProductDetailsRequest;
import jsl.group.commons.models.ProductRequest;
import jsl.group.commons.models.ProductResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class IntegrationServiceImplementation implements IntegrationService {

    private final ProductServiceGrpc.ProductServiceBlockingStub productChannel;
    private final InventoryServiceGrpc.InventoryServiceBlockingStub inventoryChannel;

    public IntegrationServiceImplementation(ProductServiceGrpc.ProductServiceBlockingStub productChannel, InventoryServiceGrpc.InventoryServiceBlockingStub inventoryChannel) {
        this.productChannel = productChannel;
        this.inventoryChannel = inventoryChannel;
    }

    @Override
    public String createProduct(ProductRequest productRequest) {
        ProductServiceOuterClass.Product product = toProduct(productRequest);
        InventoryServiceOuterClass.ProductId productId = productChannel.createProduct(product);
        return productId.getValue();
    }

    @Override
    public ProductResponse updateProduct(ProductRequest productRequest) {
        ProductServiceOuterClass.Product product = toProduct(productRequest);
        InventoryServiceOuterClass.ProductId productIdUpdated = productChannel.updateProduct(product);
        return toResponse(product);
    }

    @Override
    public ProductResponse getProduct(String productId) {
        ProductServiceOuterClass.Product product = productChannel
                .getProduct(InventoryServiceOuterClass.ProductId.newBuilder().setValue(productId).build());
        return toResponse(product);
    }

    @Override
    public String deleteProduct(String productId) {
        return productChannel
                .deleteProduct(InventoryServiceOuterClass.ProductId.newBuilder().setValue(productId).build()).getValue();
    }

    @Override
    public InventoryResponse addProductCount(ProductDetailsRequest productDetailsRequest) {
        InventoryServiceOuterClass.ProductDetails productDetails = toProductDetails(productDetailsRequest);
        int inventory = inventoryChannel.addProductCount(productDetails).getValue();
        return new InventoryResponse(productDetailsRequest.getProductId(), inventory);
    }

    @Override
    public InventoryResponse subtractProductCount(ProductDetailsRequest productDetailsRequest) {
        InventoryServiceOuterClass.ProductDetails productDetails = toProductDetails(productDetailsRequest);
        int inventory = inventoryChannel.subtractProductCount(productDetails).getValue();
        return new InventoryResponse(productDetailsRequest.getProductId(), inventory);
    }

    private ProductServiceOuterClass.Product toProduct(ProductRequest productRequest) {
        String requestId = (productRequest.getId() != null) ? productRequest.getId() : UUID.randomUUID().toString();
        return ProductServiceOuterClass.Product.newBuilder()
                .setId(requestId)
                .setProductName(productRequest.getProductName())
                .setProductPrice(productRequest.getProductPrice().floatValue())
                .setProductBarcode(productRequest.getProductBarcode())
                .setProductDescription(productRequest.getProductDescription())
                .build();
    }

    private ProductResponse toResponse(ProductServiceOuterClass.Product product) {
        return new ProductResponse(
                product.getId(), product.getProductName(), product.getProductBarcode(),
                BigDecimal.valueOf(product.getProductPrice()), product.getProductDescription()
        );
    }

    private InventoryServiceOuterClass.ProductDetails toProductDetails(ProductDetailsRequest productDetailsRequest) {
        return InventoryServiceOuterClass.ProductDetails.newBuilder()
                .setQuantity(productDetailsRequest.getQuantity())
                .setProductName(productDetailsRequest.getProductName())
                .setProductId(InventoryServiceOuterClass.ProductId.newBuilder().setValue(productDetailsRequest.getProductId()).build())
                .build();
    }
}
