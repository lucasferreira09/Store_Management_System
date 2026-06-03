package com.example.StoreManagement.model.dtoRequest;

import java.util.List;

public record OrderDtoPostRequest(
        Long customerId,
        String street,
        String number,
        String city,
        String state,
        String zip,
        List<OrderItemDtoPostRequest> orderItems
) {}
