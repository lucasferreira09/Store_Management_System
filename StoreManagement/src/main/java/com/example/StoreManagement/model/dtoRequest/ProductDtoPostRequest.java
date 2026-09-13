package com.example.StoreManagement.model.dtoRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductDtoPostRequest(
        @NotBlank(message = "Product name must not be empty")
        String name,

        String description,

        @NotBlank(message = "Barcode must not be empty")
        String barcode,

        String photo,

        @NotNull(message = "Sale price product must not be empty")
        @PositiveOrZero(message = "Sale price must be positive")
        BigDecimal salePrice,

        @NotNull(message = "Cost price product must not be empty")
        @PositiveOrZero(message = "Cost price must be positive")
        BigDecimal costPrice,

        @NotNull(message = "CategoryID must not be empty")
        Long categoryID
) {}
