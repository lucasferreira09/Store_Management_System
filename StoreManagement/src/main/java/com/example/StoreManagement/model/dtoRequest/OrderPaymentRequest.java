package com.example.StoreManagement.model.dtoRequest;

import com.example.StoreManagement.model.entity.enums.PaymentProvider;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderPaymentRequest(
        List<String> orderIds,
        BigDecimal amount,
        String currency,
        String description
) {}