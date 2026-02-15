package com.example.StoreManagement.model.dtoRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductDtoPutRequest(
        @NotBlank(message = "Product name must not be empty")
        String name,

        String description,

        @NotBlank(message = "Barcode must not be empty")
        String barcode,

        String photo,

        @NotNull(message = "SalePrice product must not be empty")
        @PositiveOrZero(message = "CostPrice must be positive")
        BigDecimal sale_price,

        @NotNull(message = "CostPrice product must not be empty")
        @PositiveOrZero(message = "CostPrice must be positive")
        BigDecimal cost_price
) {
}
