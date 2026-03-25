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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final ProductRepository productRepository;
    private final InventoryLogRepository inventoryLogRepository;
    private final SalesHistoryRepository salesHistoryRepository;
    private final ProductService productService;

    public List<ProductDTO> getLowStockProducts() {
        return productRepository.findAll().stream()
            .filter(p -> p.getReorderLevel() != null &&
                         p.getQuantity() <= p.getReorderLevel())
            .map(productService::toDto)
            .toList();
    }

    public List<ProductDTO> getOutofStockProducts() {
        return productRepository.findAll().stream()
            .filter(p -> p.getQuantity() == 0)
            .map(productService::toDto)
            .toList();
    }

    public InventoryServiceDTO toDto() {
        List<Product> products = productRepository.findAll();

        long totalProducts = products.size();

        long lowStockCount = products.stream()
            .filter(p -> p.getStockStatus() == StockStatus.LOW_STOCK)
            .count();

        long outOfStockCount = products.stream()
            .filter(p -> p.getStockStatus() == StockStatus.OUT_OF_STOCK)
            .count();

        double totalInventoryValue = products.stream()
            .mapToDouble(p ->
                (p.getCostPrice() == null ? 0.0 : p.getCostPrice()) * p.getQuantity()
            )
            .sum();

        return new InventoryServiceDTO(
            totalProducts,
            lowStockCount,
            outOfStockCount,
            totalInventoryValue
        );
    }

    @Transactional
    public void restockProduct(UUID productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setQuantity(product.getQuantity() + quantity);
        product.setStockStatus(productService.calculateStockStatus(product));
        productRepository.save(product);

        InventoryLog logEntry = new InventoryLog();
        logEntry.setProductId(productId);
        logEntry.setRestockType(InventoryAction.RESTOCK);
        logEntry.setQuantityChanged(quantity);
        logEntry.setTimestamp(LocalDateTime.now());

        inventoryLogRepository.save(logEntry);

        log.debug("Restocked product {} with quantity {}", productId, quantity);
    }

    @Transactional
    public void sellProduct(UUID productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new StockUnavailableException("Stock unavailable");
        }

        product.setQuantity(product.getQuantity() - quantity);
        product.setStockStatus(productService.calculateStockStatus(product));
        productRepository.save(product);

        InventoryLog logEntry = new InventoryLog();
        logEntry.setProductId(productId);
        logEntry.setRestockType(InventoryAction.SALE);
        logEntry.setQuantityChanged(-quantity);
        logEntry.setTimestamp(LocalDateTime.now());

        inventoryLogRepository.save(logEntry);

        SalesHistory history = new SalesHistory();
        history.setProduct(product);
        history.setQuantitySold(quantity);
        history.setTimestamp(LocalDateTime.now());

        salesHistoryRepository.save(history);

        log.debug("Sold product {} with quantity {}", productId, quantity);
    }
}