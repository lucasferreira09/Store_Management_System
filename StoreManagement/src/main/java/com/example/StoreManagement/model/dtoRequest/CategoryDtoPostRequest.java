package com.example.StoreManagement.model.dtoRequest;

import jakarta.validation.constraints.NotBlank;

public record CategoryDtoPostRequest(
        @NotBlank(message = "Category name must not be empty")
        String name
) {}
