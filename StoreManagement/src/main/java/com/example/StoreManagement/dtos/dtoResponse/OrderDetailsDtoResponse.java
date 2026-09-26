package com.example.StoreManagement.dtos.dtoResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderDetailsDtoResponse(
        Long id,
        UUID checkoutId,
        String status,
        Instant created_at,
        BigDecimal totalAmount,
        String addressLine1,
        String number,
        String city,
        String state,
        String postalCode,
        Long customerId,
        Long storeId,
        List<OrderItemDtoResponse> orderItems
) {}
