package com.prodnretail.inventory_gen.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prodnretail.inventory_gen.dto.InventoryServiceDTO;
import com.prodnretail.inventory_gen.dto.ProductDTO;
import com.prodnretail.inventory_gen.service.InventoryService;


@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService=inventoryService;
    }

    @GetMapping("/low-stock")
    public List<ProductDTO> getLowStockProducts(){
        return inventoryService.getLowStockProducts();
    }

    @GetMapping("/out-of-stock")
    public List<ProductDTO> outOfStockProducts(){
        return inventoryService.getOutofStockProducts();
    }

    @GetMapping("/summary")
    public InventoryServiceDTO getInventorySummary(){
        return inventoryService.toDto();
    }

    @PostMapping("/{id}/restock")
    public ResponseEntity<String> restock(@PathVariable UUID id,@RequestParam int quantity){
        inventoryService.restockProduct(id, quantity);
        return ResponseEntity.ok("Product restocked");
    }

    @PostMapping("{id}/sell")
     public ResponseEntity<String> sell(@PathVariable UUID id,@RequestParam int quantity){
        inventoryService.sellProduct(id, quantity);
        return ResponseEntity.ok("Product sold");
    }
}
