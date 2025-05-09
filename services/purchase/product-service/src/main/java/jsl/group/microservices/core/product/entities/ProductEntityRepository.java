package jsl.group.microservices.core.product.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, String> {
}
