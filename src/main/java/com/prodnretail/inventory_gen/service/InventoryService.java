package com.prodnretail.inventory_gen.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prodnretail.inventory_gen.dto.InventoryServiceDTO;
import com.prodnretail.inventory_gen.dto.ProductDTO;
import com.prodnretail.inventory_gen.exception.ResourceNotFoundException;
import com.prodnretail.inventory_gen.exception.StockUnavailableException;
import com.prodnretail.inventory_gen.model.InventoryLog;
import com.prodnretail.inventory_gen.model.Product;
import com.prodnretail.inventory_gen.model.SalesHistory;
import com.prodnretail.inventory_gen.model.enums.InventoryAction;
import com.prodnretail.inventory_gen.model.enums.StockStatus;
import com.prodnretail.inventory_gen.repository.InventoryLogRepository;
import com.prodnretail.inventory_gen.repository.ProductRepository;
import com.prodnretail.inventory_gen.repository.SalesHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final ProductRepository productRepository;
    private final InventoryLogRepository inventoryLogRepository;
    private final SalesHistoryRepository salesHistoryRepository;
    private final ProductService productService;

    public List<ProductDTO> getLowStockProducts(){
        return productRepository.findAll().stream()
        .filter(p-> p.getReorderLevel()!=null
        && p.getQuantity() <= p.getReorderLevel()).map(productService::toDto).toList();
    }

    public List<ProductDTO> getOutofStockProducts(){
    return productRepository.findAll().stream()
            .filter(p -> p.getQuantity() == 0)
            .map(productService::toDto)
            .toList();
}

    public InventoryServiceDTO toDto(){
        List<Product> products = productRepository.findAll();

        long totalProducts = products.size();
        long lowStockCount = products.stream()
            .filter(p -> p.getStockStatus() == StockStatus.LOW_STOCK)
            .count();

        long outOfStockCount = products.stream()
            .filter(p -> p.getStockStatus() == StockStatus.OUT_OF_STOCK)
            .count();

        double totalInventoryValue = products.stream()
            .mapToDouble(p -> p.getCostPrice() * p.getQuantity())
            .sum();


        return new InventoryServiceDTO(totalProducts, lowStockCount, outOfStockCount, totalInventoryValue);
    }

    @Transactional
    public void restockProduct(UUID productId, int quantity){
        Product product = productRepository.findById(productId)
        .orElseThrow(()-> new ResourceNotFoundException("product not found"));

        product.setQuantity(product.getQuantity()+quantity);
        product.setStockStatus(productService.calculateStockStatus(product));
        productRepository.save(product);

        InventoryLog log = new InventoryLog();
        log.setProductId(productId);
        log.setRestockType(InventoryAction.RESTOCK);
        log.setQuantityChanged(quantity);
        log.setTimestamp(LocalDateTime.now());

        inventoryLogRepository.save(log);

        SalesHistory history = new SalesHistory();
            history.setProduct(product);
            history.setQuantitySold(quantity);
            history.setTimestamp(LocalDateTime.now());
            System.out.println("Before saving sales history");
            salesHistoryRepository.save(history);
            System.out.println("After saving sales history");
    }

    @Transactional
    public void sellProduct(UUID productId, int quantity){
        Product product= productRepository.findById(productId)
        .orElseThrow(()-> new ResourceNotFoundException("product not found"));

        if(quantity<=0) throw new IllegalArgumentException("Quantity must be greater than zero.");
        if(product.getQuantity() < quantity) throw new StockUnavailableException("STOCK UNAVAILABLE");

            product.setQuantity(product.getQuantity()-quantity);
            product.setStockStatus(productService.calculateStockStatus(product));
            productRepository.save(product);

            InventoryLog log = new InventoryLog();
            log.setProductId(productId);
            log.setRestockType(InventoryAction.SALE);
            log.setQuantityChanged(-quantity);
            log.setTimestamp(LocalDateTime.now());
            System.out.println("Before inventory save");
            inventoryLogRepository.save(log);
            System.out.println("After inventory save");



            SalesHistory history = new SalesHistory();
            history.setProduct(product);
            history.setQuantitySold(-quantity);
            history.setTimestamp(LocalDateTime.now());
            System.out.println("Before saving sales history");
            salesHistoryRepository.save(history);
            System.out.println("After saving sales history");
    }
}
