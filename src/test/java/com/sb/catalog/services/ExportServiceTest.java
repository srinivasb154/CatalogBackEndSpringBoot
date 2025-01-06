package com.sb.catalog.services;

import com.sb.catalog.models.*;
import com.sb.catalog.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ExportServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductSpecificationsRepository productSpecificationsRepository;

    @Mock
    private ProductReviewRepository productReviewsRepository;

    @Mock
    private ProductAssetRepository productAssetRepository;

    @Mock
    private ProductInventoryRepository productInventoryRepository;

    @Mock
    private ProductPricingRepository productPricingRepository;

    @InjectMocks
    private ExportService exportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExportData() {
        // Mock data for categories
        Category category = new Category();
        category.setCategoryId(UUID.randomUUID());
        category.setCategoryName("Electronics");
        category.setDescription("Electronics Category");

        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));

        // Mock data for products
        Product product = new Product();
        product.setProductName("Smartphone");
        String categoryName = "Electronics";
        String brandName = "BrandX";

        when(productRepository.findAllWithCategoryAndBrand()).thenReturn(
                Collections.singletonList(new Object[]{product, categoryName, brandName})
        );

        // Mock data for product specifications
        ProductSpecifications spec = new ProductSpecifications();
        spec.setWeight("1kg");
        spec.setColor("Black");

        when(productSpecificationsRepository.findAllWithProductName()).thenReturn(
                Collections.singletonList(new Object[]{spec, "Smartphone"})
        );

        // Mock data for product reviews
        ProductReview review = new ProductReview();
        review.setUserName("User1");
        review.setComment("Great product!");

        when(productReviewsRepository.findAllWithProductName()).thenReturn(
                Collections.singletonList(new Object[]{review, "Smartphone"})
        );

        // Mock data for product assets
        ProductAsset asset = new ProductAsset();
        asset.setFileName("image.jpg");
        asset.setType("image");

        when(productAssetRepository.findAllWithProductName()).thenReturn(
                Collections.singletonList(new Object[]{asset, "Smartphone"})
        );

        // Mock data for product inventory
        ProductInventory inventory = new ProductInventory();
        inventory.setBin("A1");
        inventory.setLocation("Warehouse");

        when(productInventoryRepository.findAllWithProductName()).thenReturn(
                Collections.singletonList(new Object[]{inventory, "Smartphone"})
        );

        // Mock data for product pricing
        ProductPricing pricing = new ProductPricing();
        pricing.setMsrp(BigDecimal.valueOf(699.99));
        pricing.setMap(BigDecimal.valueOf(599.99));

        when(productPricingRepository.findAllWithProductName()).thenReturn(
                Collections.singletonList(new Object[]{pricing, "Smartphone"})
        );

        // Mock column mappings
        Map<String, String> columnMappings = getColumnMappings();

        // Call the service
        Map<String, List<Map<String, Object>>> result = exportService.exportData(columnMappings);

        // Verify categories data
        assertEquals(1, result.get("categories").size());
        assertEquals("Electronics", result.get("categories").getFirst().get("category_name"));

        // Verify products data
        assertEquals(1, result.get("products").size());
        assertEquals("Smartphone", result.get("products").getFirst().get("product_name"));
        assertEquals("Electronics", result.get("products").getFirst().get("category_name"));
        assertEquals("BrandX", result.get("products").getFirst().get("brand_name"));

        // Verify product specifications data
        assertEquals(1, result.get("productSpecifications").size());
        assertEquals("Smartphone", result.get("productSpecifications").getFirst().get("product_name"));
        assertEquals("1kg", result.get("productSpecifications").getFirst().get("weight"));

        // Verify product reviews data
        assertEquals(1, result.get("productReviews").size());
        assertEquals("Smartphone", result.get("productReviews").getFirst().get("product_name"));
        assertEquals("User1", result.get("productReviews").getFirst().get("user_name"));

        // Verify product assets data
        assertEquals(1, result.get("productAssets").size());
        assertEquals("Smartphone", result.get("productAssets").getFirst().get("product_name"));
        assertEquals("image.jpg", result.get("productAssets").getFirst().get("file_name"));

        // Verify product inventory data
        assertEquals(1, result.get("productInventories").size());
        assertEquals("Smartphone", result.get("productInventories").getFirst().get("product_name"));
        assertEquals("A1", result.get("productInventories").getFirst().get("bin"));

        // Verify product pricing data
        assertEquals(1, result.get("productPricing").size());
        assertEquals("Smartphone", result.get("productPricing").getFirst().get("product_name"));
        assertEquals(BigDecimal.valueOf(699.99), result.get("productPricing").getFirst().get("msrp"));

        // Verify repository calls
        verify(categoryRepository, times(1)).findAll();
        verify(productRepository, times(1)).findAllWithCategoryAndBrand();
        verify(productSpecificationsRepository, times(1)).findAllWithProductName();
        verify(productReviewsRepository, times(1)).findAllWithProductName();
        verify(productAssetRepository, times(1)).findAllWithProductName();
        verify(productInventoryRepository, times(1)).findAllWithProductName();
        verify(productPricingRepository, times(1)).findAllWithProductName();
    }

    private static Map<String, String> getColumnMappings() {
        Map<String, String> columnMappings = new HashMap<>();
        columnMappings.put("category_id", "category_id");
        columnMappings.put("category_name", "category_name");
        columnMappings.put("description", "description");
        columnMappings.put("product_name", "product_name");
        columnMappings.put("weight", "weight");
        columnMappings.put("color", "color");
        columnMappings.put("user_name", "user_name");
        columnMappings.put("comment", "comment");
        columnMappings.put("file_name", "file_name");
        columnMappings.put("type", "type");
        columnMappings.put("bin", "bin");
        columnMappings.put("location", "location");
        columnMappings.put("msrp", "msrp");
        columnMappings.put("map", "map");
        return columnMappings;
    }
}
