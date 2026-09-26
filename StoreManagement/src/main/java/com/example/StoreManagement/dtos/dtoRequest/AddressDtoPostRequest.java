package com.example.StoreManagement.dtos.dtoRequest;

import jakarta.validation.constraints.NotBlank;

public record AddressDtoPostRequest(
        @NotBlank(message = "Address line 1 must not be empty")
        String addressLine1,

        String addressLine2,

        @NotBlank(message = "Neighbourhood must not be empty")
        String neighbourhood,

        String number,

        @NotBlank(message = "City must not be empty")
        String city,

        @NotBlank(message = "State must not be empty")
        String state,

        @NotBlank(message = "Postal code must not be empty")
        String postalCode
) {}
