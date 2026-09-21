package com.example.StoreManagement.dtos.dtoRequest;

import jakarta.validation.constraints.NotNull;

public record InventoryDtoPostRequest(

        @NotNull(message = "StoreID must not be empty")
        Long storeID,
        @NotNull(message = "StoreID must not be empty")
        Long productID,

        Integer quantity
) {}
