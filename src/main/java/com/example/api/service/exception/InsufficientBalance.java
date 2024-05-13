package com.example.api.service.exception;

public class InsufficientBalance extends RuntimeException{
    public InsufficientBalance(String message) {
        super(message);
    }
}
