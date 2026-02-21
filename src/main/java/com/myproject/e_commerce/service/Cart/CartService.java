package com.myproject.e_commerce.service.Cart;

import com.myproject.e_commerce.dto.CartDTO;
import com.myproject.e_commerce.entity.Cart;

public interface CartService {
    void addCart(CartDTO cartDTO);
}
