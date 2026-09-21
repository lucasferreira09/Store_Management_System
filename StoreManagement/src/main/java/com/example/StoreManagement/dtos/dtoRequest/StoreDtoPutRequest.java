package com.example.StoreManagement.dtos.dtoRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;

public record StoreDtoPutRequest(
        @NotBlank(message = "Store name must not be empty")
        String name,

        @NotBlank(message = "CNPJ name must not be empty")
        @CNPJ(message = "Invalid CNPJ")
        String cnpj,

        String phoneNumber,

        @NotBlank(message = "Email must not be empty")
        String email,

        Long addressId
) {
        public record Cnpj(
                @NotBlank(message = "CNPJ name must not be empty")
                @CNPJ(message = "Invalid CNPJ")
                String cnpj
        ) {}

        public record Adress(
                @NotNull(message = "Address must not be empty")
                Long addressId
        ) {}
}
