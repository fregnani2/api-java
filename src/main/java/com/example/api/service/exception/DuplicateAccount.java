package com.example.api.service.exception;

/**
 * Exception when trying to create an account with an account number that already exists
 */
public class DuplicateAccount extends RuntimeException{
    public DuplicateAccount(String message) {
        super(message);
    }
}
