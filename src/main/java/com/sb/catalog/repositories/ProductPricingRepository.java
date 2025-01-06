package com.sb.catalog.repositories;

import com.sb.catalog.models.ProductPricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductPricingRepository extends JpaRepository<ProductPricing, UUID> {
    @Query("SELECT pi FROM ProductPricing pi WHERE pi.productId = :productId")
    ProductPricing findByProductId(@Param("productId") UUID productId);

    @Query("SELECT i, p.productName FROM ProductPricing i JOIN Product p ON i.productId = p.productId")
    List<Object[]> findAllWithProductName();
}
