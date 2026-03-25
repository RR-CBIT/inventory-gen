package com.prodnretail.inventory_gen.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.prodnretail.inventory_gen.dto.SalesDataDTO;
import com.prodnretail.inventory_gen.repository.SalesHistoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SalesHistoryService {

    private final SalesHistoryRepository salesHistoryRepository;

    public List<SalesDataDTO> getSalesByProduct(UUID productId) {
        return salesHistoryRepository.findByProductId(productId)
            .stream()
            .map(s -> new SalesDataDTO(
                s.getProduct().getId(),
                s.getProduct().getProdName(),
                s.getQuantitySold(),
                s.getTimestamp()
            ))
            .toList();
    }

    public List<SalesDataDTO> getAllSales() {
        return salesHistoryRepository.findAll()
            .stream()
            .map(s -> new SalesDataDTO(
                s.getProduct().getId(),
                s.getProduct().getProdName(),
                s.getQuantitySold(),
                s.getTimestamp()
            ))
            .toList();
    }
}