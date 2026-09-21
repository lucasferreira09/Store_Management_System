package com.example.StoreManagement.dtos.dtoResponse;

import com.example.StoreManagement.enums.PaymentMethodType;
import com.example.StoreManagement.enums.PaymentProvider;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProviderCheckoutResponse(
        UUID checkoutId,
        BigDecimal amount,
        String currency,
        PaymentMethodType paymentMethodType,
        PaymentProvider paymentProvider,
        String providerSessionId,
        Instant createdAt,
        Instant expiresAt,
        String checkoutUrl
) {}
