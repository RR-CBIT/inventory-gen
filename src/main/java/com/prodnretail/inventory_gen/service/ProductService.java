package com.prodnretail.inventory_gen.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.prodnretail.inventory_gen.dto.ProductDTO;
import com.prodnretail.inventory_gen.exception.ResourceNotFoundException;
import com.prodnretail.inventory_gen.model.Category;
import com.prodnretail.inventory_gen.model.Product;
import com.prodnretail.inventory_gen.model.Supplier;
import com.prodnretail.inventory_gen.model.enums.StockStatus;
import com.prodnretail.inventory_gen.repository.CategoryRepository;
import com.prodnretail.inventory_gen.repository.ProductRepository;
import com.prodnretail.inventory_gen.repository.SupplierRepository;

import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final SupplierRepository supplierRepo;
    private final ModelMapper modelMapper; 

    public List<ProductDTO> getAll() {
        return productRepo.findAll().stream().map(this::toDto).toList();
    }

    public ProductDTO getById(UUID id) {
        Product product= productRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found"));
        return toDto(product);
    }

    public void delete(UUID id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id)
                );
        productRepo.delete(product);
    }

    public ProductDTO create(ProductDTO dto) {
        Product product = modelMapper.map(dto,Product.class);

        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        Supplier supplier = supplierRepo.findById(dto.getSupplierId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found")
                );

        product.setCategory(category);
        product.setSupplier(supplier);
        product.setStockStatus(calculateStockStatus(product));

        Product saved= productRepo.save(product);

        return toDto(saved);
    }

    public ProductDTO update(UUID id, ProductDTO dto) {
        Product existing = productRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found"));

        modelMapper.map(dto,existing);
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        Supplier supplier = supplierRepo.findById(dto.getSupplierId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Supplier not found")
                );

        existing.setCategory(category);
        existing.setSupplier(supplier);
        existing.setStockStatus(calculateStockStatus(existing));

        Product updated=productRepo.save(existing);

        return toDto(updated);
    }


public ProductDTO toDto(Product product) {
    return ProductDTO.builder()
    .id(product.getId())
        .prodName(product.getProdName())
        .prodDescription(product.getProdDescription())
        .price(product.getCostPrice())
        .quantity(product.getQuantity())
        .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
        .supplierId(product.getSupplier() != null ? product.getSupplier().getId() : null)
        .reorderLevel(product.getReorderLevel())
        .reorderQuantity(product.getReorderQuantity())
        .sellingPrice(product.getSellingPrice())
        .stockStatus(product.getStockStatus())
        .build();
}

public StockStatus calculateStockStatus(Product product) {
    if (product.getQuantity() == 0) {
        return StockStatus.OUT_OF_STOCK;
    }
    
    Integer reorderLevel = product.getReorderLevel();
    if (reorderLevel != null && product.getQuantity() <= reorderLevel) {
        return StockStatus.LOW_STOCK;
    }
    
    return StockStatus.IN_STOCK;
}
}