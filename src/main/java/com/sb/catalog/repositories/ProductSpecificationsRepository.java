package com.sb.catalog.repositories;

import com.sb.catalog.models.ProductSpecifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductSpecificationsRepository extends JpaRepository<ProductSpecifications, UUID> {
    @Query("SELECT ps, p.productName FROM ProductSpecifications ps JOIN Product p ON ps.productId = p.productId")
    List<Object[]> findAllWithProductName();
}
