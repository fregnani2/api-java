package com.example.api.controller.exception;

import com.example.api.service.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 *  class responsible for handling exceptions.
 * Tag @RestControllerAdvice centralize exception handling for @RequestMapping methods
 */
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

    @ExceptionHandler({TransferAmount.class,WrongArgument.class})
    public ResponseEntity<Object> handleInsufficientBalance(Exception ex, HttpServletRequest request) {
        String error = "Bad Request";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(Instant.now(), status.value(), error, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }


}
