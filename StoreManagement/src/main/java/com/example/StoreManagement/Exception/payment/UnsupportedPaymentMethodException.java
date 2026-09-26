package com.example.StoreManagement.Exception.payment;

import com.example.StoreManagement.Exception.ApiException;
import org.springframework.http.HttpStatus;

public class UnsupportedPaymentMethodException extends ApiException {
    private static final String UNSUPPORTED_PAYMENT_METHOD = "unsupported-payment-method";

    public UnsupportedPaymentMethodException(String paymentMethod) {
        super(
                "Payment method %s is not supported yet".formatted(paymentMethod),
                HttpStatus.BAD_REQUEST,
                UNSUPPORTED_PAYMENT_METHOD
        );
    }
}
