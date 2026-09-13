package com.example.StoreManagement.model.dtoResponse;

import com.example.StoreManagement.model.entity.enums.PaymentMethodType;
import com.example.StoreManagement.model.entity.enums.PaymentProvider;

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
