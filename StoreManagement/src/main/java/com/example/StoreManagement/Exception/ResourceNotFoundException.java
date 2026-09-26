package com.example.StoreManagement.Exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
    private static final String RESOURCE_NOT_FOUND_BY_ID = "resource-not-found";

    public ResourceNotFoundException(String entityName, String id) {
        super(
                "%s with %s not found.".formatted(entityName, id),
                HttpStatus.NOT_FOUND,
                RESOURCE_NOT_FOUND_BY_ID
        );
    }
}
