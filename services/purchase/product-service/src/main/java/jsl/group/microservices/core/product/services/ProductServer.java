package jsl.group.microservices.core.product.services;

import com.google.protobuf.StringValue;
import io.grpc.stub.StreamObserver;
import jsl.group.commons.ProductServiceGrpc;
import jsl.group.commons.ProductServiceOuterClass;
import jsl.group.microservices.core.product.entities.ProductEntity;
import jsl.group.microservices.core.product.entities.ProductEntityMapper;
import jsl.group.microservices.core.product.entities.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductServer extends ProductServiceGrpc.ProductServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(ProductServer.class);
    private final ProductService productService;
    private final ProductEntityMapper productEntityMapper;

    public ProductServer(ProductService productService, ProductEntityMapper productEntityMapper) {
        this.productService = productService;
        this.productEntityMapper = productEntityMapper;
    }

    @Override
    public void createProduct(ProductServiceOuterClass.Product product, StreamObserver<ProductServiceOuterClass.ProductId> productIdStreamObserver) {
        ProductEntity productEntity = productService.createProduct(productEntityMapper.productToProductEntity(product));
        ProductServiceOuterClass.ProductId productId = ProductServiceOuterClass.ProductId.newBuilder().setValue(productEntity.getId()).build();
        productIdStreamObserver.onNext(productId);
        productIdStreamObserver.onCompleted();
        log.info("Product with ID {}, added successfully ...", productId.getValue());
    }

    @Override
    public void getProduct(ProductServiceOuterClass.ProductId productId, StreamObserver<ProductServiceOuterClass.Product> productStreamObserver) {
        String productIdString = productId.getValue();
        ProductEntity productEntity = productService.getProduct(productIdString);
        ProductServiceOuterClass.Product product = convertToProduct(productEntity);
        productStreamObserver.onNext(product);
        productStreamObserver.onCompleted();
        log.info("Product with ID {}, fetched successfully ...", productIdString);
    }

    @Override
    public void updateProduct(ProductServiceOuterClass.Product product, StreamObserver<ProductServiceOuterClass.ProductId> productIdStreamObserver) {
        String productId = product.getId();
        ProductEntity productEntity = productEntityMapper.productToProductEntity(product);
        ProductEntity updatedEntity = productService.updateProduct(productId, productEntity);
        productIdStreamObserver.onNext(ProductServiceOuterClass.ProductId.newBuilder().setValue(productId).build());
        productIdStreamObserver.onCompleted();
        log.info("Product with ID {}, updated successfully ...", updatedEntity.getId());
    }

    @Override
    public void deleteProduct(ProductServiceOuterClass.ProductId productId, StreamObserver<StringValue> stringStreamObserver) {
        String productStringId = productId.getValue();
        stringStreamObserver.onNext(StringValue.newBuilder().setValue(productService.deleteProduct(productStringId)).build());
        stringStreamObserver.onCompleted();
        log.info("Product with ID {}, deleted successfully ...", productStringId);
    }

    private ProductServiceOuterClass.Product convertToProduct(ProductEntity productEntity) {
        return ProductServiceOuterClass.Product.newBuilder()
                .setProductName(productEntity.getProductName())
                .setProductDescription(productEntity.getProductDescription())
                .setProductPrice(productEntity.getProductPrice().floatValue())
                .setId(productEntity.getId())
                .setProductBarcode(productEntity.getProductBarcode())
                .build();
    }
}
