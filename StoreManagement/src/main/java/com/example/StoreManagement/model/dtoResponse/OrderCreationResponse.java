package com.example.StoreManagement.model.dtoResponse;

import com.example.StoreManagement.model.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreationResponse(
        Long customerId,
        UUID checkoutId,
        BigDecimal amount,
        OrderStatus status
) {}

