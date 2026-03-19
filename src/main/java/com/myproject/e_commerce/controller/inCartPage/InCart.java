package com.myproject.e_commerce.controller.inCartPage;

import com.myproject.e_commerce.dto.CartResponseDTO;
import com.myproject.e_commerce.service.CartService.CartService;
import com.myproject.e_commerce.service.OrderService.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;

@Controller
@RequestMapping("cart")
public class InCart {
    private final CartService cartService;
    private final OrderService orderService;
    public  InCart(CartService cartService,OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }
    @GetMapping("/showCart")
    public String cart(Model model, Principal principal,@RequestParam(value = "code",required = false) String code ) {
        CartResponseDTO inCartDTOS = cartService.getCart(principal.getName(),code);
        model.addAttribute("itemCarts", inCartDTOS);
        return "/shop-homepage/shopping-cart";
    }
    @PostMapping("/delete")
    public String deleteCartItem(@RequestParam("cartItemsId") Integer cartItemsId) {
        cartService.deleteCartItem(cartItemsId);
        return "redirect:/cart/showCart";
    }
    @PostMapping("/checkout")
    public String checkout(Principal principal, @RequestParam(value = "note",required = false) String note
            , @RequestParam(value = "voucherCode",required = false) String code
            , @RequestParam(value = "cartTotalPrice")BigDecimal cartTotalPrice
            , @RequestParam(value = "discountTotalPrice") BigDecimal discountTotalPrice) {
        orderService.addToOrder(principal.getName(), note,code,cartTotalPrice,discountTotalPrice);
        return "redirect:/cart/showCart";
    }

}
