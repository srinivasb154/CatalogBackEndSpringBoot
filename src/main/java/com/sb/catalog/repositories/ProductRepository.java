package com.sb.catalog.repositories;

import com.sb.catalog.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    // Find products by category ID
    @Query("SELECT p FROM Product p WHERE p.categoryId = :categoryId")
    List<Product> findByCategoryId(UUID categoryId);

    // Find products by brand ID
    @Query("SELECT p FROM Product p WHERE p.brandId = :brandId")
    List<Product> findByBrandId(@Param("brandId") UUID brandId);

    @Query("SELECT p, p.categoryId.categoryName, p.brandId.brandName FROM Product p")
    List<Object[]> findAllWithCategoryAndBrand();
}

