package com.prodnretail.inventory_gen.repository;

import com.prodnretail.inventory_gen.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category,UUID>{
    
}