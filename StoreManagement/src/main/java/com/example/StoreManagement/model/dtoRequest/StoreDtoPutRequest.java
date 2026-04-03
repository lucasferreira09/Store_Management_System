package com.example.StoreManagement.model.dtoRequest;

import jakarta.validation.constraints.NotBlank;

public record StoreDtoPutRequest(
        @NotBlank(message = "Store name must not be empty")
        String name,
        @NotBlank(message = "CNPJ name must not be empty")
        String cnpj,

        @NotBlank(message = "Email must not be empty")
        String email,

        String phoneNumber,
        Long addressID
) {}
