package com.myproject.e_commerce.controller.home;

import com.myproject.e_commerce.service.CustomerService.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePage {
    private CustomerService customerService;
    public HomePage(CustomerService customerService) {
        this.customerService = customerService;
    }
    @GetMapping("/home")
    public String home(Model model) {
        return "home";
    }
}
