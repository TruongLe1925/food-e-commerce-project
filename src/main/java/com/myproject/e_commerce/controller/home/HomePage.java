package com.myproject.e_commerce.controller.home;

import com.myproject.e_commerce.dto.ProductHomePageDTO;
import com.myproject.e_commerce.entity.Product;
import com.myproject.e_commerce.service.CustomerService.CustomerService;
import com.myproject.e_commerce.service.ProductService.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomePage {
    private ProductService productService;
    public HomePage(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/")
    public String home(Model model) {
        List<ProductHomePageDTO> productHomePageDTOS = productService.findAllProducts();
        System.out.println("productHomePageDTOS : "+productHomePageDTOS);
        model.addAttribute("product", productHomePageDTOS);
        return "/shop-homepage/index";
    }
}
