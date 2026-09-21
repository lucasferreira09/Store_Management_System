package com.example.StoreManagement.dtos.dtoRequest;

import com.example.StoreManagement.enums.PaymentMethodType;
import com.example.StoreManagement.enums.PaymentProvider;

import java.math.BigDecimal;
import java.util.UUID;

public record CheckoutCreationRequest(
        UUID checkoutId,
        BigDecimal amount,
        String currency,
        String description,
        PaymentProvider paymentProvider,
        PaymentMethodType paymentMethodType
) {}
