package com.example.StoreManagement.dtos.dtoRequest;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderDtoPostRequest(
        @NotNull(message = "CustomerId must not be empty")
        Long customerId,
        String addressLine1,
        String number,
        String city,
        String state,
        String postalCode,
        List<OrderItemDtoPostRequest> orderItems
) {}
