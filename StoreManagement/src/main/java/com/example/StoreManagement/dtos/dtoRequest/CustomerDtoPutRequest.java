package com.example.StoreManagement.dtos.dtoRequest;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record CustomerDtoPutRequest (
        @NotBlank(message = "Name must not be empty")
        String name,

        @NotBlank(message = "CPF must not be empty")
        @CPF(message = "Invalid CPF")
        String cpf,

        @NotBlank(message = "Phone number must not be empty")
        String phoneNumber,

        @NotBlank(message = "Email must not be empty")
        String email
) {}
