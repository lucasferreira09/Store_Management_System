package com.example.StoreManagement.dtos.dtoRequest;

import jakarta.validation.constraints.NotNull;

public record CustomerAddressPutRequest(

        @NotNull(message = "AddressId must not be empty")
        Long addressId
) {}
