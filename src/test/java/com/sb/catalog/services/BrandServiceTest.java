package com.sb.catalog.services;

import com.sb.catalog.models.Brand;
import com.sb.catalog.repositories.BrandRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetAllBrands() {
        Brand brand = new Brand(UUID.randomUUID(), "Nike", "Sportswear brand", "assets.png", null, null);
        when(brandRepository.findAll()).thenReturn(Collections.singletonList(brand));

        List<Brand> brands = brandService.getAllBrands();

        assertNotNull(brands);
        assertEquals(1, brands.size());
        assertEquals("Nike", brands.get(0).getBrandName());
        verify(brandRepository, times(1)).findAll();
    }

    @Test
    public void testCreateBrand() {
        Brand brand = new Brand(UUID.randomUUID(), "Adidas", "Sportswear brand", "assets.png", null, null);
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        Brand createdBrand = brandService.createBrand(brand);

        assertNotNull(createdBrand);
        assertEquals("Adidas", createdBrand.getBrandName());
        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    public void testGetBrandById() {
        UUID id = UUID.randomUUID();
        Brand brand = new Brand(id, "Puma", "Sportswear brand", "assets.png", null, null);
        when(brandRepository.findById(eq(id))).thenReturn(Optional.of(brand));

        Optional<Brand> foundBrand = brandService.getBrandById(id);

        assertTrue(foundBrand.isPresent());
        assertEquals("Puma", foundBrand.get().getBrandName());
        verify(brandRepository, times(1)).findById(eq(id));
    }

    @Test
    public void testUpdateBrand() {
        UUID id = UUID.randomUUID();
        Brand existingBrand = new Brand(id, "Under Armour", "Sportswear", "assets.png", null, null);
        Brand updatedBrand = new Brand(id, "Under Armour Updated", "Updated description", "updated_assets.png", null, null);

        when(brandRepository.findById(eq(id))).thenReturn(Optional.of(existingBrand));
        when(brandRepository.save(any(Brand.class))).thenReturn(updatedBrand);

        Brand result = brandService.updateBrand(id, updatedBrand);

        assertNotNull(result);
        assertEquals("Under Armour Updated", result.getBrandName());
        assertEquals("Updated description", result.getDescription());
        verify(brandRepository, times(1)).findById(eq(id));
        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    public void testDeleteBrand() {
        UUID id = UUID.randomUUID();
        doNothing().when(brandRepository).deleteById(eq(id));

        brandService.deleteBrand(id);

        verify(brandRepository, times(1)).deleteById(eq(id));
    }
}
