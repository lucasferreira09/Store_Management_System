package com.example.StoreManagement.model.dtoRequest;

public record CheckoutItemRequest(
        Long productId,
        Integer quantity
) {}
