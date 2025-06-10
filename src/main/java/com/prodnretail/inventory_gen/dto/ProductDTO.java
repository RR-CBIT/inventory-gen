package com.prodnretail.inventory_gen.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    @NotBlank(message = "Product name is required")
    private String prodName;

    @Size(max=255,message = "Desccription is too long")
    private String prodDescription;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;
    private Integer quantity;

    @NotNull(message = "Category id is required")
    private UUID categoryId;

    @NotNull(message = "Supplier id is required")
    private UUID supplierId;

}
