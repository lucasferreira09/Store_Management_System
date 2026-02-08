package com.example.StoreManagement.model.dtoResponse;

public record ProductDtoResponse(
        String name,
        String description,
        String barcode,
        String photo,
        String salePrice,
        String costPrice,
        CategoryDtoResponse category
) {}
