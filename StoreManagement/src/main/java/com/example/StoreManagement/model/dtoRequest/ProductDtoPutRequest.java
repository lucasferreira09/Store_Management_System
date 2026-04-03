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

        @NotNull(message = "Sale Price product must not be empty")
        @PositiveOrZero(message = "Cost Price must be positive")
        BigDecimal salePrice,

        @NotNull(message = "Cost Price product must not be empty")
        @PositiveOrZero(message = "Cost Price must be positive")
        BigDecimal costPrice
) {
}
