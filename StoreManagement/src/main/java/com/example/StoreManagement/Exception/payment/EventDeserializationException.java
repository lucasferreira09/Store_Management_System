package com.example.StoreManagement.Exception.payment;

import com.example.StoreManagement.Exception.ApiException;
import org.springframework.http.HttpStatus;

public class EventDeserializationException extends ApiException {
    private static final String EVENT_DESERIALIZATION_FAILED = "event-deserialization-failed";

    public EventDeserializationException(String message) {
        super(
                message,
                HttpStatus.INTERNAL_SERVER_ERROR,
                EVENT_DESERIALIZATION_FAILED
        );
    }
}
