package com.sb.catalog.services;

import com.sb.catalog.models.*;
import com.sb.catalog.repositories.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExportService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductSpecificationsRepository productSpecificationsRepository;
    private final ProductReviewRepository productReviewsRepository;
    private final ProductAssetRepository productAssetRepository;
    private final ProductInventoryRepository productInventoryRepository;
    private final ProductPricingRepository productPricingRepository;

    public ExportService(CategoryRepository categoryRepository,
                         ProductRepository productRepository,
                         ProductSpecificationsRepository productSpecificationsRepository,
                         ProductReviewRepository productReviewsRepository,
                         ProductAssetRepository productAssetRepository,
                         ProductInventoryRepository productInventoryRepository,
                         ProductPricingRepository productPricingRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productSpecificationsRepository = productSpecificationsRepository;
        this.productReviewsRepository = productReviewsRepository;
        this.productAssetRepository = productAssetRepository;
        this.productInventoryRepository = productInventoryRepository;
        this.productPricingRepository = productPricingRepository;
    }

    public Map<String, List<Map<String, Object>>> exportData(Map<String, String> columnMappings) {
        // Export Categories
        List<Map<String, Object>> categories = categoryRepository.findAll().stream()
                .map(category -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put(columnMappings.getOrDefault("category_id", "category_id"), category.getCategoryId());
                    map.put(columnMappings.getOrDefault("category_name", "category_name"), category.getCategoryName());
                    map.put(columnMappings.getOrDefault("description", "description"), category.getDescription());
                    return map;
                })
                .toList();

        // Export Products with Category and Brand Names
        List<Map<String, Object>> products = productRepository.findAllWithCategoryAndBrand().stream()
                .map(record -> {
                    Product product = (Product) record[0];
                    String categoryName = (String) record[1];
                    String brandName = (String) record[2];
                    Map<String, Object> map = new HashMap<>();
                    map.put(columnMappings.getOrDefault("product_name", "product_name"), product.getProductName());
                    map.put(columnMappings.getOrDefault("category_name", "category_name"), categoryName);
                    map.put(columnMappings.getOrDefault("brand_name", "brand_name"), brandName);
                    return map;
                })
                .toList();

        // Export Product Specifications
        List<Map<String, Object>> productSpecifications = productSpecificationsRepository.findAllWithProductName().stream()
                .map(record -> {
                    ProductSpecifications spec = (ProductSpecifications) record[0];
                    String productName = (String) record[1];
                    Map<String, Object> map = new HashMap<>();
                    map.put(columnMappings.getOrDefault("product_name", "product_name"), productName);
                    map.put(columnMappings.getOrDefault("weight", "weight"), spec.getWeight());
                    map.put(columnMappings.getOrDefault("color", "color"), spec.getColor());
                    return map;
                })
                .toList();

        // Export Product Reviews
        List<Map<String, Object>> productReviews = productReviewsRepository.findAllWithProductName().stream()
                .map(record -> {
                    ProductReview review = (ProductReview) record[0];
                    String productName = (String) record[1];
                    Map<String, Object> map = new HashMap<>();
                    map.put(columnMappings.getOrDefault("product_name", "product_name"), productName);
                    map.put(columnMappings.getOrDefault("user_name", "user_name"), review.getUserName());
                    map.put(columnMappings.getOrDefault("comment", "comment"), review.getComment());
                    return map;
                })
                .toList();

        // Export Product Assets
        List<Map<String, Object>> productAssets = productAssetRepository.findAllWithProductName().stream()
                .map(record -> {
                    ProductAsset asset = (ProductAsset) record[0];
                    String productName = (String) record[1];
                    Map<String, Object> map = new HashMap<>();
                    map.put(columnMappings.getOrDefault("product_name", "product_name"), productName);
                    map.put(columnMappings.getOrDefault("file_name", "file_name"), asset.getFileName());
                    map.put(columnMappings.getOrDefault("type", "type"), asset.getType());
                    return map;
                })
                .toList();

        // Export Product Inventory
        List<Map<String, Object>> productInventories = productInventoryRepository.findAllWithProductName().stream()
                .map(record -> {
                    ProductInventory inventory = (ProductInventory) record[0];
                    String productName = (String) record[1];
                    Map<String, Object> map = new HashMap<>();
                    map.put(columnMappings.getOrDefault("product_name", "product_name"), productName);
                    map.put(columnMappings.getOrDefault("bin", "bin"), inventory.getBin());
                    map.put(columnMappings.getOrDefault("location", "location"), inventory.getLocation());
                    return map;
                })
                .toList();

        // Export Product Pricing
        List<Map<String, Object>> productPricing = productPricingRepository.findAllWithProductName().stream()
                .map(record -> {
                    ProductPricing pricing = (ProductPricing) record[0];
                    String productName = (String) record[1];
                    Map<String, Object> map = new HashMap<>();
                    map.put(columnMappings.getOrDefault("product_name", "product_name"), productName);
                    map.put(columnMappings.getOrDefault("msrp", "msrp"), pricing.getMsrp());
                    map.put(columnMappings.getOrDefault("map", "map"), pricing.getMap());
                    return map;
                })
                .toList();

        return Map.of(
                "categories", categories,
                "products", products,
                "productSpecifications", productSpecifications,
                "productReviews", productReviews,
                "productAssets", productAssets,
                "productInventories", productInventories,
                "productPricing", productPricing
        );
    }
}


