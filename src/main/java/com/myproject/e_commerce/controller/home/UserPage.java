package com.myproject.e_commerce.controller.home;

import com.myproject.e_commerce.dto.CustomerDetailDTO;
import com.myproject.e_commerce.entity.User;
import com.myproject.e_commerce.service.CustomerService.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/cus")
public class UserPage {
    private final CustomerService customerService;
    public UserPage(CustomerService customerService) {
        this.customerService = customerService;
    }
    @GetMapping("/customerdetail")
    public String userDetails(Model model, Principal principal){
        String username = principal.getName();
        CustomerDetailDTO customerDetailDTO = customerService.getCustomerDetailsByUsername(username);
        model.addAttribute("user", customerDetailDTO);
        return "/user-profile/customer-details";
    }
}
