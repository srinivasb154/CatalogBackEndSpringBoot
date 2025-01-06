package com.sb.catalog.services;

import com.sb.catalog.models.Brand;
import com.sb.catalog.models.Category;
import com.sb.catalog.models.Product;
import com.sb.catalog.models.ProductSpecifications;
import com.sb.catalog.repositories.ProductRepository;
import com.sb.catalog.repositories.ProductSpecificationsRepository;
import com.sb.catalog.util.ProductSearchCriteria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductSpecificationsRepository productSpecificationsRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BrandService brandService;

    @InjectMocks
    private ProductService productService;

    private UUID sampleProductId;
    private Product sampleProduct;
    private ProductSpecifications sampleSpecifications;

    private Category sampleCategory;
    private Brand sampleBrand;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);

        sampleProductId = UUID.randomUUID();
        sampleCategory = new Category(UUID.randomUUID(), "Electronics");
        sampleBrand = new Brand(UUID.randomUUID(), "Samsung", "Description", null,
                null, null);
        sampleSpecifications = new ProductSpecifications(sampleProductId, "1kg", "Red", "10x10x10",
                "1L", "Plastic", "USA", "Medium", "100W", "220V",
                "Waterproof", null, null, null);
        sampleProduct = new Product(sampleProductId, "Sample Product", "SKU123",
                "Short description", "Long description", "Shipping notes",
                "Warranty info", true, false, null, null,
                null, null, sampleSpecifications, null);
        sampleSpecifications.setProductSpec(sampleProduct);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(sampleProduct));

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Sample Product", products.getFirst().getProductName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductById() {
        when(productRepository.findById(sampleProductId)).thenReturn(Optional.of(sampleProduct));

        Optional<Product> product = productService.getProductById(sampleProductId);

        assertTrue(product.isPresent());
        assertEquals("Sample Product", product.get().getProductName());
        verify(productRepository, times(1)).findById(sampleProductId);
    }

    @Test
    public void testSearchProducts() {
        ProductSearchCriteria criteria = new ProductSearchCriteria("Sample Product", "SKU123", "Electronics", "Samsung");

        // Set up a valid Category
        UUID sampleCategoryId = UUID.randomUUID();
        sampleCategory = new Category(sampleCategoryId, "Electronics", null, "Electronics Description",
                "1", true, false, false, null, null);
        sampleProduct.setCategoryId(sampleCategory);

        // Set up a valid Brand
        UUID sampleBrandId = UUID.randomUUID();
        sampleBrand = new Brand(sampleBrandId, "Samsung", "Description", null, null, null);
        sampleProduct.setBrandId(sampleBrand);

        // Mocking services
        when(categoryService.getCategoryByCategoryName("Electronics"))
                .thenReturn(Optional.of(sampleCategory));
        when(brandService.getBrandByBrandName("Samsung"))
                .thenReturn(Optional.of(sampleBrand));
        when(productRepository.findAll()).thenReturn(Collections.singletonList(sampleProduct));

        // Perform search
        List<Product> result = productService.searchProducts(criteria);

        // Assertions
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Expected one matching product");
        assertEquals("Sample Product", result.getFirst().getProductName());
        verify(categoryService, times(1)).getCategoryByCategoryName("Electronics");
        verify(brandService, times(1)).getBrandByBrandName("Samsung");
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testSearchProducts_NoMatch() {
        ProductSearchCriteria criteria = new ProductSearchCriteria("Nonexistent Product", null, "Invalid Category", "Invalid Brand");

        when(categoryService.getCategoryByCategoryName("Invalid Category"))
                .thenReturn(Optional.empty());
        when(brandService.getBrandByBrandName("Invalid Brand"))
                .thenReturn(Optional.empty());
        when(productRepository.findAll()).thenReturn(Collections.singletonList(sampleProduct));

        List<Product> result = productService.searchProducts(criteria);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryService, times(1)).getCategoryByCategoryName("Invalid Category");
        verify(brandService, times(1)).getBrandByBrandName("Invalid Brand");
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testCreateProductWithSpecifications() {
        when(productSpecificationsRepository.save(any(ProductSpecifications.class))).thenReturn(sampleSpecifications);
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        Product createdProduct = productService.createProduct(sampleProduct);

        assertNotNull(createdProduct);
        assertEquals("Sample Product", createdProduct.getProductName());
        assertNotNull(createdProduct.getProductSpecifications());
        verify(productSpecificationsRepository, times(1)).save(any(ProductSpecifications.class));
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    public void testCreateProductWithoutSpecifications() {
        sampleProduct.setProductSpecifications(null);
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        Product createdProduct = productService.createProduct(sampleProduct);

        assertNotNull(createdProduct);
        assertNull(createdProduct.getProductSpecifications());
        verify(productSpecificationsRepository, never()).save(any(ProductSpecifications.class));
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    public void testUpdateProductWithSpecifications() {
        when(productRepository.findById(sampleProductId)).thenReturn(Optional.of(sampleProduct));
        when(productSpecificationsRepository.save(any(ProductSpecifications.class))).thenReturn(sampleSpecifications);
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        Product updatedProduct = productService.updateProduct(sampleProductId, sampleProduct);

        assertNotNull(updatedProduct);
        assertEquals("Sample Product", updatedProduct.getProductName());
        assertNotNull(updatedProduct.getProductSpecifications());
        verify(productSpecificationsRepository, times(1)).save(any(ProductSpecifications.class));
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    public void testUpdateProductWithoutSpecifications() {
        when(productRepository.findById(sampleProductId)).thenReturn(Optional.of(sampleProduct));
        sampleProduct.setProductSpecifications(null);
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        Product updatedProduct = productService.updateProduct(sampleProductId, sampleProduct);

        assertNotNull(updatedProduct);
        assertNull(updatedProduct.getProductSpecifications());
        verify(productSpecificationsRepository, never()).save(any(ProductSpecifications.class));
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    public void testDeleteProductWithSpecifications() {
        when(productRepository.findById(sampleProductId)).thenReturn(Optional.of(sampleProduct));
        doNothing().when(productSpecificationsRepository).delete(any(ProductSpecifications.class));
        doNothing().when(productRepository).delete(any(Product.class));

        productService.deleteProduct(sampleProductId);

        verify(productSpecificationsRepository, times(1)).delete(any(ProductSpecifications.class));
        verify(productRepository, times(1)).delete(sampleProduct);
    }

    @Test
    public void testDeleteProductWithoutSpecifications() {
        sampleProduct.setProductSpecifications(null);
        when(productRepository.findById(sampleProductId)).thenReturn(Optional.of(sampleProduct));
        doNothing().when(productRepository).delete(any(Product.class));

        productService.deleteProduct(sampleProductId);

        verify(productSpecificationsRepository, never()).delete(any(ProductSpecifications.class));
        verify(productRepository, times(1)).delete(sampleProduct);
    }
}

