package com.prodnretail.inventory_gen.controller;

import com.prodnretail.inventory_gen.dto.ProductDTO;
import com.prodnretail.inventory_gen.service.ProductService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

@PostMapping
public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(productDTO));
}

@GetMapping
public ResponseEntity<List<ProductDTO>> getAllProducts() {
    return ResponseEntity.ok(productService.getAll());
}

@GetMapping("/{id}")
public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID id) {
    return ResponseEntity.ok(productService.getById(id));
}
    
@PutMapping("/{id}")
public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productService.update(id, dto));
}
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable UUID id){
       productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
