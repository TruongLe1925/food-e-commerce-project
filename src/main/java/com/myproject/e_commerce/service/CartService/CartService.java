package com.myproject.e_commerce.service.CartService;

import com.myproject.e_commerce.dto.CartDTO;
import com.myproject.e_commerce.dto.CartResponseDTO;

public interface CartService {
    void addCart(CartDTO cartDTO);
    CartResponseDTO getCart(String username);
    void deleteCartItem(Integer cartItemsId);

}
