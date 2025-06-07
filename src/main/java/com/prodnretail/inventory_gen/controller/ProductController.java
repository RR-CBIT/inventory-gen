package com.prodnretail.inventory_gen.controller;

import com.prodnretail.inventory_gen.model.Product;
import com.prodnretail.inventory_gen.service.ProductService;

import jakarta.validation.Valid;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService=productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product){
        Product savedProduct = productService.saveProduct(product);
        return new ResponseEntity<>(savedProduct,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id){
       return productService.getProductById(id).map(product -> new ResponseEntity<>(product, HttpStatus.OK))
       .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @Valid @RequestBody Product product) {
        return productService.getProductById(id)
       .map(existingProduct -> {
        product.setId(existingProduct.getId());
        Product updatedProduct = productService.saveProduct(product); 
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);})
       .orElseGet(() -> ResponseEntity.notFound().build());
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
