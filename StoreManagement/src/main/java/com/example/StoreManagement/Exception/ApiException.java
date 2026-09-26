package com.example.StoreManagement.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String type;

    public ApiException(String message, HttpStatus status, String type) {
        super(message);
        this.status = status;
        this.type = type;
    }
}
