package com.myproject.e_commerce.repository;

import com.myproject.e_commerce.entity.CustomerDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Integer> {
    Optional<CustomerDetails> findByUserUsername(String username);
    @Query("SELECT c FROM CustomerDetails c WHERE LOWER(c.fullName) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(c.email) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<CustomerDetails> findByKeyword(String keyword, Pageable pageable);
    Page<CustomerDetails> findAll(Pageable pageable);
}
