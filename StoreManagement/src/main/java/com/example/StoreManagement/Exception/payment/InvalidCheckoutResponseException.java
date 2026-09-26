package com.example.StoreManagement.Exception.payment;

import com.example.StoreManagement.Exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidCheckoutResponseException extends ApiException {
    private static final String CHECKOUT_RESPONSE_FAILURE = "checkout-response-failure";
    public static final String PAY_LINK_NOT_FOUND = "PAY link not found";
    public static final String CHECKOUT_URL_NOT_FOUND = "Checkout URL link not found";

    public InvalidCheckoutResponseException(String message) {
        super(
                message,
                HttpStatus.INTERNAL_SERVER_ERROR,
                CHECKOUT_RESPONSE_FAILURE
        );
    }
}
