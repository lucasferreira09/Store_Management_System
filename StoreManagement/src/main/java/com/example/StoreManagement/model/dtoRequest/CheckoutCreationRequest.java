package com.example.StoreManagement.model.dtoRequest;

import com.example.StoreManagement.model.entity.enums.PaymentMethodType;
import com.example.StoreManagement.model.entity.enums.PaymentProvider;

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
