package com.myproject.e_commerce.restController.inCartRestController;

import com.myproject.e_commerce.dto.CartResponseDTO;
import com.myproject.e_commerce.service.CartService.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartRestController {
    private final CartService cartService;

    public CartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(@AuthenticationPrincipal Jwt jwt,
                                                     @RequestParam(required = false) String code) {
        String username = jwt.getSubject();
        return ResponseEntity.ok(cartService.getCart(username, code));
    }

    @DeleteMapping("/item/{cartItemsId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Integer cartItemsId) {
        cartService.deleteCartItem(cartItemsId);
        return ResponseEntity.ok().build();
    }
}
