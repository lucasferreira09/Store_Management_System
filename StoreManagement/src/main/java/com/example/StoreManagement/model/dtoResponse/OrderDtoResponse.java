package com.example.StoreManagement.model.dtoResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDtoResponse(
        Long id,
        String status,
        LocalDateTime date,
        BigDecimal totalAmount
) {}
