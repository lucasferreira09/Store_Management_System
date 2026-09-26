package com.example.StoreManagement.Exception.payment;

import com.example.StoreManagement.Exception.ApiException;
import org.springframework.http.HttpStatus;

public class WebhookProcessingException extends ApiException {
    private static final String WEBHOOK_PROCESSING_FAILED = "webhook-processing-failed";

    public WebhookProcessingException(String message) {
        super(
                message,
                HttpStatus.INTERNAL_SERVER_ERROR,
                WEBHOOK_PROCESSING_FAILED
        );

    }
}
