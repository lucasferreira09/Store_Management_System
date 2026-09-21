package com.example.StoreManagement.dtos.dtoResponse;

public record InventoryDtoResponse(
        Long id,
        Long storeID,
        Long productID,
        Integer quantity
) {}
