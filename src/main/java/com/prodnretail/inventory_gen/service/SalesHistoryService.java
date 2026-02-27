package com.prodnretail.inventory_gen.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.prodnretail.inventory_gen.dto.ProductDTO;
import com.prodnretail.inventory_gen.dto.SalesDataDTO;
import com.prodnretail.inventory_gen.model.Product;
import com.prodnretail.inventory_gen.model.SalesHistory;
import com.prodnretail.inventory_gen.repository.SalesHistoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SalesHistoryService {

    private SalesHistoryRepository salesHistoryRepository;

    public List<SalesDataDTO> getSalesByProduct(UUID productId){
        return salesHistoryRepository.findByProductId(productId)
        .stream()
        .map(s-> new SalesDataDTO(
                    productId,
                    s.getProduct().getProdName(),
                    s.getQuantitySold(),
                    s.getTimestamp()
            )).toList();
    }

    public List<SalesDataDTO> getAllSales(){
        return salesHistoryRepository.findAll().stream()
        .map(s-> new SalesDataDTO(
                    s.getId(),
                    s.getProduct().getProdName(),
                    s.getQuantitySold(),
                    s.getTimestamp()
            )).toList();    
    }


    
}
