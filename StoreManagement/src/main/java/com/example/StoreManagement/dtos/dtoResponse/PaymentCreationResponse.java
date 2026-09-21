package com.example.StoreManagement.dtos.dtoResponse;

import com.example.StoreManagement.enums.PaymentMethodType;
import com.example.StoreManagement.enums.PaymentProvider;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentCreationResponse(
        UUID checkoutId,
        PaymentMethodType paymentMethodType,
        PaymentProvider paymentProvider,
        String providerSessionId,
        BigDecimal amount,
        String currency,
        String checkoutUrl
) {}
