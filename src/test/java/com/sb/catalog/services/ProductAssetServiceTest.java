package com.sb.catalog.services;

import com.sb.catalog.models.Product;
import com.sb.catalog.models.ProductAsset;
import com.sb.catalog.repositories.ProductAssetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductAssetServiceTest {

    @Mock
    private ProductAssetRepository productAssetRepository;

    @InjectMocks
    private ProductAssetService productAssetService;

    private UUID productId;
    private Integer assetId;
    private ProductAsset productAsset;
    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        productId = UUID.randomUUID();
        assetId = 1;
        Product product = new Product();
        product.setProductId(productId);

        productAsset = new ProductAsset();
        productAsset.setProductAssetId(assetId);
        productAsset.setProduct(product);
        productAsset.setFileName("test.jpg");
        productAsset.setType("IMAGE");
        productAsset.setExtension("jpg");
        productAsset.setBinaryData(new byte[]{1, 2, 3});
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testGetAssetsByProductId() {
        when(productAssetRepository.findByProduct_ProductId(productId))
                .thenReturn(Collections.singletonList(productAsset));

        List<ProductAsset> assets = productAssetService.getAssetsByProductId(productId);

        assertNotNull(assets);
        assertEquals(1, assets.size());
        assertEquals(assetId, assets.getFirst().getProductAssetId());
        assertEquals("IMAGE", assets.getFirst().getType());
        verify(productAssetRepository, times(1)).findByProduct_ProductId(productId);
    }

    @Test
    void testGetAssetById() {
        when(productAssetRepository.findById(assetId))
                .thenReturn(Optional.of(productAsset));

        Optional<ProductAsset> result = productAssetService.getAssetById(assetId);

        assertTrue(result.isPresent());
        assertEquals(assetId, result.get().getProductAssetId());
        assertEquals("IMAGE", result.get().getType());
        verify(productAssetRepository, times(1)).findById(assetId);
    }

    @Test
    void testCreateAsset() {
        when(productAssetRepository.save(productAsset))
                .thenReturn(productAsset);

        ProductAsset createdAsset = productAssetService.createAsset(productAsset);

        assertNotNull(createdAsset);
        assertEquals(assetId, createdAsset.getProductAssetId());
        assertEquals("IMAGE", createdAsset.getType());
        verify(productAssetRepository, times(1)).save(productAsset);
    }

    @Test
    void testUpdateAsset() {
        when(productAssetRepository.findByProduct_ProductIdAndProductAssetId(productId, assetId))
                .thenReturn(Optional.of(productAsset));

        ProductAsset updatedAsset = new ProductAsset();
        updatedAsset.setFileName("updated.jpg");
        updatedAsset.setType("VIDEO");
        updatedAsset.setExtension("mp4");
        updatedAsset.setBinaryData(new byte[]{4, 5, 6});

        when(productAssetRepository.save(any(ProductAsset.class)))
                .thenReturn(updatedAsset);

        ProductAsset result = productAssetService.updateAsset(productId, assetId, updatedAsset);

        assertNotNull(result);
        assertEquals("updated.jpg", result.getFileName());
        assertEquals("VIDEO", result.getType());
        assertEquals("mp4", result.getExtension());
        verify(productAssetRepository, times(1)).findByProduct_ProductIdAndProductAssetId(productId, assetId);
        verify(productAssetRepository, times(1)).save(any(ProductAsset.class));
    }

    @Test
    void testDeleteAsset() {
        when(productAssetRepository.findByProduct_ProductIdAndProductAssetId(productId, assetId))
                .thenReturn(Optional.of(productAsset));

        productAssetService.deleteAsset(productId, assetId);

        verify(productAssetRepository, times(1)).findByProduct_ProductIdAndProductAssetId(productId, assetId);
        verify(productAssetRepository, times(1)).delete(productAsset);
    }

    @Test
    void testDeleteAssetsByProductId() {
        when(productAssetRepository.findByProduct_ProductId(productId))
                .thenReturn(Collections.singletonList(productAsset));

        productAssetService.deleteAssetsByProductId(productId);

        verify(productAssetRepository, times(1)).findByProduct_ProductId(productId);
        verify(productAssetRepository, times(1)).deleteAll(anyList());
    }
}


