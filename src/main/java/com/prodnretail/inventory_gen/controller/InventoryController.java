package com.prodnretail.inventory_gen.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prodnretail.inventory_gen.dto.InventoryServiceDTO;
import com.prodnretail.inventory_gen.model.Product;
import com.prodnretail.inventory_gen.service.InventoryService;


@RestController
@RequestMapping("/inventory")
public class InventoryController {
    
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService=inventoryService;
    }

    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts(){
        return inventoryService.getLowStockProducts();
    }

    @GetMapping("/out-of-stock")
    public List<Product> outOfStockProducts(){
        return inventoryService.getOutofStockProducts();
    }

    @GetMapping("/summary")
    public InventoryServiceDTO getInventorySummary(){
        return inventoryService.tDto();
    }
}
