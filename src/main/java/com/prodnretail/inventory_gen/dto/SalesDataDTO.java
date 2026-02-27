package com.prodnretail.inventory_gen.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.prodnretail.inventory_gen.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesDataDTO {
    private UUID id;
    private String productName;
    private int quantitySold;
    private LocalDateTime timestamp;
}
