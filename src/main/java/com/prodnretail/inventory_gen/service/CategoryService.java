package com.prodnretail.inventory_gen.service;

import com.prodnretail.inventory_gen.dto.CategoryDTO;
import com.prodnretail.inventory_gen.model.Category;
import com.prodnretail.inventory_gen.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper=modelMapper;
    }

    public Category saveCategory(Category category){
        return categoryRepository.save(category);
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    public Category createCategoryFromDto(CategoryDTO dto) {
        Category category = modelMapper.map(dto, Category.class);

        return categoryRepository.save(category);

    }

    public Category updateCategoryFromDto(UUID id, CategoryDTO dto) {
        return categoryRepository.findById(id).map(existing -> {
            modelMapper.map(dto,existing);

            return categoryRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }

    public Category toEntity(CategoryDTO dto){
        Category category= new Category();
        category.setId(dto.getId());
        category.setCategoryName(dto.getCategoryName());
        return category;
    }

    public CategoryDTO toDto(Category category){
        return CategoryDTO.builder()
        .id(category.getId())
        .categoryName(category.getCategoryName())
        .build();
    }
}