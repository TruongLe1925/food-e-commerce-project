package com.myproject.e_commerce.controller.home;

import com.myproject.e_commerce.dto.CartDTO;
import com.myproject.e_commerce.repository.CartRepository;
import com.myproject.e_commerce.service.Cart.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class AddToCart {
    private CartService cartService;
    public AddToCart(CartService cartService) {
        this.cartService = cartService;
    }
    @PostMapping("/add-to-cart")
    public String addToCart(Principal principal, CartDTO cartDTO,@RequestParam("quantity") Integer quantity
    ,@RequestParam("productName") String productName) {
        cartDTO.setUsername(principal.getName());
        cartDTO.setQuantity(quantity);
        cartDTO.setProductName(productName);
        cartService.addCart(cartDTO);
        return "redirect:/";
    }
}
