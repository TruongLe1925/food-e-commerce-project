package com.myproject.e_commerce.repository;

import com.myproject.e_commerce.entity.Cart;
import com.myproject.e_commerce.entity.CartItems;
import com.myproject.e_commerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems,Integer> {
    Optional<CartItems> findByCartAndProduct(Cart cart, Product product);
    List<CartItems> findByCart(Cart cart);
    void deleteAllByCart(Cart cart);
}
