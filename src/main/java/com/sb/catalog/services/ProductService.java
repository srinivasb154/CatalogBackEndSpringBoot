package com.sb.catalog.services;

import com.sb.catalog.models.Product;
import com.sb.catalog.models.Category;
import com.sb.catalog.models.Brand;
import com.sb.catalog.models.ProductSpecifications;
import com.sb.catalog.repositories.ProductRepository;
import com.sb.catalog.repositories.ProductSpecificationsRepository;
import com.sb.catalog.util.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductSpecificationsRepository productSpecificationsRepository;
    private final CategoryService categoryService;
    private final BrandService brandService;


    public ProductService(ProductRepository productRepository,
                          ProductSpecificationsRepository productSpecificationsRepository,
                          CategoryService categoryService,
                          BrandService brandService) {
        this.productRepository = productRepository;
        this.productSpecificationsRepository = productSpecificationsRepository;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get product by ID
    public Optional<Product> getProductById(UUID productId) {
        return productRepository.findById(productId);
    }

    // Search products by ProductSearchCriteria
    public List<Product> searchProducts(ProductSearchCriteria criteria) {
        // Match categoryId from categoryName if provided
        UUID categoryId;
        if (criteria.getCategoryName() != null && !criteria.getCategoryName().isEmpty()) {
            categoryId = categoryService.getCategoryByCategoryName(criteria.getCategoryName())
                    .map(Category::getCategoryId)
                    .orElse(null);
        } else {
            categoryId = null;
        }

        // Match brandId from brandName if provided
        UUID brandId;
        if (criteria.getBrandName() != null && !criteria.getBrandName().isEmpty()) {
            brandId = brandService.getBrandByBrandName(criteria.getBrandName())
                    .map(Brand::getBrandId)
                    .orElse(null);
        } else {
            brandId = null;
        }

        // Fetch and filter products based on criteria
        return productRepository.findAll().stream()
                .filter(product ->
                        (criteria.getProductName() == null || criteria.getProductName().isEmpty() ||
                                product.getProductName().equalsIgnoreCase(criteria.getProductName())) &&
                                (criteria.getSku() == null || criteria.getSku().isEmpty() ||
                                        product.getSku().equalsIgnoreCase(criteria.getSku())) &&
                                (categoryId == null || (product.getCategoryId() != null &&
                                        product.getCategoryId().getCategoryId().equals(categoryId))) &&
                                (brandId == null || (product.getBrandId() != null &&
                                        product.getBrandId().getBrandId().equals(brandId)))
                )
                .collect(Collectors.toList());
    }

    // Create a new product
    public Product createProduct(Product product) {
        if (product.getProductSpecifications() != null) {
            ProductSpecifications specifications = product.getProductSpecifications();
            specifications.setProductSpec(product);
            productSpecificationsRepository.save(specifications);
        } else {
            product.setProductSpecifications(null);
        }
        return productRepository.save(product);
    }

    // Update an existing product
    public Product updateProduct(UUID productId, Product productDetails) {
        return productRepository.findById(productId).map(existingProduct -> {
            existingProduct.setProductName(productDetails.getProductName());
            existingProduct.setSku(productDetails.getSku());
            existingProduct.setShortDescription(productDetails.getShortDescription());
            existingProduct.setLongDescription(productDetails.getLongDescription());
            existingProduct.setShippingNotes(productDetails.getShippingNotes());
            existingProduct.setWarrantyInfo(productDetails.getWarrantyInfo());
            existingProduct.setVisibleToFrontEnd(productDetails.isVisibleToFrontEnd());
            existingProduct.setFeaturedProduct(productDetails.isFeaturedProduct());
            existingProduct.setCategoryId(productDetails.getCategoryId());
            existingProduct.setBrandId(productDetails.getBrandId());

            if (productDetails.getProductSpecifications() != null) {
                ProductSpecifications specifications = productDetails.getProductSpecifications();
                specifications.setProductSpec(existingProduct);
                productSpecificationsRepository.save(specifications);
                existingProduct.setProductSpecifications(specifications);
            } else {
                if (existingProduct.getProductSpecifications() != null) {
                    productSpecificationsRepository.delete(existingProduct.getProductSpecifications());
                }
                existingProduct.setProductSpecifications(null);
            }

            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    // Delete a product by ID
    public void deleteProduct(UUID productId) {
        productRepository.findById(productId).ifPresent(product -> {
            if (product.getProductSpecifications() != null) {
                productSpecificationsRepository.delete(product.getProductSpecifications());
            }
            productRepository.delete(product);
        });
    }

    // Get products by category ID
    public List<Product> getProductsByCategoryId(UUID categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    // Get products by brand ID
    public List<Product> getProductsByBrandId(UUID brandId) {
        return productRepository.findByBrandId(brandId);
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    public void saveAllProducts(List<Product> products) {
        productRepository.saveAll(products);
    }
}
