package com.prodnretail.inventory_gen.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prodnretail.inventory_gen.model.Product;
import com.prodnretail.inventory_gen.repository.ProductRepository;

public class ProductService {
    private final ProductRepository productRepo;

    public ProductService(ProductRepository productRepo){
        this.productRepo=productRepo;
    }
    public Product saveProduct(Product product){
        return productRepo.save(product);
    }
    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }
    public Optional<Product> getProductById(UUID id){
        return productRepo.findById(id);
    }

    public void deleteProductById(UUID id){
        productRepo.deleteById(id);
    }

    public Optional<Product> updateProduct(UUID id, Product updatedProduct) {
        return productRepo.findById(id).map(existing -> {
            existing.setProdName(updatedProduct.getProdName());
            existing.setProdDescription(updatedProduct.getProdDescription());
            existing.setPrice(updatedProduct.getPrice());
            existing.setCategory(updatedProduct.getCategory());
            return productRepo.save(existing);
        });
}
}
