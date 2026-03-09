package com.myproject.e_commerce.service.OrderService;

import com.myproject.e_commerce.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    void addToOrder(String username, String note);
    List<OrderDTO> getOrder(String username);
}
