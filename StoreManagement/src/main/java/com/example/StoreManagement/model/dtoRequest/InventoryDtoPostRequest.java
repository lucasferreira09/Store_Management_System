package com.example.StoreManagement.model.dtoRequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record InventoryDtoPostRequest(

        @NotNull(message = "StoreID must not be empty")
        Long storeID,
        @NotNull(message = "StoreID must not be empty")
        Long productID,

        Integer quantity
) {}
