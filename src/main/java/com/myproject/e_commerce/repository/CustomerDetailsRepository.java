package com.myproject.e_commerce.repository;

import com.myproject.e_commerce.entity.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Integer> {

}
