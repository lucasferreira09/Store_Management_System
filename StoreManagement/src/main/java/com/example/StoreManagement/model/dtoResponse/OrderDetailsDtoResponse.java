package com.example.StoreManagement.model.dtoResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDetailsDtoResponse(
        Long id,
        UUID checkoutId,
        String status,
        Instant created_at,
        BigDecimal totalAmount,
        String street,
        String number,
        String city,
        String state,
        String postalCode,
        Long customerId,
        Long storeId,
        List<OrderItemDtoResponse> orderItems
) {}
