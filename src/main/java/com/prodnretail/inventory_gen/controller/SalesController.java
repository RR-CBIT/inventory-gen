package com.prodnretail.inventory_gen.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prodnretail.inventory_gen.dto.SalesDataDTO;
import com.prodnretail.inventory_gen.model.SalesHistory;

import com.prodnretail.inventory_gen.service.SalesHistoryService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/sales")
@AllArgsConstructor
public class SalesController {

    private final SalesHistoryService salesHistoryService;

    @GetMapping
    public List<SalesDataDTO> getAllSales(){
        return salesHistoryService.getAllSales();
    }

    @GetMapping("/product/{productId}")
    public List<SalesDataDTO> getSalesByProduct(@PathVariable UUID productId){
        return salesHistoryService.getSalesByProduct(productId);
    }

}
