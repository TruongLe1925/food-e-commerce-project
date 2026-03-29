package com.myproject.e_commerce.controller.errorHandler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorHandler {
    @GetMapping("/customError")
    public String handleError() {
        return "/custom-error";
    }
}
