package com.example.StoreManagement.Exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyInUseException extends ApiException {
    private static final String RESOURCE_ALREADY_IN_USE = "resource-already-in-use";

    public ResourceAlreadyInUseException(String entityName, String id) {
        super(
                "%s with %s already exists.".formatted(entityName, id),
                HttpStatus.CONFLICT,
                RESOURCE_ALREADY_IN_USE
        );
    }

    public ResourceAlreadyInUseException(String value) {
        super(
                "%s already exists.".formatted(value),
                HttpStatus.CONFLICT,
                RESOURCE_ALREADY_IN_USE
        );
    }
}
