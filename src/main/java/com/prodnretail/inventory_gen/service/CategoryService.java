package com.prodnretail.inventory_gen.service;

import com.prodnretail.inventory_gen.model.Category;
import com.prodnretail.inventory_gen.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(UUID id, Category updatedCategory) {
        return categoryRepository.findById(id).map(category -> {
            category.setCategoryName(updatedCategory.getCategoryName());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }
}