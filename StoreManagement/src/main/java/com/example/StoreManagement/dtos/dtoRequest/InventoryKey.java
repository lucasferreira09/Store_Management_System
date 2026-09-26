package com.example.StoreManagement.dtos.dtoRequest;

import jakarta.validation.constraints.NotNull;

public record InventoryKey(

        @NotNull(message = "StoreId must not be empty")
        Long storeId,
        @NotNull(message = "productId must not be empty")
        Long productId
) {}
