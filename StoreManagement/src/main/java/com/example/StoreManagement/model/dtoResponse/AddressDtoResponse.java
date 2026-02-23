package com.example.StoreManagement.model.dtoResponse;

public record AddressDtoResponse(
        String street,
        String neighbourhood,
        String complement,
        String number,
        String city,
        String state,
        String zip
) {}
