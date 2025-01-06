package com.sb.catalog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.catalog.dto.ProductInventoryDTO;
import com.sb.catalog.dto.ProductPricingDTO;
import com.sb.catalog.dto.ProductRequest;
import com.sb.catalog.dto.ProductReviewDTO;
import com.sb.catalog.mappers.ProductMapper;
import com.sb.catalog.models.Product;
import com.sb.catalog.models.ProductInventory;
import com.sb.catalog.models.ProductPricing;
import com.sb.catalog.models.ProductReview;
import com.sb.catalog.services.ProductInventoryService;
import com.sb.catalog.services.ProductPricingService;
import com.sb.catalog.services.ProductReviewService;
import com.sb.catalog.services.ProductService;
import com.sb.catalog.util.ProductSearchCriteria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private ProductReviewService productReviewService;

    @Mock
    private ProductInventoryService productInventoryService;

    @Mock
    private ProductPricingService productPricingService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductController productController;

    private AutoCloseable closeable;
    private UUID sampleProductId;
    private Product sampleProduct;
    private ProductRequest sampleProductRequest;
    private ProductReview sampleReview;
    private ProductReviewDTO sampleReviewDTO;
    private ProductInventory sampleInventory;
    private ProductInventoryDTO sampleInventoryDTO;
    private ProductPricing samplePricing;
    private ProductPricingDTO samplePricingDTO;


    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        sampleProductId = UUID.randomUUID();
        sampleProduct = new Product();
        sampleProduct.setProductId(sampleProductId);
        sampleProduct.setProductName("Sample Product");

        sampleProductRequest = new ProductRequest();
        sampleProductRequest.setProductId(sampleProductId);
        sampleProductRequest.setProductName("Sample Product");

        sampleReview = ProductReview.builder()
                .productId(sampleProductId)
                .userName("testUser")
                .commentId(1)
                .comment("Great Product")
                .rating(5)
                .build();

        sampleReviewDTO = new ProductReviewDTO(
                sampleProductId,
                "testUser",
                null,
                "Great Product",
                5,
                null
        );

        sampleInventory = new ProductInventory();
        sampleInventory.setProductId(sampleProductId);
        sampleInventory.setBin("A1");
        sampleInventory.setLocation("Warehouse 1");
        sampleInventory.setSource("Supplier X");
        sampleInventory.setOnHand(100);
        sampleInventory.setOnHold(10);

        sampleInventoryDTO = new ProductInventoryDTO();
        sampleInventoryDTO.setProductId(sampleProductId);
        sampleInventoryDTO.setBin("A1");
        sampleInventoryDTO.setLocation("Warehouse 1");
        sampleInventoryDTO.setSource("Supplier X");
        sampleInventoryDTO.setOnHand(100);
        sampleInventoryDTO.setOnHold(10);

        samplePricing = ProductPricing.builder()
                .productId(sampleProductId)
                .msrp(new BigDecimal("100.0000"))
                .map(new BigDecimal("90.0000"))
                .cost(new BigDecimal("80.0000"))
                .sell(new BigDecimal("95.0000"))
                .base(new BigDecimal("85.0000"))
                .startDate(LocalDate.now())
                .endDate(null)
                .createdBy("Admin")
                .build();

        samplePricingDTO = ProductPricingDTO.builder()
                .productId(sampleProductId)
                .msrp(new BigDecimal("100.0000"))
                .map(new BigDecimal("90.0000"))
                .cost(new BigDecimal("80.0000"))
                .sell(new BigDecimal("95.0000"))
                .base(new BigDecimal("85.0000"))
                .startDate(LocalDate.now())
                .endDate(null)
                .createdBy("Admin")
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(sampleProduct));
        when(productMapper.toDto(sampleProduct)).thenReturn(sampleProductRequest);

        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].productName").value("Sample Product"));

        verify(productService, times(1)).getAllProducts();
        verify(productMapper, times(1)).toDto(sampleProduct);
    }

    @Test
    public void testGetProductById() throws Exception {
        when(productService.getProductById(sampleProductId)).thenReturn(Optional.of(sampleProduct));
        when(productMapper.toDto(sampleProduct)).thenReturn(sampleProductRequest);

        mockMvc.perform(get("/api/products/" + sampleProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Sample Product"));

        verify(productService, times(1)).getProductById(sampleProductId);
        verify(productMapper, times(1)).toDto(sampleProduct);
    }

    @Test
    public void testCreateProduct() throws Exception {
        when(productMapper.toEntity(any(ProductRequest.class))).thenReturn(sampleProduct);
        when(productService.createProduct(any(Product.class))).thenReturn(sampleProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(sampleProductRequest);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"productName\": \"Sample Product\"," +
                                "\"sku\": \"SKU123\"," +
                                "\"shortDescription\": \"Short description\"," +
                                "\"longDescription\": \"Long description\"," +
                                "\"shippingNotes\": \"Shipping notes\"," +
                                "\"warrantyInfo\": \"Warranty info\"," +
                                "\"visibleToFrontEnd\": true," +
                                "\"featuredProduct\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Sample Product"));

        verify(productMapper, times(1)).toEntity(any(ProductRequest.class));
        verify(productService, times(1)).createProduct(any(Product.class));
        verify(productMapper, times(1)).toDto(any(Product.class));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(sampleProductId);

        mockMvc.perform(delete("/api/products/" + sampleProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(sampleProductId);
    }

    @Test
    public void testSearchProducts() throws Exception {
        ProductSearchCriteria searchCriteria = new ProductSearchCriteria();
        searchCriteria.setProductName("Sample");

        when(productService.searchProducts(any(ProductSearchCriteria.class))).thenReturn(Collections.singletonList(sampleProduct));

        mockMvc.perform(post("/api/products/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(searchCriteria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)));

        verify(productService, times(1)).searchProducts(any(ProductSearchCriteria.class));
    }

    @Test
    public void testGetProductReviews() throws Exception {
        when(productReviewService.getReviewsByProductId(sampleProductId)).thenReturn(List.of(sampleReview));
        when(productMapper.toDto(sampleReview)).thenReturn(sampleReviewDTO);

        mockMvc.perform(get("/api/products/" + sampleProductId + "/reviews")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user").value("testUser"))
                .andExpect(jsonPath("$[0].comment").value("Great Product"));

        verify(productReviewService, times(1)).getReviewsByProductId(sampleProductId);
        verify(productMapper, times(1)).toDto(sampleReview);
    }

    //@Test
    public void testSaveProductReviews() throws Exception {
        List<ProductReviewDTO> reviewsDTO = List.of(sampleReviewDTO);
        String payload = new ObjectMapper().writeValueAsString(reviewsDTO);

        when(productMapper.toEntity(any(ProductReviewDTO.class))).thenReturn(sampleReview);
        when(productReviewService.saveReview(any(ProductReview.class))).thenReturn(sampleReview);
        when(productMapper.toDto(any(ProductReview.class))).thenReturn(sampleReviewDTO);

        mockMvc.perform(post("/api/products/" + sampleProductId + "/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].user").value("testUser"))
                .andExpect(jsonPath("$[0].comment").value("Great Product"));

        verify(productMapper, times(1)).toEntity(any(ProductReviewDTO.class));
        verify(productReviewService, times(1)).saveReview(any(ProductReview.class));
        verify(productMapper, times(1)).toDto(any(ProductReview.class));
    }

    @Test
    public void testGetInventoryByProductId() throws Exception {
        List<ProductInventory> inventories = List.of(sampleInventory);

        when(productInventoryService.getInventoriesByProductId(sampleProductId)).thenReturn(inventories);
        when(productMapper.toDto(sampleInventory)).thenReturn(sampleInventoryDTO);

        mockMvc.perform(get("/api/products/" + sampleProductId + "/inventory")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bin").value("A1"))
                .andExpect(jsonPath("$[0].location").value("Warehouse 1"));

        verify(productInventoryService, times(1)).getInventoriesByProductId(sampleProductId);
        verify(productMapper, times(inventories.size())).toDto(any(ProductInventory.class));
    }

    @Test
    public void testCreateOrUpdateInventory() throws Exception {
        when(productMapper.toEntity(any(ProductInventoryDTO.class))).thenReturn(sampleInventory);
        when(productInventoryService.saveProductInventory(any(ProductInventory.class))).thenReturn(sampleInventory);
        when(productMapper.toDto(any(ProductInventory.class))).thenReturn(sampleInventoryDTO);

        mockMvc.perform(post("/api/products/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sampleInventoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Inventory saved successfully."))
                .andExpect(jsonPath("$.inventory.bin").value("A1"))
                .andExpect(jsonPath("$.inventory.location").value("Warehouse 1"));

        verify(productMapper, times(1)).toEntity(any(ProductInventoryDTO.class));
        verify(productInventoryService, times(1)).saveProductInventory(any(ProductInventory.class));
        verify(productMapper, times(1)).toDto(any(ProductInventory.class));
    }

    @Test
    public void testUpdateInventory() throws Exception {
        when(productMapper.toEntity(any(ProductInventoryDTO.class))).thenReturn(sampleInventory);
        when(productInventoryService.updateProductInventory(eq(sampleProductId), any(ProductInventory.class))).thenReturn(sampleInventory);
        when(productMapper.toDto(any(ProductInventory.class))).thenReturn(sampleInventoryDTO);

        mockMvc.perform(patch("/api/products/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sampleInventoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Inventory updated successfully."))
                .andExpect(jsonPath("$.inventory.bin").value("A1"))
                .andExpect(jsonPath("$.inventory.location").value("Warehouse 1"));

        verify(productMapper, times(1)).toEntity(any(ProductInventoryDTO.class));
        verify(productInventoryService, times(1)).updateProductInventory(eq(sampleProductId), any(ProductInventory.class));
        verify(productMapper, times(1)).toDto(any(ProductInventory.class));
    }

    public void testGetPricingByProductId() throws Exception {
        when(productPricingService.getProductPricingById(sampleProductId)).thenReturn(Optional.of(samplePricing));
        when(productMapper.toDto(samplePricing)).thenReturn(samplePricingDTO);

        mockMvc.perform(get("/api/pricing/" + sampleProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(sampleProductId.toString()))
                .andExpect(jsonPath("$.msrp").value("100.0000"));

        verify(productPricingService, times(1)).getProductPricingById(sampleProductId);
        verify(productMapper, times(1)).toDto(samplePricing);
    }

    public void testCreateOrUpdatePricing() throws Exception {
        when(productMapper.toEntity(any(ProductPricingDTO.class))).thenReturn(samplePricing);
        when(productPricingService.saveProductPricing(any(ProductPricing.class))).thenReturn(samplePricing);
        when(productMapper.toDto(any(ProductPricing.class))).thenReturn(samplePricingDTO);

        mockMvc.perform(post("/api/pricing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(samplePricingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Pricing saved successfully."))
                .andExpect(jsonPath("$.pricing.productId").value(sampleProductId.toString()))
                .andExpect(jsonPath("$.pricing.msrp").value("100.0000"));

        verify(productMapper, times(1)).toEntity(any(ProductPricingDTO.class));
        verify(productPricingService, times(1)).saveProductPricing(any(ProductPricing.class));
        verify(productMapper, times(1)).toDto(any(ProductPricing.class));
    }

    public void testUpdatePricing() throws Exception {
        when(productMapper.toEntity(any(ProductPricingDTO.class))).thenReturn(samplePricing);
        when(productPricingService.updateProductPricing(eq(sampleProductId), any(ProductPricing.class))).thenReturn(samplePricing);
        when(productMapper.toDto(any(ProductPricing.class))).thenReturn(samplePricingDTO);

        mockMvc.perform(patch("/api/pricing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(samplePricingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Pricing updated successfully."))
                .andExpect(jsonPath("$.pricing.productId").value(sampleProductId.toString()))
                .andExpect(jsonPath("$.pricing.msrp").value("100.0000"));

        verify(productMapper, times(1)).toEntity(any(ProductPricingDTO.class));
        verify(productPricingService, times(1)).updateProductPricing(eq(sampleProductId), any(ProductPricing.class));
        verify(productMapper, times(1)).toDto(any(ProductPricing.class));
    }
}

