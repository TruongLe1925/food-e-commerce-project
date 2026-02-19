package com.myproject.e_commerce.controller.logincontroller;

import com.myproject.e_commerce.dto.CustomerRegistrationDTO;
import com.myproject.e_commerce.service.CustomerService.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class LoginController {
    private final CustomerService customerService;
    public LoginController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @GetMapping("/login")
    public String login(){
        return "login/login-form";
    }
    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("CustomerRegistrationDTO", new CustomerRegistrationDTO());
        return "login/register-form";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute("CustomerRegistrationDTO") CustomerRegistrationDTO customerRegistrationDTO){
        customerService.save(customerRegistrationDTO);
        return "redirect:/account/login";
    }
}
