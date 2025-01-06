package com.sb.catalog.controllers;

import com.sb.catalog.services.ProductImportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductImportControllerIntegrationTest {

    @Mock
    private ProductImportService productImportService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        // Initialize MockitoAnnotations with try-with-resources
        mocks = MockitoAnnotations.openMocks(this);

        // Create a local instance of MockMvc
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new ProductImportController(productImportService)).build();
    }

    @Test
    void testImportProducts_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "productName,sku,categoryName,brandName\nProduct1,SKU1,Category1,Brand1".getBytes()
        );

        doNothing().when(productImportService).importProducts(any(), anyString());

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new ProductImportController(productImportService)).build();

        mockMvc.perform(multipart("/api/products/import")
                        .file(file)
                        .param("mode", "add"))
                .andExpect(status().isOk())
                .andExpect(content().string("Products imported successfully."));
    }

    @Test
    void testImportProducts_FileEmpty() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.csv",
                MediaType.TEXT_PLAIN_VALUE,
                new byte[0]
        );

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new ProductImportController(productImportService)).build();

        mockMvc.perform(multipart("/api/products/import")
                        .file(emptyFile)
                        .param("mode", "add"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No file uploaded. Please upload a CSV file."));
    }

    @Test
    void testImportProducts_InternalServerError() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "productName,sku,categoryName,brandName\nProduct1,SKU1,Category1,Brand1".getBytes()
        );

        doThrow(new RuntimeException("Mocked exception"))
                .when(productImportService).importProducts(any(), anyString());

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new ProductImportController(productImportService)).build();

        mockMvc.perform(multipart("/api/products/import")
                        .file(file)
                        .param("mode", "replace"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error importing products: Mocked exception"));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }
}