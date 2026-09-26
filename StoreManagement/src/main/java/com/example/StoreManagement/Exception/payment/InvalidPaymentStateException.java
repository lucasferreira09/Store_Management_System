package com.example.StoreManagement.Exception.payment;

import com.example.StoreManagement.Exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidPaymentStateException extends ApiException {
    private static final String INVALID_PAYMENT_STATE = "invalid-payment-state";
    public static final String ALREADY_COMPLETED = "Cannot modify payment. It's already completed";
    public static final String ORDERS_ARE_ALREADY_COMPLETED = "Orders are already completed";


    public InvalidPaymentStateException(String message) {
        super(
                message,
                HttpStatus.INTERNAL_SERVER_ERROR,
                INVALID_PAYMENT_STATE
        );
    }
}
