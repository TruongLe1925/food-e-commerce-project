package com.myproject.e_commerce.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePage {
    @GetMapping("/home")
    public String home(){
        return "home/homePage";
    }
}
