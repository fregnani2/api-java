package com.example.api.service.exception;

/**
 * Exception when provided post data is wrong
 */
public class WrongArgument extends RuntimeException{
    public WrongArgument(String msg){
        super(msg);
    }
}
