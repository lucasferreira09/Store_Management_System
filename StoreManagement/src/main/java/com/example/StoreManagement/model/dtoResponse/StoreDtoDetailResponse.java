package com.example.StoreManagement.model.dtoResponse;

public record StoreDtoDetailResponse(
        Long id,
        String name,
        String cnpj,
        String phoneNumber,
        String email,
        Long addressID
) {}
