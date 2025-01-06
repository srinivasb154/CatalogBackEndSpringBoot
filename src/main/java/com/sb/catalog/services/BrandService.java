package com.sb.catalog.services;

import com.sb.catalog.models.Brand;
import com.sb.catalog.repositories.BrandRepository;
import com.sb.catalog.util.SearchCriteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    // Get all brands
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public List<Brand> searchBrands(SearchCriteria searchCriteria) {
        List<Brand> allBrands = brandRepository.findAll();

        return allBrands.stream()
                .filter(brand -> {
                    boolean matches = true;
                    if (searchCriteria.getName() != null && !searchCriteria.getName().isEmpty()) {
                        matches = brand.getBrandName().contains(searchCriteria.getName());
                    }
                    if (searchCriteria.getDescription() != null && !searchCriteria.getDescription().isEmpty()) {
                        matches = matches && brand.getDescription().contains(searchCriteria.getDescription());
                    }

                    return matches;
                })
                .collect(Collectors.toList());
    }

    // Create a new brand
    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    // Get brand by ID
    public Optional<Brand> getBrandById(UUID id) {
        return brandRepository.findById(id);
    }

    public Optional<Brand> getBrandByBrandName(String name) {
        return Optional.ofNullable(brandRepository.findByBrandName(name));
    }

    // Update an existing brand
    public Brand updateBrand(UUID id, Brand brandDetails) {
        return brandRepository.findById(id).map(existingBrand -> {
            existingBrand.setBrandName(brandDetails.getBrandName());
            existingBrand.setDescription(brandDetails.getDescription());
            existingBrand.setAssets(brandDetails.getAssets());
            return brandRepository.save(existingBrand);
        }).orElseThrow(() -> new IllegalArgumentException("Brand not found"));
    }

    // Delete a brand by ID
    public void deleteBrand(UUID id) {
        brandRepository.deleteById(id);
    }
}
