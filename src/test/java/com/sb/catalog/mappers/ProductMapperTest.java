package com.sb.catalog.mappers;

import com.sb.catalog.dto.ProductAssetsDTO;
import com.sb.catalog.models.Product;
import com.sb.catalog.models.ProductAsset;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProductMapperTest {

    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Test
    public void testToEntityProductAssetsDTO() {
        UUID productId = UUID.randomUUID();
        ProductAssetsDTO dto = new ProductAssetsDTO(1, productId, "test.jpg", "IMAGE", "jpg", new byte[]{1, 2, 3});

        ProductAsset entity = productMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getProductAssetId(), entity.getProductAssetId());
        assertEquals(dto.getFileName(), entity.getFileName());
        assertEquals(dto.getType(), entity.getType());
        assertEquals(dto.getExtension(), entity.getExtension());
        assertArrayEquals(dto.getBinaryData(), entity.getBinaryData());
        assertNotNull(entity.getProduct());
        assertEquals(dto.getProductId(), entity.getProduct().getProductId());
    }

    @Test
    public void testToDtoProductAsset() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setProductId(productId);

        ProductAsset entity = new ProductAsset(1, product, "test.jpg", "IMAGE", "jpg", new byte[]{1, 2, 3}, null, null);

        ProductAssetsDTO dto = productMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getProductAssetId(), dto.getProductAssetId());
        assertEquals(entity.getFileName(), dto.getFileName());
        assertEquals(entity.getType(), dto.getType());
        assertEquals(entity.getExtension(), dto.getExtension());
        assertArrayEquals(entity.getBinaryData(), dto.getBinaryData());
        assertEquals(entity.getProduct().getProductId(), dto.getProductId());
    }

    /*@Test
    public void testToEntityProductSpecificationsDTO() {
        ProductSpecificationsDTO dto = new ProductSpecificationsDTO();
        dto.setCapacity("500GB");

        ProductSpecifications entity = productMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getCapacity(), entity.getCapacity());
    }

    @Test
    public void testToDtoProductSpecifications() {
        ProductSpecifications entity = new ProductSpecifications();
        entity.setCapacity("500GB");

        ProductSpecificationsDTO dto = productMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getCapacity(), dto.getCapacity());
    }*/
}

