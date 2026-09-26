package com.example.StoreManagement.dtos.dtoRequest;

import jakarta.validation.constraints.NotNull;

public record CustomerAddressDtoPostRequest(
        @NotNull(message = "CustomerId must not be empty")
        Long customerId,
        @NotNull(message = "AddressId must not be empty")
        Long addressId
) {}
