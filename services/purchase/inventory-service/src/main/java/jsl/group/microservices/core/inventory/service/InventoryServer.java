package jsl.group.microservices.core.inventory.service;

import com.google.protobuf.Int32Value;
import io.grpc.stub.StreamObserver;
import jsl.group.commons.InventoryServiceGrpc;
import jsl.group.commons.InventoryServiceOuterClass;
import jsl.group.commons.models.ItemDetails;
import jsl.group.commons.models.ItemDetailsResponse;
import jsl.group.microservices.core.inventory.entities.InventoryService;
import jsl.group.microservices.core.inventory.entities.ProductDetailsEntity;
import jsl.group.microservices.core.inventory.entities.ProductDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InventoryServer extends InventoryServiceGrpc.InventoryServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(InventoryServer.class);
    private final InventoryService inventoryService;
    private final ProductDetailsMapper productDetailsMapper;

    public InventoryServer(InventoryService inventoryService, ProductDetailsMapper productDetailsMapper) {
        this.inventoryService = inventoryService;
        this.productDetailsMapper = productDetailsMapper;
    }

    @Override
    public void addProductCount(InventoryServiceOuterClass.ProductDetails productDetails, StreamObserver<Int32Value> int32ValueStreamObserver) {
        ProductDetailsEntity productDetailsEntity = productDetailsMapper.toDetailsEntity(productDetails);
        String productId = productDetails.getProductId().getValue();
        productDetailsEntity.setProductId(productId);
        int incrementedValue = inventoryService.addProductCount(productDetailsEntity);
        int32ValueStreamObserver.onNext(Int32Value.newBuilder().setValue(incrementedValue).build());
        int32ValueStreamObserver.onCompleted();
        log.info("Total items for {} is {}", productId, incrementedValue);
    }

    @Override
    public void subtractProductCount(InventoryServiceOuterClass.ProductDetails productDetails, StreamObserver<Int32Value> int32ValueStreamObserver) {
        ProductDetailsEntity productDetailsEntity = productDetailsMapper.toDetailsEntity(productDetails);
        String productId = productDetails.getProductId().getValue();
        productDetailsEntity.setProductId(productId);
        int decrementedValue = inventoryService.subtractProductCount(productDetailsEntity);
        int32ValueStreamObserver.onNext(Int32Value.newBuilder().setValue(decrementedValue).build());
        int32ValueStreamObserver.onCompleted();
        log.info("Total items available for {} is {}", productId, decrementedValue);
    }

    @Override
    public void isAvailable(InventoryServiceOuterClass.ItemDetails itemDetails,
                            StreamObserver<InventoryServiceOuterClass.ItemDetailsResponse> detailsResponseStreamObserver) {
        ItemDetails clientItemDetails = new ItemDetails(itemDetails.getProductId(), itemDetails.getQuantity());
        ItemDetailsResponse itemDetailsResponse = inventoryService.isAvailable(clientItemDetails);
        InventoryServiceOuterClass.ItemDetailsResponse serverItemsResponse = InventoryServiceOuterClass.ItemDetailsResponse.newBuilder()
                .setProductId(itemDetailsResponse.productId())
                .setProductName(itemDetailsResponse.productName())
                .setInStock(itemDetailsResponse.inStock())
                .build();
        detailsResponseStreamObserver.onNext(serverItemsResponse);
        detailsResponseStreamObserver.onCompleted();
        log.info("Item availability retrieved successfully: {}", itemDetailsResponse);
    }

}
