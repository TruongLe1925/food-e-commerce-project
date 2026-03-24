package com.myproject.e_commerce.exception.exceptionHandler;

import com.myproject.e_commerce.exception.errorReponse.EntityErrorResponse;
import com.myproject.e_commerce.exception.exception.InsufficientStockException;
import com.myproject.e_commerce.exception.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<EntityErrorResponse> handleException(ProductNotFoundException e) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<EntityErrorResponse> handleException(InsufficientStockException e) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
