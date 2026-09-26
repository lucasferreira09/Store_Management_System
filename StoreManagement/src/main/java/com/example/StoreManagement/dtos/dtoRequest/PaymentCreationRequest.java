package com.example.StoreManagement.dtos.dtoRequest;

import com.example.StoreManagement.enums.PaymentMethodType;
import com.example.StoreManagement.enums.PaymentProvider;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PaymentCreationRequest(
        @NotNull(message = "CheckoutId must not be empty")
        UUID checkoutId,

        @NotNull(message = "Payment provider must not be empty")
        PaymentProvider paymentProvider,

        @NotNull(message = "Payment method type must not be empty")
        PaymentMethodType paymentMethodType
) {}
