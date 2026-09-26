package com.example.StoreManagement.dtos.dtoResponse;

public record StoreDtoResponse(
        Long id,
        String name,
        String phoneNumber,
        Long addressId
) {}
