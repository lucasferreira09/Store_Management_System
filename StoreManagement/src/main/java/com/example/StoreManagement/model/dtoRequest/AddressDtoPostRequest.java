package com.example.StoreManagement.model.dtoRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressDtoPostRequest(
        @NotBlank(message = "Street must not be empty")
        String street,

        @NotBlank(message = "Neighbourhood must not be empty")
        String neighbourhood,

        String complement,
        String number,

        @NotBlank(message = "City must not be empty")
        String city,

        @NotBlank(message = "State must not be empty")
        String state,

        @NotBlank(message = "ZIP must not be empty")
        String zip,

        @NotNull(message = "CustomerID must not be empty")
        Long customerID
) {}
