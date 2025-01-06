package com.sb.catalog.services;

import com.sb.catalog.models.Brand;
import com.sb.catalog.models.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductImportServiceTest {

    private ProductService productService;
    private CategoryService categoryService;
    private BrandService brandService;
    private ProductImportService productImportService;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        categoryService = mock(CategoryService.class);
        brandService = mock(BrandService.class);
        productImportService = new ProductImportService(productService, categoryService, brandService);
    }

    @Test
    void testImportProducts_AddMode() throws Exception {
        String csvContent = """
                productName,sku,categoryName,brandName
                Product1,SKU1,Category1,Brand1
                Product2,SKU2,Category2,Brand2
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        when(categoryService.getCategoryByCategoryName(anyString()))
                .thenReturn(Optional.of(new Category()));

        when(brandService.getBrandByBrandName(anyString()))
                .thenReturn(Optional.of(new Brand())); // Mock Brand

        productImportService.importProducts(file, "add");

        verify(productService, times(1)).saveAllProducts(anyList());
        verify(productService, never()).deleteAllProducts();
    }

    @Test
    void testImportProducts_ReplaceMode() throws Exception {
        String csvContent = """
                productName,sku,categoryName,brandName
                Product1,SKU1,Category1,Brand1
                Product2,SKU2,Category2,Brand2
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        when(categoryService.getCategoryByCategoryName(anyString()))
                .thenReturn(Optional.of(new Category()));

        when(brandService.getBrandByBrandName(anyString()))
                .thenReturn(Optional.of(new Brand())); // Mock Brand

        productImportService.importProducts(file, "replace");

        verify(productService, times(1)).deleteAllProducts();
        verify(productService, times(1)).saveAllProducts(anyList());
    }

    @Test
    void testImportProducts_InvalidMode() {
        String csvContent = """
                productName,sku,categoryName,brandName
                Product1,SKU1,Category1,Brand1
                Product2,SKU2,Category2,Brand2
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                productImportService.importProducts(file, "invalid")
        );

        assertEquals("Invalid mode: invalid", exception.getMessage());
    }
}