package com.example.StoreManagement.dtos.dtoResponse;

public record AddressDtoResponse(
        Long id,
        String addressLine1,
        String addressLine2,
        String neighbourhood,
        String number,
        String city,
        String state,
        String postalCode
) {}
