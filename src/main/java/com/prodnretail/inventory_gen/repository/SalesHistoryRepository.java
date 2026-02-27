package com.prodnretail.inventory_gen.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prodnretail.inventory_gen.model.SalesHistory;

public interface SalesHistoryRepository extends JpaRepository<SalesHistory, UUID>{
    List <SalesHistory> findByProductId(UUID productId);
    
}
