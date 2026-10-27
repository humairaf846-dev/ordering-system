package com.example.orderingsystem.exception;

import com.example.orderingsystem.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessValidation(BusinessValidationException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}