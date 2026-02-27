package com.prodnretail.inventory_gen.service;

import com.prodnretail.inventory_gen.dto.CategoryDTO;
import com.prodnretail.inventory_gen.exception.ResourceNotFoundException;
import com.prodnretail.inventory_gen.model.Category;
import com.prodnretail.inventory_gen.repository.CategoryRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    
    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll()
        .stream()
        .map(category -> modelMapper.map(category,CategoryDTO.class)).toList();
    }

    public CategoryDTO create(CategoryDTO dto) {
        Category category = modelMapper.map(dto, Category.class);
        Category saved = categoryRepository.save(category);
        return modelMapper.map(saved,CategoryDTO.class);

    }
    public CategoryDTO getById(UUID id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id)
                );
        return modelMapper.map(category, CategoryDTO.class);
    }

    public CategoryDTO update(UUID id, CategoryDTO dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id)
                );
            modelMapper.map(dto,existing);
            Category updated =categoryRepository.save(existing);

            return modelMapper.map(updated, CategoryDTO.class);
    }

    public void delete(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

}