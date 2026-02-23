package com.example.StoreManagement.model.dtoResponse;

public record CustomerAddressDetailsResponse(
        String name,
        AddressDtoResponse address
) {}
