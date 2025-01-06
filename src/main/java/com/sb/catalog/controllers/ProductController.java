package com.sb.catalog.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.catalog.dto.*;
import com.sb.catalog.mappers.ProductMapper;
import com.sb.catalog.models.Product;
import com.sb.catalog.models.ProductAsset;
import com.sb.catalog.models.ProductPricing;
import com.sb.catalog.models.ProductReview;
import com.sb.catalog.services.*;
import com.sb.catalog.util.ProductSearchCriteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductAssetService productAssetService;
    private final ProductReviewService productReviewService;
    private final ProductInventoryService productInventoryService;
    private final ProductPricingService productPricingService;
    final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductAssetService productAssetService,
                             ProductReviewService productReviewService, ProductInventoryService productInventoryService,
                             ProductPricingService productPricingService, ProductMapper productMapper) {
        this.productService = productService;
        this.productAssetService = productAssetService;
        this.productReviewService = productReviewService;
        this.productInventoryService = productInventoryService;
        this.productPricingService = productPricingService;
        this.productMapper = productMapper;
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductRequest>> getAllProducts() {
        List<ProductRequest> products = productService.getAllProducts().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductRequest> getProductById(@PathVariable UUID id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(productMapper.toDto(product)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestBody ProductSearchCriteria productSearchCriteria) {
        List<Product> products = productService.searchProducts(productSearchCriteria);
        return ResponseEntity.ok(products);
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<ProductRequest> createProduct(@RequestBody ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(productMapper.toDto(createdProduct));
    }

    // Update an existing product
    @PatchMapping("/{id}")
    public ResponseEntity<ProductRequest> updateProduct(@PathVariable UUID id,
                                                        @RequestBody ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(productMapper.toDto(updatedProduct));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Get products by category ID
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductRequest>> getProductsByCategoryId(@PathVariable UUID categoryId) {
        List<ProductRequest> products = productService.getProductsByCategoryId(categoryId).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    // Get products by brand ID
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<ProductRequest>> getProductsByBrandId(@PathVariable UUID brandId) {
        List<ProductRequest> products = productService.getProductsByBrandId(brandId).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    // Get product specifications by product ID
    @GetMapping("/{id}/specifications")
    public ResponseEntity<ProductSpecificationsDTO> getProductSpecifications(@PathVariable UUID id) {
        Optional<Product> productOptional = productService.getProductById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (product.getProductSpecifications() != null) {
                ProductSpecificationsDTO specificationsDTO = productMapper.toDto(product.getProductSpecifications());
                return ResponseEntity.ok(specificationsDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Save product assets
    @PostMapping("/{_id}/assets")
    public ResponseEntity<?> saveProductAssets(@PathVariable("_id") UUID productId,
                                               @RequestParam("assets") String assetsJson,
                                               @RequestParam("files") List<MultipartFile> files) {
        try {
            List<ProductAssetsDTO> assets = new ObjectMapper().readValue(assetsJson, new TypeReference<>() {
            });

            if (assets.size() != files.size()) {
                throw new IllegalArgumentException("Number of files does not match number of assets.");
            }

            // Construct the assets with binary data
            List<ProductAsset> constructedAssets = assets.stream().map(assetDTO -> {
                int index = assets.indexOf(assetDTO);
                MultipartFile file = files.get(index);

                if (file == null) {
                    throw new IllegalArgumentException("Missing file for asset with ID: " +
                            assetDTO.getProductAssetId());
                }

                // Map DTO to Entity
                ProductAsset productAsset = new ProductAsset();
                productAsset.setProductAssetId(assetDTO.getProductAssetId());
                productAsset.setProduct(productService.getProductById(productId).orElseThrow(
                        () -> new IllegalArgumentException("Invalid Product ID")));
                productAsset.setFileName(assetDTO.getFileName());
                productAsset.setType(assetDTO.getType());
                productAsset.setExtension(assetDTO.getExtension());
                try {
                    productAsset.setBinaryData(file.getBytes());
                } catch (Exception e) {
                    throw new RuntimeException("Error reading file data.", e);
                }

                return productAsset;
            }).toList();

            // Save assets
            List<ProductAsset> savedAssets = constructedAssets.stream()
                    .map(productAssetService::createAsset)
                    .collect(Collectors.toList());

            return ResponseEntity.status(201).body(savedAssets);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving assets: " + e.getMessage());
        }
    }

    // Get assets for a specific product
    @GetMapping("/{productId}/assets")
    public ResponseEntity<?> getProductAssets(@PathVariable UUID productId) {
        try {
            List<ProductAsset> assets = productAssetService.getAssetsByProductId(productId);

            if (assets.isEmpty()) {
                return ResponseEntity.status(404).body("No assets found for this product.");
            }

            List<ProductAssetsDTO> assetDTOs = assets.stream()
                    .map(productMapper::toDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(assetDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching assets: " + e.getMessage());
        }
    }

    // Get product reviews for a specific product
    @GetMapping("/{_id}/reviews")
    public ResponseEntity<List<ProductReviewDTO>> getProductReviews(@PathVariable("_id") UUID productId) {
        List<ProductReview> reviews = productReviewService.getReviewsByProductId(productId);
        List<ProductReviewDTO> reviewDTOs = reviews.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviewDTOs);
    }

    // Save product reviews for a specific product
    @PostMapping("/{productId}/reviews")
    public ResponseEntity<List<ProductReviewDTO>> saveProductReviews(
            @PathVariable UUID productId,
            @RequestBody Map<String, List<ProductReviewDTO>> payload) {
        List<ProductReviewDTO> reviewsDTO = payload.get("reviews");

        if (reviewsDTO == null) {
            return ResponseEntity.badRequest().body(null);
        }

        List<ProductReview> reviews = reviewsDTO.stream()
                .map(dto -> {
                    ProductReview review = productMapper.toEntity(dto);
                    review.setProductId(productId);
                    return review;
                }).toList();

        List<ProductReview> savedReviews = reviews.stream()
                .map(productReviewService::saveReview)
                .toList();

        List<ProductReviewDTO> savedReviewDTOs = savedReviews.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.status(201).body(savedReviewDTOs);
    }

    // Route to get inventory details for a product
    @GetMapping("/{productId}/inventory")
    public ResponseEntity<List<ProductInventoryDTO>> getInventoryByProductId(@PathVariable UUID productId) {
        var inventories = productInventoryService.getInventoriesByProductId(productId);
        if (inventories.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var inventoryDTOs = inventories.stream()
                .map(productMapper::toDto)
                .toList();
        return ResponseEntity.ok(inventoryDTOs);
    }

    @PostMapping("/inventory")
    public ResponseEntity<Map<String, Object>> createOrUpdateInventory(@RequestBody ProductInventoryDTO productInventoryDTO) {
        var inventory = productMapper.toEntity(productInventoryDTO);
        var savedInventory = productInventoryService.saveProductInventory(inventory);
        var response = Map.of(
                "message", "Inventory saved successfully.",
                "inventory", productMapper.toDto(savedInventory)
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/inventory")
    public ResponseEntity<Map<String, Object>> updateInventory(@RequestBody ProductInventoryDTO productInventoryDTO) {
        var inventory = productMapper.toEntity(productInventoryDTO);
        var updatedInventory = productInventoryService.updateProductInventory(productInventoryDTO.getProductId(), inventory);
        var response = Map.of(
                "message", "Inventory updated successfully.",
                "inventory", productMapper.toDto(updatedInventory)
        );
        return ResponseEntity.ok(response);
    }

    // Route to get pricing details for a product
    @GetMapping("/{productId}/pricing")
    public ResponseEntity<List<ProductPricingDTO>> getPricingByProductId(@PathVariable UUID productId) {
        Optional<ProductPricing> optPriceEntity = productPricingService.getProductPricingById(productId);

        if (optPriceEntity.isPresent()) {
            ProductPricingDTO pricingDTO = productMapper.toDto(optPriceEntity.get());
            return ResponseEntity.ok(List.of(pricingDTO));
        } else {
            return ResponseEntity.ok(List.of());
        }
    }

    // Route to create or update pricing details
    @PostMapping("/pricing")
    public ResponseEntity<Map<String, Object>> createOrUpdatePricing(@RequestBody ProductPricingDTO productPricingDTO) {
        var pricing = productMapper.toEntity(productPricingDTO);
        var savedPricing = productPricingService.saveProductPricing(pricing);
        var response = Map.of(
                "message", "Pricing saved successfully.",
                "pricing", productMapper.toDto(savedPricing)
        );
        return ResponseEntity.ok(response);
    }

    // Route to partially update pricing details
    @PatchMapping("/pricing")
    public ResponseEntity<Map<String, Object>> updatePricing(@RequestBody ProductPricingDTO productPricingDTO) {
        var pricing = productMapper.toEntity(productPricingDTO);
        var updatedPricing = productPricingService.updateProductPricing(productPricingDTO.getProductId(), pricing);
        var response = Map.of(
                "message", "Pricing updated successfully.",
                "pricing", productMapper.toDto(updatedPricing)
        );
        return ResponseEntity.ok(response);
    }
}

