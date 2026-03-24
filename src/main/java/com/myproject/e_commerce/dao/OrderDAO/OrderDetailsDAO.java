package com.myproject.e_commerce.dao.OrderDAO;

import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.entity.Orders;

import java.util.List;
import java.util.Optional;

public interface OrderDetailsDAO {
   Optional<Orders> findOrderById(Integer orderId);
   List<Orders> findAllOrder();
   List<Orders> findAllOrderByStatus(StatusOrder status);
   List<Orders> findAllOrderByUsername(String username);

}
