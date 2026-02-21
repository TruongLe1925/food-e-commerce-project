package com.myproject.e_commerce.repository;

import com.myproject.e_commerce.entity.Cart;
import com.myproject.e_commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Optional<Cart> findByUser(User user);
}
