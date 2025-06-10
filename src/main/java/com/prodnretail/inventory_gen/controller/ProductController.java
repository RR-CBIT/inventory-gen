package com.prodnretail.inventory_gen.controller;

import com.prodnretail.inventory_gen.dto.ProductDTO;
import com.prodnretail.inventory_gen.model.Product;
import com.prodnretail.inventory_gen.service.ProductService;

import jakarta.validation.Valid;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService=productService;
    }

@PostMapping
public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
    Product saved = productService.saveProduct(productService.toEntity(productDTO));
    return new ResponseEntity<>(productService.toDto(saved), HttpStatus.CREATED);
}

@GetMapping
public ResponseEntity<List<ProductDTO>> getAllProducts() {
    List<ProductDTO> dtos = productService.getAllProducts().stream()
        .map(productService::toDto)
        .toList();
    return ResponseEntity.ok(dtos);
}

@GetMapping("/{id}")
public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID id) {
    return productService.getProductById(id)
        .map(product -> new ResponseEntity<>(productService.toDto(product), HttpStatus.OK))
        .orElse(ResponseEntity.notFound().build());
}
    
@PutMapping("/{id}")
public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductDTO dto) {
    try {
        Product updated = productService.updateProductFromDto(id, dto);
        return ResponseEntity.ok(productService.toDto(updated));
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable UUID id){
       return productService.getProductById(id)
       .map(product -> {
        productService.deleteProductById(id); 
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);})
       .orElseGet(()-> ResponseEntity.notFound().build());
    }
}
