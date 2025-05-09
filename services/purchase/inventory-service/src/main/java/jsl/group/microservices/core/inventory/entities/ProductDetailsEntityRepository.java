package jsl.group.microservices.core.inventory.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductDetailsEntityRepository extends JpaRepository<ProductDetailsEntity, String> {
    Optional<ProductDetailsEntity> findByProductId(String productId);
}
