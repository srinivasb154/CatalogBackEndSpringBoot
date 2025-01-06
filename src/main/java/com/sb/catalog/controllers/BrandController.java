package com.sb.catalog.controllers;

import com.sb.catalog.models.Brand;
import com.sb.catalog.models.Category;
import com.sb.catalog.services.BrandService;
import com.sb.catalog.util.SearchCriteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    // Get all brands
    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Brand>> searchBrands(@RequestBody SearchCriteria searchCriteria) {
        List<Brand> brands = brandService.searchBrands(searchCriteria);
        return ResponseEntity.ok(brands);
    }

    // Create a new brand
    @PostMapping
    public ResponseEntity<Brand> createBrand(@RequestBody Brand brand) {
        Brand createdBrand = brandService.createBrand(brand);
        return ResponseEntity.ok(createdBrand);
    }

    // Get brand by ID
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable UUID id) {
        Optional<Brand> brand = brandService.getBrandById(id);
        return brand.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update an existing brand
    @PatchMapping("/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable UUID id, @RequestBody Brand brand) {
        try {
            Brand updatedBrand = brandService.updateBrand(id, brand);
            return ResponseEntity.ok(updatedBrand);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a brand by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable UUID id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}

