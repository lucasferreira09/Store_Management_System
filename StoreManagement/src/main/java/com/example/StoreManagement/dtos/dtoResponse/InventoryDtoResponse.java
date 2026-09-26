package com.example.StoreManagement.dtos.dtoResponse;

public record InventoryDtoResponse(
        Long id,
        Long storeId,
        Long productId,
        Integer quantity
) {}
