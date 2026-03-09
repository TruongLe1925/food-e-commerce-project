package com.myproject.e_commerce.service.OrderService;

import com.myproject.e_commerce.dto.OrderDetailsWrapperDTO;
import com.myproject.e_commerce.dto.OrderProcessDTO;
import com.myproject.e_commerce.dto.ProductHomePageDTO;
import com.myproject.e_commerce.entity.Orders;

import java.util.List;

public interface OrderProcess {
    List<OrderProcessDTO> findOrderForEmployee(List<Orders> orders);
    OrderDetailsWrapperDTO getOrderDetails(Orders order,Integer orderId);
}
