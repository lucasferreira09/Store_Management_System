package com.example.StoreManagement.model.dtoRequest;


public record OrderItemDtoPostRequest(
        Long productId,
        Integer quantity
) {}
