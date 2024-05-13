package com.example.api.controller.exception;

import com.example.api.service.exception.DuplicateAccount;
import com.example.api.service.exception.EntityNotFound;
import com.example.api.service.exception.InsufficientBalance;
import com.example.api.service.exception.TransferAmount;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFound ex, HttpServletRequest request) {
        String error = "Not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse(Instant.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DuplicateAccount.class)
    public ResponseEntity<Object> handleDuplicateAccount(DuplicateAccount ex, HttpServletRequest request) {
        String error = "Conflict";
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse response = new ErrorResponse(Instant.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler({InsufficientBalance.class, TransferAmount.class})
    public ResponseEntity<Object> handleInsufficientBalance(Exception ex, HttpServletRequest request) {
        String error = "Bad Request";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(Instant.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }

}
