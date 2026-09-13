package com.example.StoreManagement.model.dtoResponse;

public record ProductDtoResponse(
        Long id,
        String name,
        String description,
        String barcode,
        String photo,
        String salePrice,
        String costPrice,
        Long categoryID
) {}
