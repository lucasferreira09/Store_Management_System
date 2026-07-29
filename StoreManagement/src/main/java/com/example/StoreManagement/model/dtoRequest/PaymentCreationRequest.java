package com.example.StoreManagement.model.dtoRequest;

import com.example.StoreManagement.model.entity.enums.PaymentMethodType;
import com.example.StoreManagement.model.entity.enums.PaymentProvider;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PaymentCreationRequest(
        List<String> orderIds,
        BigDecimal amount,
        String currency,
        PaymentProvider paymentProvider,
        PaymentMethodType paymentMethodType,
        String cardBrand,
        String lastCardNumbers,
        String providerPaymentId,
        String sessionProviderId,
        String providerChargeId,
        LocalDateTime createdAt
) {}
