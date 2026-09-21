package com.example.StoreManagement.Exception;

public class EntityNotFound extends ApiException {
    public EntityNotFound(Long id) {
        super("Entity not found with id " + id);
    }
}
