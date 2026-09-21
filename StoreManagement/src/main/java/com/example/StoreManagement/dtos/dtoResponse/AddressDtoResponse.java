package com.example.StoreManagement.dtos.dtoResponse;

public record AddressDtoResponse(
        Long id,
        String street,
        String neighbourhood,
        String complement,
        String number,
        String city,
        String state,
        String postalCode
) {}
