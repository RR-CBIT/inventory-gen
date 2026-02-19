package com.prodnretail.inventory_gen.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prodnretail.inventory_gen.dto.InventoryServiceDTO;
import com.prodnretail.inventory_gen.exception.ResourceNotFoundException;
import com.prodnretail.inventory_gen.exception.StockUnavailableException;
import com.prodnretail.inventory_gen.model.InventoryLog;
import com.prodnretail.inventory_gen.model.Product;
import com.prodnretail.inventory_gen.repository.InventoryLogRepository;
import com.prodnretail.inventory_gen.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final ProductRepository productRepository;
    private final InventoryLogRepository inventoryLogRepository;

    public List<Product> getLowStockProducts(){
        return productRepository.findAll().stream()
        .filter(p-> p.getReorderLevel()!=null
        && p.getQuantity() <= p.getReorderLevel()).toList();
    }

    public List<Product> getOutofStockProducts(){
        return productRepository.findAll().stream()
        .filter(p-> p.getQuantity()==0)
        .toList();
    }

    public InventoryServiceDTO tDto(){
        List<Product> products = productRepository.findAll();

        long totalProducts = products.size();

        long lowStockCount= getLowStockProducts().size();

        long outOfStockCount= getOutofStockProducts().size();

        double totalInventoryValue= products.stream().mapToDouble(p-> p.getCostPrice() * p.getQuantity()).sum();

        return new InventoryServiceDTO(totalProducts, lowStockCount, outOfStockCount, totalInventoryValue);
    }

    @Transactional
    public void restockProduct(UUID productId, int quantity){
        Product product = productRepository.findById(productId)
        .orElseThrow(()-> new ResourceNotFoundException("product not found"));

        product.setQuantity(product.getQuantity()+quantity);
        productRepository.save(product);

        InventoryLog log = new InventoryLog();
        log.setProductId(productId);
        log.setRestockType("RESTOCKED");
        log.setQuantityChanged(quantity);
        log.setTimestamp(LocalDateTime.now());

        inventoryLogRepository.save(log);
    }

    @Transactional
    public void sellProduct(UUID productId, int quantity){
        Product product= productRepository.findById(productId)
        .orElseThrow(()-> new ResourceNotFoundException("product not found"));

        if(quantity<=0) throw new IllegalArgumentException("Quantity must be greater than zero.");
        if(product.getQuantity() < quantity) throw new StockUnavailableException("STOCK UNAVAILABLE");

            product.setQuantity(product.getQuantity()-quantity);
            productRepository.save(product);

            InventoryLog log = new InventoryLog();
            log.setProductId(productId);
            log.setRestockType("SALE");
            log.setQuantityChanged(quantity);
            log.setTimestamp(LocalDateTime.now());
            inventoryLogRepository.save(log);
    }
}
