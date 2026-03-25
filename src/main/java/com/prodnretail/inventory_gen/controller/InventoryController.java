package com.prodnretail.inventory_gen.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.prodnretail.inventory_gen.dto.InventoryServiceDTO;
import com.prodnretail.inventory_gen.dto.ProductDTO;
import com.prodnretail.inventory_gen.service.InventoryService;

import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/inventory")
@Validated
public class InventoryController {
    
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDTO>> getLowStockProducts() {
        return ResponseEntity.ok(inventoryService.getLowStockProducts());
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<ProductDTO>> outOfStockProducts() {
        return ResponseEntity.ok(inventoryService.getOutofStockProducts());
    }

    @GetMapping("/summary")
    public ResponseEntity<InventoryServiceDTO> getInventorySummary() {
        return ResponseEntity.ok(inventoryService.toDto());
    }

    @PostMapping("/{id}/restock")
    public ResponseEntity<String> restock(
            @PathVariable UUID id,
            @RequestParam @Min(1) int quantity) {

        inventoryService.restockProduct(id, quantity);
        return ResponseEntity.ok("Product restocked");
    }

    @PostMapping("/{id}/sell")
    public ResponseEntity<String> sell(
            @PathVariable UUID id,
            @RequestParam @Min(1) int quantity) {

        inventoryService.sellProduct(id, quantity);
        return ResponseEntity.ok("Product sold");
    }
}