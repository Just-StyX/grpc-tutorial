package jsl.group.microservices.core.inventory.entities;

import jsl.group.commons.models.ItemDetails;
import jsl.group.commons.models.ItemDetailsResponse;

public interface InventoryService {
    int addProductCount(ProductDetailsEntity productDetailsEntity);
    int subtractProductCount(ProductDetailsEntity productDetailsEntity);
    ItemDetailsResponse isAvailable(ItemDetails itemDetails);
}
