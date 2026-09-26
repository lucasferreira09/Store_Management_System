package com.example.StoreManagement.Exception.payment;

import com.example.StoreManagement.Exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidSessionStateException extends ApiException {
    private static final String INVALID_SESSION_STATE = "invalid-session-state";
    public static final String SESSION_COMPLETED_OR_EXPIRED = "Session already completed or expired";

    public InvalidSessionStateException(String message) {
        super(
                message,
                HttpStatus.BAD_REQUEST,
                INVALID_SESSION_STATE
        );
    }
}
