package com.myproject.e_commerce.controller.home;

import com.myproject.e_commerce.dto.CartDTO;
import com.myproject.e_commerce.dto.CartResponseDTO;
import com.myproject.e_commerce.dto.InCartDTO;
import com.myproject.e_commerce.dto.ProductHomePageDTO;
import com.myproject.e_commerce.entity.CartItems;
import com.myproject.e_commerce.entity.Product;
import com.myproject.e_commerce.service.Cart.CartService;
import com.myproject.e_commerce.service.CustomerService.CustomerService;
import com.myproject.e_commerce.service.ProductService.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
public class HomePage {
    private ProductService productService;
    private CartService cartService;
    public HomePage(ProductService productService,CartService cartService) {
        this.cartService = cartService;
        this.productService = productService;
    }
    @GetMapping("/")
    public String home(Model model) {
        List<ProductHomePageDTO> productHomePageDTOS = productService.findAllProducts();
        System.out.println("productHomePageDTOS : "+productHomePageDTOS);
        model.addAttribute("product", productHomePageDTOS);
        return "/shop-homepage/index";
    }

    @PostMapping("/addToCart")
    @ResponseBody
    public String addToCart(Principal principal, CartDTO cartDTO, @RequestParam("quantity") Integer quantity
            , @RequestParam("productName") String productName) {
        cartDTO.setUsername(principal.getName());
        cartDTO.setQuantity(quantity);
        cartDTO.setProductName(productName);
        cartService.addCart(cartDTO);
        return "";
    }

}
