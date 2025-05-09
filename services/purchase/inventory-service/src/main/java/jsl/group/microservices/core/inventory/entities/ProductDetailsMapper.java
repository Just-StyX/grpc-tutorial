package jsl.group.microservices.core.inventory.entities;

import jsl.group.commons.InventoryServiceOuterClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductDetailsMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true),
            @Mapping(target = "productId", ignore = true)
    })
    ProductDetailsEntity toDetailsEntity(InventoryServiceOuterClass.ProductDetails productDetails);
}
