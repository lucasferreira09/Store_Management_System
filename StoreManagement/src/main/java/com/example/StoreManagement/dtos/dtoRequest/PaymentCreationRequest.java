package com.example.StoreManagement.dtos.dtoRequest;

import com.example.StoreManagement.enums.PaymentMethodType;
import com.example.StoreManagement.enums.PaymentProvider;

import java.util.UUID;

public record PaymentCreationRequest(
        UUID checkoutId,
        PaymentProvider paymentProvider,
        PaymentMethodType paymentMethodType
) {}
