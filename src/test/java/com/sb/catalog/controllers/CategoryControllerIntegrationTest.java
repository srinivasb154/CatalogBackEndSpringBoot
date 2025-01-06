package com.sb.catalog.controllers;

import com.sb.catalog.models.Category;
import com.sb.catalog.services.CategoryService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetAllCategories() throws Exception {
        Category category = new Category(UUID.randomUUID(), "Electronics");
        when(categoryService.getAllCategories()).thenReturn(Collections.singletonList(category));

        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    public void testSearchCategories() throws Exception {
        when(categoryService.searchCategories(any()))
                .thenReturn(Collections.singletonList(new Category(UUID.randomUUID(), "Electronics")));

        mockMvc.perform(post("/api/categories/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Electronics\",\"description\":\"\",\"isActive\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testCreateCategory() throws Exception {
        Category category = new Category(UUID.randomUUID(), "Books");
        when(categoryService.createCategory(any())).thenReturn(category);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Books\",\"description\":\"All kinds of books\",\"isActive\":true}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateCategory() throws Exception {
        UUID id = UUID.randomUUID();
        Category category = new Category(id, "Updated Name");
        when(categoryService.getCategoryById(eq(id))).thenReturn(Optional.of(category));
        when(categoryService.updateCategory(eq(id), any())).thenReturn(category);

        mockMvc.perform(patch("/api/categories/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Name\",\"description\":\"Updated Description\",\"isActive\":true}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteCategory() throws Exception {
        UUID id = UUID.randomUUID();
        when(categoryService.getCategoryById(eq(id))).thenReturn(Optional.of(new Category()));

        mockMvc.perform(delete("/api/categories/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetCategoryById() throws Exception {
        UUID id = UUID.randomUUID();
        Category category = new Category(id, "Books");
        when(categoryService.getCategoryById(eq(id))).thenReturn(Optional.of(category));

        mockMvc.perform(get("/api/categories/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testListCategoriesByName() throws Exception {
        when(categoryService.getCategoryByCategoryName(eq("Books")))
                .thenReturn(Optional.of(new Category(UUID.randomUUID(), "Books")));

        mockMvc.perform(get("/api/categories/name/Books"))
                .andExpect(status().isOk());
    }
}


