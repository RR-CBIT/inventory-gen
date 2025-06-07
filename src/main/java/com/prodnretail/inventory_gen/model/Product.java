package com.prodnretail.inventory_gen.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable= false)
    private String prodName;

    private String prodDescription;

    @Column(nullable = false)
    private Double price;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="supplier_id")
    private Supplier supplier;

}
