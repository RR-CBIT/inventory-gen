package com.prodnretail.inventory_gen.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

    private UUID id;

    @NotBlank(message = "Category name cannot be blank")
    private String categoryName;
}
