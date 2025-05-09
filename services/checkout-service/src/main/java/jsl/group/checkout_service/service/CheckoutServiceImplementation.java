package jsl.group.checkout_service.service;

import jsl.group.commons.InventoryServiceGrpc;
import jsl.group.commons.InventoryServiceOuterClass;
import jsl.group.commons.models.InventoryResponse;
import jsl.group.commons.models.ItemDetails;
import jsl.group.commons.models.ItemDetailsResponse;
import jsl.group.commons.models.ProductDetailsRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CheckoutServiceImplementation implements CheckoutService {
    private final InventoryServiceGrpc.InventoryServiceBlockingStub inventoryServiceBlockingStub;

    public CheckoutServiceImplementation(InventoryServiceGrpc.InventoryServiceBlockingStub inventoryServiceBlockingStub) {
        this.inventoryServiceBlockingStub = inventoryServiceBlockingStub;
    }

    @Override
    public List<ItemDetailsResponse> checkAvailability(List<ItemDetails> itemDetailsList) {
        List<ItemDetailsResponse> itemDetailsResponseList = new ArrayList<>(itemDetailsList.size());
        for (ItemDetails itemDetails: itemDetailsList) {
            InventoryServiceOuterClass.ItemDetails clientItemDetails = itemDetails(itemDetails);
            InventoryServiceOuterClass.ItemDetailsResponse itemDetailsResponse = inventoryServiceBlockingStub.isAvailable(clientItemDetails);
            ItemDetailsResponse detailsResponse = new ItemDetailsResponse(
                    itemDetailsResponse.getProductId(), itemDetailsResponse.getProductName(), itemDetailsResponse.getInStock()
            );
            itemDetailsResponseList.add(detailsResponse);
        }
        return itemDetailsResponseList;
    }

    @Override
    public List<InventoryResponse> inventoryCleanUp(List<ProductDetailsRequest> productDetailsRequests) {
        List<InventoryResponse> inventoryResponseList = new ArrayList<>(productDetailsRequests.size());
        for (ProductDetailsRequest productDetailsRequest: productDetailsRequests) {
            InventoryServiceOuterClass.ProductDetails productDetails = productDetails(productDetailsRequest);
            int inventory = inventoryServiceBlockingStub.subtractProductCount(productDetails).getValue();
            inventoryResponseList.add(new InventoryResponse(productDetailsRequest.getProductId(), inventory));
        }
        return inventoryResponseList;
    }

    private InventoryServiceOuterClass.ItemDetails itemDetails(ItemDetails itemDetails) {
        return InventoryServiceOuterClass.ItemDetails.newBuilder()
                .setProductId(itemDetails.productId())
                .setQuantity(itemDetails.quantity())
                .build();
    }

    private InventoryServiceOuterClass.ProductDetails productDetails(ProductDetailsRequest productDetailsRequest) {
        return InventoryServiceOuterClass.ProductDetails.newBuilder()
                .setProductName(productDetailsRequest.getProductName())
                .setQuantity(productDetailsRequest.getQuantity())
                .setProductId(InventoryServiceOuterClass.ProductId.newBuilder().setValue(productDetailsRequest.productId).build())
                .build();
    }
}
