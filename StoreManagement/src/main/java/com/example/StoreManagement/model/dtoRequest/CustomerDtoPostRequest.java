package com.example.StoreManagement.model.dtoRequest;

import jakarta.validation.constraints.NotBlank;

public record CustomerDtoPostRequest(

        @NotBlank(message = "Name must not be empty")
        String name,

        @NotBlank(message = "CPF must not be empty")
        String cpf,

        @NotBlank(message = "Phone number must not be empty")
        String phoneNumber,

        @NotBlank(message = "Email must not be empty")
        String email
) {}
