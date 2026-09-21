package com.example.StoreManagement.dtos.dtoResponse;

public record StoreDtoDetailResponse(
        Long id,
        String name,
        String cnpj,
        String phoneNumber,
        String email,
        Long addressId
) {}
