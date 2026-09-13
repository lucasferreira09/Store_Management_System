package com.example.StoreManagement.model.dtoResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDtoResponse(
        Long id,
        String status,
        Instant created_at,
        BigDecimal totalAmount
) {}
