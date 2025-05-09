package jsl.group.checkout_service.service;

import jsl.group.commons.models.InventoryResponse;
import jsl.group.commons.models.ItemDetails;
import jsl.group.commons.models.ItemDetailsResponse;
import jsl.group.commons.models.ProductDetailsRequest;

import java.util.List;

public interface CheckoutService {
    List<ItemDetailsResponse> checkAvailability(List<ItemDetails> itemDetailsList);
    List<InventoryResponse> inventoryCleanUp(List<ProductDetailsRequest> productDetailsRequests);
}
