package com.myproject.e_commerce.repository;

import com.myproject.e_commerce.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    Optional<Status> findById(Integer id);
}
