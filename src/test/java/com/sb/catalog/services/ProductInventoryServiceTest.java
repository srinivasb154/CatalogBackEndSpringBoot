package com.sb.catalog.services;

import com.sb.catalog.models.ProductInventory;
import com.sb.catalog.repositories.ProductInventoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductInventoryServiceTest {

    @Mock
    private ProductInventoryRepository productInventoryRepository;

    @InjectMocks
    private ProductInventoryService productInventoryService;

    private AutoCloseable closeable;

    private ProductInventory sampleInventory;
    private UUID sampleId;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        sampleId = UUID.randomUUID();
        sampleInventory = ProductInventory.builder()
                .productId(sampleId)
                .bin("A1")
                .location("Warehouse 1")
                .source("Supplier A")
                .onHand(100)
                .onHold(10)
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetInventoriesByProductId_Found() {
        List<ProductInventory> inventoryList = List.of(sampleInventory);
        when(productInventoryRepository.findByProductId(sampleId)).thenReturn(inventoryList);

        List<ProductInventory> result = productInventoryService.getInventoriesByProductId(sampleId);

        assertEquals(1, result.size());
        assertEquals(sampleInventory, result.getFirst());
        verify(productInventoryRepository, times(1)).findByProductId(sampleId);
    }

    @Test
    public void testGetInventoriesByProductId_NotFound() {
        when(productInventoryRepository.findByProductId(sampleId)).thenReturn(List.of());

        List<ProductInventory> result = productInventoryService.getInventoriesByProductId(sampleId);

        assertTrue(result.isEmpty());
        verify(productInventoryRepository, times(1)).findByProductId(sampleId);
    }

    @Test
    public void testGetAllProductInventories() {
        List<ProductInventory> inventoryList = List.of(sampleInventory);
        when(productInventoryRepository.findAll()).thenReturn(inventoryList);

        List<ProductInventory> result = productInventoryService.getAllProductInventories();

        assertEquals(1, result.size());
        assertEquals(sampleInventory, result.getFirst());
        verify(productInventoryRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductInventoryById_Found() {
        when(productInventoryRepository.findById(sampleId)).thenReturn(Optional.of(sampleInventory));

        Optional<ProductInventory> result = productInventoryService.getProductInventoryById(sampleId);

        assertTrue(result.isPresent());
        assertEquals(sampleInventory, result.get());
        verify(productInventoryRepository, times(1)).findById(sampleId);
    }

    @Test
    public void testGetProductInventoryById_NotFound() {
        when(productInventoryRepository.findById(sampleId)).thenReturn(Optional.empty());

        Optional<ProductInventory> result = productInventoryService.getProductInventoryById(sampleId);

        assertFalse(result.isPresent());
        verify(productInventoryRepository, times(1)).findById(sampleId);
    }

    @Test
    public void testSaveProductInventory() {
        when(productInventoryRepository.save(sampleInventory)).thenReturn(sampleInventory);

        ProductInventory result = productInventoryService.saveProductInventory(sampleInventory);

        assertEquals(sampleInventory, result);
        verify(productInventoryRepository, times(1)).save(sampleInventory);
    }

    @Test
    public void testUpdateProductInventory_Success() {
        ProductInventory updatedInventory = ProductInventory.builder()
                .productId(sampleId)
                .bin("B1")
                .location("Warehouse 2")
                .source("Supplier B")
                .onHand(200)
                .onHold(20)
                .build();

        when(productInventoryRepository.findById(sampleId)).thenReturn(Optional.of(sampleInventory));
        when(productInventoryRepository.save(any(ProductInventory.class))).thenReturn(updatedInventory);

        ProductInventory result = productInventoryService.updateProductInventory(sampleId, updatedInventory);

        assertEquals(updatedInventory.getBin(), result.getBin());
        assertEquals(updatedInventory.getLocation(), result.getLocation());
        assertEquals(updatedInventory.getSource(), result.getSource());
        assertEquals(updatedInventory.getOnHand(), result.getOnHand());
        assertEquals(updatedInventory.getOnHold(), result.getOnHold());
        verify(productInventoryRepository, times(1)).findById(sampleId);
        verify(productInventoryRepository, times(1)).save(any(ProductInventory.class));
    }

    @Test
    public void testUpdateProductInventory_NotFound() {
        when(productInventoryRepository.findById(sampleId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productInventoryService.updateProductInventory(sampleId, sampleInventory));

        assertEquals("ProductInventory not found with id: " + sampleId, exception.getMessage());
        verify(productInventoryRepository, times(1)).findById(sampleId);
        verify(productInventoryRepository, never()).save(any(ProductInventory.class));
    }

    @Test
    public void testDeleteProductInventory_Success() {
        when(productInventoryRepository.existsById(sampleId)).thenReturn(true);
        doNothing().when(productInventoryRepository).deleteById(sampleId);

        assertDoesNotThrow(() -> productInventoryService.deleteProductInventory(sampleId));
        verify(productInventoryRepository, times(1)).existsById(sampleId);
        verify(productInventoryRepository, times(1)).deleteById(sampleId);
    }

    @Test
    public void testDeleteProductInventory_NotFound() {
        when(productInventoryRepository.existsById(sampleId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productInventoryService.deleteProductInventory(sampleId));

        assertEquals("ProductInventory not found with id: " + sampleId, exception.getMessage());
        verify(productInventoryRepository, times(1)).existsById(sampleId);
        verify(productInventoryRepository, never()).deleteById(sampleId);
    }
}

