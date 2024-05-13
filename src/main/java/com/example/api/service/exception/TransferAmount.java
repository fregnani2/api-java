package com.example.api.service.exception;

public class TransferAmount extends RuntimeException{
    public TransferAmount(String message) {
        super(message);
    }
}
