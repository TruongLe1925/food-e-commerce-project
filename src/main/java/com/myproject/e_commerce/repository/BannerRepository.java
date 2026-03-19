package com.myproject.e_commerce.repository;

import com.myproject.e_commerce.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner,Integer> {
    Banner findFirstByOrderByIdAsc();
}
