package com.prodnretail.inventory_gen.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;

import com.prodnretail.inventory_gen.model.enums.StockStatus;

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
    @NotBlank(message = "Product name is required")
    private String prodName;

    @Size(max=255,message = "Description is too long")
    private String prodDescription;

    @Column(nullable = false, name = "price")
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double costPrice;

    @Min(0)
    @NotNull
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="supplier_id")
    private Supplier supplier;

    @Min(0)
    private Integer reorderLevel;

    @Min(0)
    private Integer reorderQuantity;

    @Positive
    private Double sellingPrice;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;
}
