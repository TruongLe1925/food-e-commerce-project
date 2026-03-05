package com.myproject.e_commerce.controller.inCartPage;

import com.myproject.e_commerce.dto.CartResponseDTO;
import com.myproject.e_commerce.service.Cart.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("cart")
public class InCart {
    private final CartService cartService;
    public  InCart(CartService cartService) {
        this.cartService = cartService;
    }
    @GetMapping("/showCart")
    public String cart(Model model, Principal principal) {
        CartResponseDTO inCartDTOS = cartService.getCart(principal.getName());
        model.addAttribute("itemCarts", inCartDTOS);
        return "/shop-homepage/shopping-cart";
    }
    @PostMapping("/delete")
    public String deleteCartItem(@RequestParam("cartItemsId") Integer cartItemsId) {
        cartService.deleteCartItem(cartItemsId);
        return "redirect:/cart/showCart";
    }
}
