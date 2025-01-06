package com.sb.catalog.services;

import com.sb.catalog.models.Category;
import com.sb.catalog.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void testCreateCategory() {
        Category category = new Category();
        category.setCategoryName("Test Category");

        when(categoryRepository.save(category)).thenReturn(category);

        Category createdCategory = categoryService.createCategory(category);

        assertNotNull(createdCategory);
        assertEquals("Test Category", createdCategory.getCategoryName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testGetCategoryById() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        category.setCategoryId(id);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        Optional<Category> foundCategory = categoryService.getCategoryById(id);

        assertTrue(foundCategory.isPresent());
        assertEquals(id, foundCategory.get().getCategoryId());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void testGetAllCategories() {
        Category category1 = new Category();
        Category category2 = new Category();

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<Category> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryByCategoryName() {
        String categoryName = "Test Category";
        Category category = new Category();
        category.setCategoryName(categoryName);

        when(categoryRepository.findByCategoryName(categoryName)).thenReturn(category);

        Optional<Category> foundCategory = categoryService.getCategoryByCategoryName(categoryName);

        assertTrue(foundCategory.isPresent());
        assertEquals(categoryName, foundCategory.get().getCategoryName());
        verify(categoryRepository, times(1)).findByCategoryName(categoryName);
    }

    @Test
    void testUpdateCategory() {
        UUID id = UUID.randomUUID();
        Category existingCategory = new Category();
        existingCategory.setCategoryId(id);
        existingCategory.setCategoryName("Old Name");

        Category updatedCategory = new Category();
        updatedCategory.setCategoryName("New Name");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);

        Category result = categoryService.updateCategory(id, updatedCategory);

        assertNotNull(result);
        assertEquals("New Name", result.getCategoryName());
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).save(existingCategory);
    }

    @Test
    void testDeleteCategory() {
        UUID id = UUID.randomUUID();

        when(categoryRepository.existsById(id)).thenReturn(true);

        categoryService.deleteCategory(id);

        verify(categoryRepository, times(1)).existsById(id);
        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteCategoryThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();

        when(categoryRepository.existsById(id)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(id));

        assertEquals("Category not found with ID: " + id, exception.getMessage());
        verify(categoryRepository, times(1)).existsById(id);
        verify(categoryRepository, never()).deleteById(id);
    }
}

