package com.prodnretail.inventory_gen.repository;

import com.prodnretail.inventory_gen.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier,UUID>{
    
}
