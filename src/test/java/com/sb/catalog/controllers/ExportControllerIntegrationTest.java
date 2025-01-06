package com.sb.catalog.controllers;

import com.sb.catalog.services.ExportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ExportControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private ExportService exportService;

    @InjectMocks
    private ExportController exportController;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(exportController).build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testExportData_Success() throws Exception {
        // Mock data
        Map<String, List<Map<String, Object>>> mockData = Map.of(
                "categories", List.of(
                        Map.of("category_id", "1", "category_name", "Electronics", "description", "Category Description")
                ),
                "products", List.of(
                        Map.of("product_name", "Product A", "category_name", "Electronics", "brand_name", "Brand A")
                )
        );

        // Mock service response
        when(exportService.exportData(Collections.emptyMap())).thenReturn(mockData);

        // Perform the GET request
        mockMvc.perform(get("/api/export")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categories[0].category_id").value("1"))
                .andExpect(jsonPath("$.categories[0].category_name").value("Electronics"))
                .andExpect(jsonPath("$.products[0].product_name").value("Product A"));
    }

    @Test
    public void testExportData_WithColumnMappings() throws Exception {
        // Column mappings
        Map<String, String> columnMappings = Map.of(
                "category_id", "id",
                "category_name", "name"
        );

        // Mock data with adjusted keys
        Map<String, List<Map<String, Object>>> mockData = Map.of(
                "categories", List.of(
                        Map.of("id", "1", "name", "Electronics", "description", "Category Description")
                ),
                "products", List.of(
                        Map.of("product_name", "Product A", "category_name", "Electronics", "brand_name", "Brand A")
                )
        );

        // Mock service response
        when(exportService.exportData(columnMappings)).thenReturn(mockData);

        // Perform the GET request with query parameters
        mockMvc.perform(get("/api/export")
                        .param("category_id", "id")
                        .param("category_name", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categories[0].id").value("1"))
                .andExpect(jsonPath("$.categories[0].name").value("Electronics"));
    }

    @Test
    public void testExportData_InternalServerError() throws Exception {
        // Mock service to throw an exception
        when(exportService.exportData(Collections.emptyMap())).thenThrow(new RuntimeException("Mock Exception"));

        // Perform the GET request
        mockMvc.perform(get("/api/export")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error[0].message").value("Internal Server Error"));
    }
}

