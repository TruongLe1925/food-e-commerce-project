package com.myproject.e_commerce.service.OrderService;

import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.dto.OrderDTO;
import com.myproject.e_commerce.dto.OrderDetailsWrapperDTO;
import com.myproject.e_commerce.dto.OrderProcessDTO;

import java.util.List;

public interface OrderService {
    void addToOrder(String username, String note);
    List<OrderDTO> getOrder(String username);
    OrderDetailsWrapperDTO getOrderDetails(Integer orderId, String username);
    OrderDetailsWrapperDTO getOrderDetailsForEmployee(Integer orderId);
    List<OrderProcessDTO> getOrderProcess();
    List<OrderProcessDTO> getOrderProcessByStatus(StatusOrder status);
    void updateToNextStatus(Integer orderId);
    void cancelOrder(Integer orderId);
}
