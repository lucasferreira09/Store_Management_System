package com.example.StoreManagement.model.dtoResponse;

public record ProductDtoResponse(
        String name,
        String description,
        String barcode,
        String photo,
        String sale_price,
        String cost_price,
        CategoryDtoResponse category
) {}
