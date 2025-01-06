package com.sb.catalog.services;

import com.sb.catalog.models.Category;
import com.sb.catalog.repositories.CategoryRepository;
import com.sb.catalog.util.SearchCriteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> searchCategories(SearchCriteria searchCriteria) {
        List<Category> allCategories = categoryRepository.findAll();

        return allCategories.stream()
                .filter(category -> {
                    boolean matches = true;
                    if (searchCriteria.getName() != null && !searchCriteria.getName().isEmpty()) {
                        matches = category.getCategoryName().contains(searchCriteria.getName());
                    }
                    if (searchCriteria.getDescription() != null && !searchCriteria.getDescription().isEmpty()) {
                        matches = matches && category.getDescription().contains(searchCriteria.getDescription());
                    }

                    return matches;
                })
                .collect(Collectors.toList());
    }

    // Create a new category entry
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Retrieve a category entry by ID
    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    // Retrieve all category entries
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Retrieve a category entry by category name
    public Optional<Category> getCategoryByCategoryName(String categoryName) {
        return Optional.ofNullable(categoryRepository.findByCategoryName(categoryName));
    }

    // Update a category entry
    public Category updateCategory(UUID id, Category updatedCategory) {
        return categoryRepository.findById(id).map(existingCategory -> {
            existingCategory.setCategoryName(updatedCategory.getCategoryName());
            existingCategory.setParentCategory(updatedCategory.getParentCategory());
            existingCategory.setDescription(updatedCategory.getDescription());
            existingCategory.setSortOrder(updatedCategory.getSortOrder());
            existingCategory.setVisible(updatedCategory.isVisible());
            existingCategory.setSmartCategory(updatedCategory.isSmartCategory());
            existingCategory.setProductMustWatch(updatedCategory.isProductMustWatch());
            return categoryRepository.save(existingCategory);
        }).orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
    }

    // Delete a category entry by ID
    public void deleteCategory(UUID id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Category not found with ID: " + id);
        }
    }
}

