package com.myproject.e_commerce.exception.exceptionHandler;

import com.myproject.e_commerce.exception.exception.*;
import org.springframework.core.annotation.Order;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice(annotations = Controller.class)
@Order(2)
public class MVCExceptionHandler {
    @ExceptionHandler(InsufficientStockException.class)
    public String handleProductNotFoundException(InsufficientStockException e, RedirectAttributes  attributes) {
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/cart/showCart";
    }
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException e, RedirectAttributes  attributes) {
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/cus/orders";
    }
    @ExceptionHandler(OrderNotFoundException.class)
    public String handleOrderNotFoundException(OrderNotFoundException e, RedirectAttributes  attributes) {
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/cus/orders";
    }
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e, RedirectAttributes  attributes) {
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/error/customError";
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e, RedirectAttributes  attributes) {
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/error/customError";
    }
    @ExceptionHandler(PromotionNotFoundException.class)
    public String handlePromotionNotFoundException(PromotionNotFoundException e, RedirectAttributes  attributes) {
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/cart/showCart";
    }
    @ExceptionHandler(PromotionExpiredException.class)
    public String handlePromotionExpiredException(PromotionExpiredException e, RedirectAttributes  attributes) {
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/cart/showCart";
    }
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public String handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e, RedirectAttributes  attributes) {
        attributes.addFlashAttribute("errorMessageUsername", e.getMessage());
        return "/login/register-form";
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailAlreadyExistsException(EmailAlreadyExistsException e, RedirectAttributes  attributes) {
        attributes.addFlashAttribute("errorMessageEmail", e.getMessage());
        return "/login/register-form";
    }
    @ExceptionHandler(PasswordMismatchException.class)
    public String handlePasswordMismatchException(PasswordMismatchException e, RedirectAttributes  attributes) {
        attributes.addFlashAttribute("errorMessagePasswordMismatch", e.getMessage());
        return "/login/register-form";
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResourceFoundException(NoResourceFoundException e, RedirectAttributes  attributes) {
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/error/customError";
    }
    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointerException(NullPointerException e, RedirectAttributes  attributes) {
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/error/customError";
    }
}
