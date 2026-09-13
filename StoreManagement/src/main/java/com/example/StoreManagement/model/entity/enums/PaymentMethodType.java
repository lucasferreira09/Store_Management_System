package com.example.StoreManagement.model.entity.enums;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum PaymentMethodType {
    @JsonAlias({"CREDIT_CARD"})
    CARD,
    PIX,
    BOLETO,
    PAYPAL_BALANCE
}
