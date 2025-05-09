package jsl.group.microservices.core.product.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProductServiceImplementation implements ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImplementation.class);
    private final ProductEntityRepository productEntityRepository;

    public ProductServiceImplementation(ProductEntityRepository productEntityRepository) {
        this.productEntityRepository = productEntityRepository;
    }

    @Override
    public ProductEntity createProduct(ProductEntity productEntity) {
        return productEntityRepository.save(productEntity);
    }

    @Override
    public ProductEntity updateProduct(String productId, ProductEntity productEntity) {
        AtomicReference<ProductEntity> found = new AtomicReference<>(new ProductEntity());
        productEntityRepository.findById(productId).ifPresentOrElse(
                foundEntity -> {
                    foundEntity.setProductBarcode(productEntity.getProductBarcode());
                    foundEntity.setProductName(productEntity.getProductName());
                    foundEntity.setProductDescription(productEntity.getProductDescription());
                    foundEntity.setProductPrice(productEntity.getProductPrice());
                    foundEntity = productEntityRepository.save(foundEntity);
                    found.set(foundEntity);
                },
                () -> {
                    log.info("Product values not updated properly");
                }
        );
        return found.get();
    }

    @Override
    public ProductEntity getProduct(String productId) {
        ProductEntity productEntity = productEntityRepository.findById(productId).orElseThrow(() -> {
                log.info("Product not found");
                return new RuntimeException("Item not found");
            }
        );
        log.info(productEntity.toString());
        return productEntity;
    }

    @Override
    public String deleteProduct(String productId) {
        productEntityRepository.deleteById(productId);
        return productId;
    }
}
