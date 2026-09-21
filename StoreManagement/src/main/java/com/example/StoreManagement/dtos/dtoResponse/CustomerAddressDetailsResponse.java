package com.example.StoreManagement.dtos.dtoResponse;

public record CustomerAddressDetailsResponse(
        String name,
        AddressDtoResponse address
) {}
