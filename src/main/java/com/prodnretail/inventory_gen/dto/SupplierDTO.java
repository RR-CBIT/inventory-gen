package com.prodnretail.inventory_gen.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
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
public class SupplierDTO {
    private UUID id;
    @NotBlank(message = "Supplier name is required")
    private String supplierName;
    @NotBlank(message="invalid email")
    private String email;
    private String phone;

}