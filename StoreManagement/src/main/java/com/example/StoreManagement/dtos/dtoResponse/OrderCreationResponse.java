package com.example.StoreManagement.dtos.dtoResponse;

import com.example.StoreManagement.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreationResponse(
        Long customerId,
        UUID checkoutId,
        BigDecimal amount,
        OrderStatus status
) {}

