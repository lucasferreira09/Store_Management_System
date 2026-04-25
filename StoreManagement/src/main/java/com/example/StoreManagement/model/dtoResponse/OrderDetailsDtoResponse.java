package com.example.StoreManagement.model.dtoResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDetailsDtoResponse(
        Long id,
        String status,
        LocalDateTime date,
        BigDecimal totalAmount,
        String street,
        String number,
        String city,
        String state,
        String zip,
        Long customerId,
        Long storeId
) {}
