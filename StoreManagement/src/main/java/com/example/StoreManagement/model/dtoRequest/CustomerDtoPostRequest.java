package com.example.StoreManagement.model.dtoRequest;

import jakarta.validation.constraints.NotBlank;

public record CustomerDtoPostRequest(

        @NotBlank(message = "Name must not be empty")
        String name,
        @NotBlank(message = "CPF must not be empty")
        String cpf,
        String phone_number,
        @NotBlank(message = "Email must not be empty")
        String email
) {
}
