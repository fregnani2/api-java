package com.example.api.service.exception;

public class DuplicateAccount extends RuntimeException{
    public DuplicateAccount(String message) {
        super(message);
    }
}
