package com.example.StoreManagement.dtos.dtoResponse;

public record CustomerDtoDetailResponse(
        Long id,
        String name,
        String cpf,
        String email
) {}
