package com.sb.catalog.services;

import com.sb.catalog.models.ProductAsset;
import com.sb.catalog.repositories.ProductAssetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ProductAssetService {

    private final ProductAssetRepository productAssetRepository;

    public ProductAssetService(ProductAssetRepository productAssetRepository) {
        this.productAssetRepository = productAssetRepository;
    }

    // Get all assets for a specific product
    public List<ProductAsset> getAssetsByProductId(UUID productId) {
        return productAssetRepository.findByProduct_ProductId(productId);
    }

    // Get a specific asset by ID
    public Optional<ProductAsset> getAssetById(Integer assetId) {
        return productAssetRepository.findById(assetId);
    }

    // Create a new asset
    public ProductAsset createAsset(ProductAsset productAsset) {
        return productAssetRepository.save(productAsset);
    }

    // Update an existing asset
    public ProductAsset updateAsset(UUID productId, Integer assetId, ProductAsset updatedAsset) {
        return productAssetRepository.findByProduct_ProductIdAndProductAssetId(productId, assetId)
                .map(existingAsset -> {
                    existingAsset.setFileName(updatedAsset.getFileName());
                    existingAsset.setType(updatedAsset.getType());
                    existingAsset.setExtension(updatedAsset.getExtension());
                    existingAsset.setBinaryData(updatedAsset.getBinaryData());
                    return productAssetRepository.save(existingAsset);
                }).orElseThrow(() -> new IllegalArgumentException("Asset not found"));
    }

    // Delete an asset by ID
    public void deleteAsset(UUID productId, Integer assetId) {
        ProductAsset asset = productAssetRepository.findByProduct_ProductIdAndProductAssetId(productId, assetId)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found"));
        productAssetRepository.delete(asset);
    }

    // Delete all assets for a specific product
    public void deleteAssetsByProductId(UUID productId) {
        List<ProductAsset> assets = productAssetRepository.findByProduct_ProductId(productId);
        productAssetRepository.deleteAll(assets);
    }
}
