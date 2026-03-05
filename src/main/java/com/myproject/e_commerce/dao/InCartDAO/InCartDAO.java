package com.myproject.e_commerce.dao.InCartDAO;

import com.myproject.e_commerce.entity.Cart;
import com.myproject.e_commerce.entity.CartItems;

import java.util.List;

public interface InCartDAO {
    Cart getCart(String username);

}
