package com.example.api.service.exception;

/**
 * Exception when trying to access an entity that does not exist
 */
public class EntityNotFound extends RuntimeException{
    public EntityNotFound(String message) {
        super(message);
    }

}
