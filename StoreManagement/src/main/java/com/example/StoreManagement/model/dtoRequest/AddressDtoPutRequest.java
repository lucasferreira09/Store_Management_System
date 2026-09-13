package com.example.StoreManagement.model.dtoRequest;

import jakarta.validation.constraints.NotBlank;

public record AddressDtoPutRequest(
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

        @NotBlank(message = "Postal code must not be empty")
        String postalCode
) {}
