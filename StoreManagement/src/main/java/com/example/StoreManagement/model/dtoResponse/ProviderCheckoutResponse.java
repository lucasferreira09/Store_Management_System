package com.example.StoreManagement.model.dtoResponse;

import com.example.StoreManagement.model.entity.enums.PaymentMethodType;
import com.example.StoreManagement.model.entity.enums.PaymentProvider;

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
