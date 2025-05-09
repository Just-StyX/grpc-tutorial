package jsl.group.microservices.core.product.entities;

import jsl.group.commons.ProductServiceOuterClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true),
    })
    ProductEntity productToProductEntity(ProductServiceOuterClass.Product product);
}
