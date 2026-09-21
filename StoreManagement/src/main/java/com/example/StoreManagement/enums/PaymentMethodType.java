package com.example.StoreManagement.enums;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum PaymentMethodType {
    @JsonAlias({"CREDIT_CARD"})
    CARD,
    PIX,
    BOLETO,
    PAYPAL_BALANCE
}
