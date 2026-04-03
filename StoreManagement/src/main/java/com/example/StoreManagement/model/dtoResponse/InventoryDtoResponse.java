package com.example.StoreManagement.model.dtoResponse;

public record InventoryDtoResponse(
        Long id,
        Long storeID,
        Long productID,
        Integer quantity
) {}
