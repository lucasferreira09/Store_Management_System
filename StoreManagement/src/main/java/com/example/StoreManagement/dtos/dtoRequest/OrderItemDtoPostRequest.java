package com.example.StoreManagement.dtos.dtoRequest;


public record OrderItemDtoPostRequest(
        Long productId,
        Long storeId,
        Integer quantity
) {}
