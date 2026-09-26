package com.example.StoreManagement.Exception.payment;

import com.example.StoreManagement.Exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidWebhookPayloadException extends ApiException {
    private static final String WEBHOOK_PAYLOAD_FAILURE = "webhook-payload-failure";

    public InvalidWebhookPayloadException(String message) {
        super(
                message,
                HttpStatus.BAD_REQUEST,
                WEBHOOK_PAYLOAD_FAILURE
        );
    }
}
