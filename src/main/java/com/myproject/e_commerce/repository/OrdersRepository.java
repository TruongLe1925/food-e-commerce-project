package com.myproject.e_commerce.repository;

import com.myproject.e_commerce.entity.CustomerDetails;
import com.myproject.e_commerce.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByCustomerDetails(CustomerDetails customerDetails);
}
