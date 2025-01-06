package com.sb.catalog.repositories;

import com.sb.catalog.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {
    Brand findByBrandName(String brandName);
}
