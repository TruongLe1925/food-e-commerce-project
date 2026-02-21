package com.myproject.e_commerce.repository;

import com.myproject.e_commerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
        Product findByName(String productName);
}
