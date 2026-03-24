package com.myproject.e_commerce.exception.exceptionHandler;

import com.myproject.e_commerce.exception.exception.AccessDeniedException;
import com.myproject.e_commerce.exception.exception.InsufficientStockException;
import com.myproject.e_commerce.exception.exception.OrderNotFoundException;
import com.myproject.e_commerce.exception.exception.ProductNotFoundException;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(annotations = Controller.class)
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
}
