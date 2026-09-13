package com.example.StoreManagement.model.dtoRequest;

import com.example.StoreManagement.model.entity.enums.PaymentMethodType;
import com.example.StoreManagement.model.entity.enums.PaymentProvider;

import java.util.UUID;

public record PaymentCreationRequest(
        UUID checkoutId,
        PaymentProvider paymentProvider,
        PaymentMethodType paymentMethodType
) {}
