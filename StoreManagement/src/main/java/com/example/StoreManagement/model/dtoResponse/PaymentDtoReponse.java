package com.example.StoreManagement.model.dtoResponse;

import com.example.StoreManagement.model.entity.enums.PaymentProvider;
import com.example.StoreManagement.model.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentDtoReponse(
    Long id,
    UUID checkoutId,
    BigDecimal amount,
    String currency,
    PaymentStatus paymentStatus,
    PaymentProvider paymentProvider,
    String providerSessionId,
    Instant createdAt,
    Instant expiresAt,
    Instant paidAt
) {}
