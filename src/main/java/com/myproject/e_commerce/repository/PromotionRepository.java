package com.myproject.e_commerce.repository;

import com.myproject.e_commerce.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    Promotion findByName(String name);
}
