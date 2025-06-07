package com.prodnretail.inventory_gen.repository;

import com.prodnretail.inventory_gen.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product,UUID>{
    
}
