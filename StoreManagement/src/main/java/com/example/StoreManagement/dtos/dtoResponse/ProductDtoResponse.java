package com.example.StoreManagement.dtos.dtoResponse;

public record ProductDtoResponse(
        Long id,
        String name,
        String description,
        String barcode,
        String photo,
        String salePrice,
        String costPrice,
        Long categoryId
) {}
