package com.example.StoreManagement.model.dtoResponse;

public record CustomerDtoDetailResponse(
        Long id,
        String name,
        String cpf,
        String email
) {}
