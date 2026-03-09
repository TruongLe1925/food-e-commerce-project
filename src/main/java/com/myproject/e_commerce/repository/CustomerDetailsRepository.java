package com.myproject.e_commerce.repository;

import com.myproject.e_commerce.entity.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Integer> {
    Optional<CustomerDetails> findByUserUsername(String username);
}
