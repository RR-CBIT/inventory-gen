package com.prodnretail.inventory_gen.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prodnretail.inventory_gen.model.InventoryLog;


public interface InventoryLogRepository extends JpaRepository<InventoryLog, UUID>{
}
