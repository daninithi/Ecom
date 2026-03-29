package com.example.product.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class ProductDTO {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    private Integer initialStock;

}
