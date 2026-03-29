package com.example.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestockDTO {
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Stock quantity is required")
    @Min(value = 1, message = "Restock quantity must be at least 1")
    private Integer stock;
}
