package com.myproject.e_commerce.controller.loginController;

import com.myproject.e_commerce.dto.CustomerRegistrationDTO;
import com.myproject.e_commerce.service.CustomerService.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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
    public String save(@Valid @ModelAttribute("CustomerRegistrationDTO") CustomerRegistrationDTO customerRegistrationDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "login/register-form";
        }else {
            customerService.save(customerRegistrationDTO);
            return "redirect:/account/login";
        }
    }
}
