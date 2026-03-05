package com.myproject.e_commerce.service.Cart;

import com.myproject.e_commerce.dto.CartDTO;
import com.myproject.e_commerce.dto.CartResponseDTO;

public interface CartService {
    void addCart(CartDTO cartDTO);
    CartResponseDTO getCart(String username);
    void deleteCartItem(Integer cartItemsId);
}
