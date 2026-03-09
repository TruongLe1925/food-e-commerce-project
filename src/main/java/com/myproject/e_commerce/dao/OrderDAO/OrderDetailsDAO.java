package com.myproject.e_commerce.dao.OrderDAO;

import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.entity.Orders;

import java.util.List;

public interface OrderDetailsDAO {
   Orders findOrderById(Integer orderId);
   List<Orders> findAllOrder();
   List<Orders> findAllOrderByStatus(StatusOrder status);
   List<Orders> findAllOrderByUsername(String username);

}
