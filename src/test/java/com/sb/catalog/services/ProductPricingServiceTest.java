package com.sb.catalog.services;

import com.sb.catalog.models.ProductPricing;
import com.sb.catalog.repositories.ProductPricingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductPricingServiceTest {

    @Mock
    private ProductPricingRepository productPricingRepository;

    @InjectMocks
    private ProductPricingService productPricingService;

    private AutoCloseable closeable;

    private ProductPricing samplePricing;
    private UUID sampleProductId;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        sampleProductId = UUID.randomUUID();
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
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetAllProductPricing() {
        List<ProductPricing> pricingList = List.of(samplePricing);
        when(productPricingRepository.findAll()).thenReturn(pricingList);

        List<ProductPricing> result = productPricingService.getAllProductPricing();

        assertEquals(1, result.size());
        assertEquals(samplePricing, result.getFirst());
        verify(productPricingRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductPricingById_Found() {
        when(productPricingRepository.findById(sampleProductId)).thenReturn(Optional.of(samplePricing));

        Optional<ProductPricing> result = productPricingService.getProductPricingById(sampleProductId);

        assertTrue(result.isPresent());
        assertEquals(samplePricing, result.get());
        verify(productPricingRepository, times(1)).findById(sampleProductId);
    }

    @Test
    public void testGetProductPricingById_NotFound() {
        when(productPricingRepository.findById(sampleProductId)).thenReturn(Optional.empty());

        Optional<ProductPricing> result = productPricingService.getProductPricingById(sampleProductId);

        assertFalse(result.isPresent());
        verify(productPricingRepository, times(1)).findById(sampleProductId);
    }

    @Test
    public void testSaveProductPricing() {
        when(productPricingRepository.save(samplePricing)).thenReturn(samplePricing);

        ProductPricing result = productPricingService.saveProductPricing(samplePricing);

        assertEquals(samplePricing, result);
        verify(productPricingRepository, times(1)).save(samplePricing);
    }

    @Test
    public void testUpdateProductPricing_Success() {
        ProductPricing updatedPricing = ProductPricing.builder()
                .productId(sampleProductId)
                .msrp(new BigDecimal("110.0000"))
                .map(new BigDecimal("100.0000"))
                .cost(new BigDecimal("90.0000"))
                .sell(new BigDecimal("105.0000"))
                .base(new BigDecimal("95.0000"))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .createdBy("Admin")
                .build();

        when(productPricingRepository.findById(sampleProductId)).thenReturn(Optional.of(samplePricing));
        when(productPricingRepository.save(any(ProductPricing.class))).thenReturn(updatedPricing);

        ProductPricing result = productPricingService.updateProductPricing(sampleProductId, updatedPricing);

        assertEquals(updatedPricing.getMsrp(), result.getMsrp());
        assertEquals(updatedPricing.getMap(), result.getMap());
        assertEquals(updatedPricing.getCost(), result.getCost());
        assertEquals(updatedPricing.getSell(), result.getSell());
        assertEquals(updatedPricing.getBase(), result.getBase());
        verify(productPricingRepository, times(1)).findById(sampleProductId);
        verify(productPricingRepository, times(1)).save(any(ProductPricing.class));
    }

    @Test
    public void testUpdateProductPricing_NotFound() {
        when(productPricingRepository.findById(sampleProductId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productPricingService.updateProductPricing(sampleProductId, samplePricing));

        assertEquals("ProductPricing not found with id: " + sampleProductId, exception.getMessage());
        verify(productPricingRepository, times(1)).findById(sampleProductId);
        verify(productPricingRepository, never()).save(any(ProductPricing.class));
    }

    @Test
    public void testDeleteProductPricing_Success() {
        when(productPricingRepository.existsById(sampleProductId)).thenReturn(true);
        doNothing().when(productPricingRepository).deleteById(sampleProductId);

        assertDoesNotThrow(() -> productPricingService.deleteProductPricing(sampleProductId));
        verify(productPricingRepository, times(1)).existsById(sampleProductId);
        verify(productPricingRepository, times(1)).deleteById(sampleProductId);
    }

    @Test
    public void testDeleteProductPricing_NotFound() {
        when(productPricingRepository.existsById(sampleProductId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productPricingService.deleteProductPricing(sampleProductId));

        assertEquals("ProductPricing not found with id: " + sampleProductId, exception.getMessage());
        verify(productPricingRepository, times(1)).existsById(sampleProductId);
        verify(productPricingRepository, never()).deleteById(sampleProductId);
    }
}
