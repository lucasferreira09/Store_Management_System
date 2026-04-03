package com.example.StoreManagement.model.dtoResponse;

public record StoreDtoDetailResponse(
        Long id,
        String name,
        String cnpj,
        String phone_number,
        String email,
        Long addressID
) {}
