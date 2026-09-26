package com.example.StoreManagement.dtos.dtoRequest;

import jakarta.validation.constraints.NotNull;

public record InventoryDtoPostRequest(

        @NotNull(message = "StoreId must not be empty")
        Long storeId,
        @NotNull(message = "StoreId must not be empty")
        Long productId,

        Integer quantity
) {}
