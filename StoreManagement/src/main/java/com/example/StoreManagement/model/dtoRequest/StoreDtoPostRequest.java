package com.example.StoreManagement.model.dtoRequest;

import jakarta.validation.constraints.NotBlank;

public record StoreDtoPostRequest(
        @NotBlank(message = "Store name must not be empty")
        String name,
        @NotBlank(message = "CNPJ name must not be empty")
        String cnpj,

        String phone_number,

        @NotBlank(message = "Email must not be empty")
        String email,

        Long addressID
) {}
