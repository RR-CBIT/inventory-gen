package com.prodnretail.inventory_gen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryServiceDTO{
    private long totalProducts;
    private long lowStockCount;
    private long outOfStockCount;
    private double totalInventoryValue;
}