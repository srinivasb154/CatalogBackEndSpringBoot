package com.sb.catalog.services;

import com.sb.catalog.models.ProductPricing;
import com.sb.catalog.repositories.ProductPricingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductPricingService {

    private final ProductPricingRepository productPricingRepository;

    public ProductPricingService(ProductPricingRepository productPricingRepository) {
        this.productPricingRepository = productPricingRepository;
    }

    public List<ProductPricing> getAllProductPricing() {
        return productPricingRepository.findAll();
    }

    public Optional<ProductPricing> getProductPricingById(UUID productId) {
        ProductPricing productPricing = productPricingRepository.findById(productId).orElse(null);
        return Optional.ofNullable(productPricing);
        //return Optional.ofNullable(productPricingRepository.findByProductId(productId));
    }

    @Transactional
    public ProductPricing saveProductPricing(ProductPricing productPricing) {
        return productPricingRepository.save(productPricing);
    }

    @Transactional
    public ProductPricing updateProductPricing(UUID productId, ProductPricing updatedPricing) {
        return productPricingRepository.findById(productId)
                .map(existingPricing -> {
                    existingPricing.setMsrp(updatedPricing.getMsrp());
                    existingPricing.setMap(updatedPricing.getMap());
                    existingPricing.setCost(updatedPricing.getCost());
                    existingPricing.setSell(updatedPricing.getSell());
                    existingPricing.setBase(updatedPricing.getBase());
                    existingPricing.setStartDate(updatedPricing.getStartDate());
                    existingPricing.setEndDate(updatedPricing.getEndDate());
                    existingPricing.setCreatedBy(updatedPricing.getCreatedBy());
                    return productPricingRepository.save(existingPricing);
                })
                .orElseThrow(() -> new RuntimeException("ProductPricing not found with id: " + productId));
    }

    @Transactional
    public void deleteProductPricing(UUID productId) {
        if (productPricingRepository.existsById(productId)) {
            productPricingRepository.deleteById(productId);
        } else {
            throw new RuntimeException("ProductPricing not found with id: " + productId);
        }
    }
}
