package com.example.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.common.dto.ResponseMessage;
import com.example.common.enums.ResponseStatus;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseMessage<String>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        ResponseMessage<String> response = new ResponseMessage<>(
            ResponseStatus.ERROR,
            ex.getMessage()
        );
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseMessage<String>> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        
        ResponseMessage<String> response = new ResponseMessage<>(
            ResponseStatus.ERROR,
            ex.getMessage()
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage<String>> handleGlobalException(
            Exception ex, WebRequest request) {
        
        ResponseMessage<String> response = new ResponseMessage<>(
            ResponseStatus.ERROR,
            "An internal server error occurred: " + ex.getMessage()
        );
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseMessage<String>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        ResponseMessage<String> response = new ResponseMessage<>(
            ResponseStatus.ERROR,
            ex.getMessage()
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
