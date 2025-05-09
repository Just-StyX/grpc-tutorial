package jsl.group.microservices.core.inventory.entities;

import jsl.group.commons.models.ItemDetails;
import jsl.group.commons.models.ItemDetailsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class InventoryServiceImplementation implements InventoryService {
    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImplementation.class);
    private final ProductDetailsEntityRepository productDetailsEntityRepository;

    public InventoryServiceImplementation(ProductDetailsEntityRepository productDetailsEntityRepository) {
        this.productDetailsEntityRepository = productDetailsEntityRepository;
    }

    @Override
    public int addProductCount(ProductDetailsEntity productDetailsEntity) {
        AtomicInteger atomicInteger = new AtomicInteger(productDetailsEntity.getQuantity());
        productDetailsEntityRepository.findByProductId(productDetailsEntity.getProductId()).ifPresentOrElse(
                foundEntity -> {
                    int newQuantity = foundEntity.getQuantity() + productDetailsEntity.getQuantity();
                    foundEntity.setQuantity(newQuantity);
                    productDetailsEntityRepository.save(foundEntity);
                    atomicInteger.set(newQuantity);
                },
                () -> {
                    log.info("Product inventory with ID {} could not be incremented. Saving a new entry.", productDetailsEntity.getId());
                    productDetailsEntityRepository.save(productDetailsEntity);
                }
        );
        int incrementedValue = atomicInteger.get();
        atomicInteger.set(0);
        return incrementedValue;
    }

    @Override
    public int subtractProductCount(ProductDetailsEntity productDetailsEntity) {
        AtomicInteger atomicInteger = new AtomicInteger(productDetailsEntity.getQuantity());
        productDetailsEntityRepository.findByProductId(productDetailsEntity.getProductId()).ifPresentOrElse(
                foundEntity -> {
                    int newQuantity = foundEntity.getQuantity() - productDetailsEntity.getQuantity();
                    foundEntity.setQuantity(newQuantity);
                    productDetailsEntityRepository.save(foundEntity);
                    atomicInteger.set(newQuantity);
                },
                () -> {
                    log.info("Product inventory with ID {} could not be decremented. Item might be out of stock", productDetailsEntity.getId());
                }
        );
        int decrementedValue = atomicInteger.get();
        atomicInteger.set(0);
        return decrementedValue;
    }

    @Override
    public ItemDetailsResponse isAvailable(ItemDetails itemDetails) {
        Optional<ProductDetailsEntity> productDetailsEntity = productDetailsEntityRepository.findByProductId(itemDetails.productId());
        if (productDetailsEntity.isPresent()) {
            ProductDetailsEntity found = productDetailsEntity.get();
            boolean available = found.getQuantity() >= itemDetails.quantity();
            return new ItemDetailsResponse(found.getProductId(), found.getProductName(), available);
        } else {
            return null;
        }
    }
}
