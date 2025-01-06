package com.sb.catalog.repositories;

import com.sb.catalog.models.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, UUID> {
    @Query("SELECT pi FROM ProductInventory pi WHERE pi.productId = :productId")
    List<ProductInventory> findByProductId(@Param("productId") UUID productId);

    @Query("SELECT i, p.productName FROM ProductInventory i JOIN Product p ON i.productId = p.productId")
    List<Object[]> findAllWithProductName();
}
