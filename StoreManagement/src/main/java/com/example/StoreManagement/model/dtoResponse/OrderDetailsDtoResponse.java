package com.example.StoreManagement.model.dtoResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailsDtoResponse(
        Long id,
        String status,
        LocalDateTime date,
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
