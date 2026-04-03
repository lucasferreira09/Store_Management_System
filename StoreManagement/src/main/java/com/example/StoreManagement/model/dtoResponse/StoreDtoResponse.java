package com.example.StoreManagement.model.dtoResponse;

public record StoreDtoResponse(
        Long id,
        String name,
        String phoneNumber,
        String email,
        Long addressID
) {}
