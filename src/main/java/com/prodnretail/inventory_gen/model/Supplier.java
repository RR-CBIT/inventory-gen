package com.prodnretail.inventory_gen.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable= false, unique= true)
    private String supplierName;
    private String email;
    private String phone;
    @OneToMany(mappedBy = "supplier")
    @Builder.Default
    private List<Product> products = new ArrayList<>(); 
}
