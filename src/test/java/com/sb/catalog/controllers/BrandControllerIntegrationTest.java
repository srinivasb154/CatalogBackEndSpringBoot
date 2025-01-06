package com.sb.catalog.controllers;

import com.sb.catalog.models.Brand;
import com.sb.catalog.services.BrandService;
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
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BrandControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private BrandService brandService;

    @InjectMocks
    private BrandController brandController;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(brandController).build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetAllBrands() throws Exception {
        Brand brand = new Brand(UUID.randomUUID(), "Nike", "Sportswear brand", "assets.png", null, null);
        when(brandService.getAllBrands()).thenReturn(Collections.singletonList(brand));

        mockMvc.perform(get("/api/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].brandName").value("Nike"));
    }

    @Test
    public void testCreateBrand() throws Exception {
        Brand brand = new Brand(UUID.randomUUID(), "Adidas", "Sportswear brand", "assets.png", null, null);
        when(brandService.createBrand(any(Brand.class))).thenReturn(brand);

        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"brandName\": \"Adidas\"," +
                                "\"description\": \"Sportswear brand\"," +
                                "\"assets\": \"assets.png\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brandName").value("Adidas"));
    }

    @Test
    public void testGetBrandById() throws Exception {
        UUID id = UUID.randomUUID();
        Brand brand = new Brand(id, "Puma", "Sportswear brand", "assets.png", null, null);
        when(brandService.getBrandById(eq(id))).thenReturn(Optional.of(brand));

        mockMvc.perform(get("/api/brands/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brandName").value("Puma"));
    }

    @Test
    public void testUpdateBrand() throws Exception {
        UUID id = UUID.randomUUID();
        Brand brand = new Brand(id, "Under Armour", "Sportswear", "assets.png", null, null);
        when(brandService.updateBrand(eq(id), any(Brand.class))).thenReturn(brand);

        mockMvc.perform(patch("/api/brands/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"brandName\": \"Under Armour\"," +
                                "\"description\": \"Sportswear\"," +
                                "\"assets\": \"assets.png\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brandName").value("Under Armour"));
    }

    @Test
    public void testDeleteBrand() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/api/brands/" + id))
                .andExpect(status().isNoContent());
    }
}
