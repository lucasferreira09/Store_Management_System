package com.example.StoreManagement.dtos.dtoRequest;


import jakarta.validation.constraints.NotNull;

public record OrderItemDtoPostRequest(
        @NotNull(message = "ProductId must not be empty")
        Long productId,
        @NotNull(message = "StoreId must not be empty")
        Long storeId,
        Integer quantity
) {}
