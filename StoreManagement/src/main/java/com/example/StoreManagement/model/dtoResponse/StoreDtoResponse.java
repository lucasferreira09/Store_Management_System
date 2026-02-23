package com.example.StoreManagement.model.dtoResponse;

public record StoreDtoResponse(
        String name,
        String cnpj,
        String phone_number,
        String email,
        Long addressID
) {}
