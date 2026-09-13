package com.example.StoreManagement.model.dtoRequest;

import com.example.StoreManagement.model.entity.enums.PaymentMethodType;
import com.example.StoreManagement.model.entity.enums.PaymentProvider;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentCompletedData(
        UUID checkoutId,
        PaymentProvider paymentProvider,
        PaymentMethodType paymentMethodType,
        String providerPaymentId,
        String providerSessionId,
        String providerChargeId,
        BigDecimal amount,
        String currency,
        String cardBrand,
        String last4,
        Instant createdAt,
        String paymentMessage
) {}