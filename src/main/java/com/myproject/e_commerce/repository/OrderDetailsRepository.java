package com.myproject.e_commerce.repository;

import com.myproject.e_commerce.entity.OrderDetails;
import com.myproject.e_commerce.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    List<OrderDetails> findByOrders(Orders orders);
}
