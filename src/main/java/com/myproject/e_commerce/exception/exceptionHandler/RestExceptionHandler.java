package com.myproject.e_commerce.exception.exceptionHandler;

import com.myproject.e_commerce.response.EntityErrorResponse;
import com.myproject.e_commerce.exception.exception.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
@Order(1)
public class RestExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<EntityErrorResponse> userNotFoundExeption(UserNotFoundException exeption) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(exeption.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<EntityErrorResponse> handleException(ProductNotFoundException e) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<EntityErrorResponse> handleException(InsufficientStockException e) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<EntityErrorResponse> handleException(OrderNotFoundException e) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<EntityErrorResponse> handleException(AccessDeniedException e) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<EntityErrorResponse> handleException(EmailAlreadyExistsException e) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<EntityErrorResponse> handleException(PasswordMismatchException e) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<EntityErrorResponse> handleException(UsernameAlreadyExistsException e) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(PromotionExpiredException.class)
    public ResponseEntity<EntityErrorResponse> handleException(PromotionExpiredException e) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PromotionNotFoundException.class)
    public ResponseEntity<EntityErrorResponse> handleException(PromotionNotFoundException e) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<EntityErrorResponse> handleException(RuntimeException e) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EntityErrorResponse> handleException(MethodArgumentNotValidException e) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<EntityErrorResponse> handleException(NullPointerException e) {
        EntityErrorResponse response = new EntityErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
