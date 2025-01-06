package com.sb.catalog.controllers;

import com.sb.catalog.models.Category;
import com.sb.catalog.services.CategoryService;
import com.sb.catalog.util.SearchCriteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Search categories
    @PostMapping("/search")
    public ResponseEntity<List<Category>> searchCategories(@RequestBody SearchCriteria searchCriteria) {
        List<Category> categories = categoryService.searchCategories(searchCriteria);
        return ResponseEntity.ok(categories);
    }

    // Create a new category
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(createdCategory);
    }

    // Update an existing category
    @PatchMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable UUID id, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updatedCategory);
    }

    // Delete a category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // List categories by name
    @GetMapping("/name/{categoryName}")
    public ResponseEntity<Category> listCategoriesByName(@PathVariable String categoryName) {
        Optional<Category> category = categoryService.getCategoryByCategoryName(categoryName);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    // Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable UUID id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

