package com.example.StoreManagement.dtos.dtoRequest;

import com.example.StoreManagement.enums.PaymentMethodType;
import com.example.StoreManagement.enums.PaymentProvider;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentCreation(
        UUID checkoutId,
        PaymentMethodType paymentMethodType,
        PaymentProvider paymentProvider,
        String providerSessionId,
        BigDecimal amount,
        String currency,
        Instant createdAt,
        Instant expiresAt
) {}

