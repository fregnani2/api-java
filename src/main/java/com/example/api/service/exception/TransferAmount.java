package com.example.api.service.exception;

/**
 * Exception when trying to transfer more money than the account balance or less than 1
 */
public class TransferAmount extends RuntimeException{
    public TransferAmount(String message) {
        super(message);
    }
}
