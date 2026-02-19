package com.prodnretail.inventory_gen.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.prodnretail.inventory_gen.dto.ProductDTO;
import com.prodnretail.inventory_gen.model.Category;
import com.prodnretail.inventory_gen.model.Product;
import com.prodnretail.inventory_gen.model.Supplier;
import com.prodnretail.inventory_gen.repository.CategoryRepository;
import com.prodnretail.inventory_gen.repository.ProductRepository;
import com.prodnretail.inventory_gen.repository.SupplierRepository;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final SupplierRepository supplierRepo;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepo,
                          CategoryRepository categoryRepo,
                          SupplierRepository supplierRepo,
                          ModelMapper modelMapper) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.supplierRepo = supplierRepo;
        this.modelMapper = modelMapper;
    }

    // Save product directly
    public Product saveProduct(Product product) {
        return productRepo.save(product);
    }

    // Get all
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    // Get by ID
    public Optional<Product> getProductById(UUID id) {
        return productRepo.findById(id);
    }

    // Delete
    public void deleteProductById(UUID id) {
        productRepo.deleteById(id);
    }

    // Create from DTO
    public Product createProductFromDto(ProductDTO dto) {
        Product product = modelMapper.map(dto, Product.class);

        Category category = categoryRepo.findById(dto.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found"));
        Supplier supplier = supplierRepo.findById(dto.getSupplierId())
            .orElseThrow(() -> new RuntimeException("Supplier not found"));

        product.setCategory(category);
        product.setSupplier(supplier);

        return productRepo.save(product);
    }

    // Update from DTO
    public Product updateProductFromDto(UUID id, ProductDTO dto) {
        return productRepo.findById(id).map(existing -> {
            modelMapper.map(dto, existing);  // Map new fields into existing object

            Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
            Supplier supplier = supplierRepo.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

            existing.setCategory(category);
            existing.setSupplier(supplier);

            return productRepo.save(existing);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

public Product toEntity(ProductDTO dto) {
    Product product = new Product();
    product.setProdName(dto.getProdName());
    product.setProdDescription(dto.getProdDescription());
    product.setCostPrice(dto.getPrice());
    product.setQuantity(dto.getQuantity());

    Category category = categoryRepo.findById(dto.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));
    Supplier supplier = supplierRepo.findById(dto.getSupplierId())
        .orElseThrow(() -> new RuntimeException("Supplier not found"));

    product.setCategory(category);
    product.setSupplier(supplier);
    return product;
}

public ProductDTO toDto(Product product) {
    return ProductDTO.builder()
        .prodName(product.getProdName())
        .prodDescription(product.getProdDescription())
        .price(product.getCostPrice())
        .quantity(product.getQuantity())
        .categoryId(product.getCategory().getId())
        .supplierId(product.getSupplier().getId())
        .build();
}
}