package com.myproject.e_commerce.service.OrderService;

import com.myproject.e_commerce.entity.*;
import com.myproject.e_commerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderServiceImpl implements OrderService {
    private CartItemsRepository cartItemsRepository;
    private OrdersRepository ordersRepository;
    private CustomerDetailsRepository customerDetailsRepository;
    private CartRepository cartRepository;
    private StatusRepository statusRepository;
    public OrderServiceImpl(OrdersRepository ordersRepository,CustomerDetailsRepository customerDetailsRepository, StatusRepository statusRepository,CartItemsRepository cartItemsRepository, OrderDetailsRepository orderDetailsRepository,CartRepository cartRepository) {
        this.cartItemsRepository = cartItemsRepository;
        this.ordersRepository = ordersRepository;
        this.statusRepository = statusRepository;
        this.customerDetailsRepository = customerDetailsRepository;
        this.cartRepository = cartRepository;
    }
    @Transactional
    @Override
    public void addToOrder(String username, String note) {
        CustomerDetails customerDetails =  customerDetailsRepository.findByUserUsername(username).orElse(null);
        Status status = statusRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Lỗi: Hệ thống chưa cấu hình trạng thái PENDING"));
        Cart cart = cartRepository.findByUser(customerDetails.getUser()).orElse(null);
        Orders orders = Orders.builder()
                .customerDetails(customerDetails)
                .status(status)
                .orderAddress("BlaBlaBla")
                .note(note)
                .build();
        for (CartItems item : cart.getCartItems()) {
            OrderDetails detail = OrderDetails.builder()
                    .product(item.getProduct())
                    .quantity(item.getQuantity())
                    .originalPrice(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .discountPrice(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .orders(orders)
                    .build();
            orders.addOrderDetails(detail);
        }
        ordersRepository.save(orders);
        cartItemsRepository.deleteAllByCart(cart);
        cart.getCartItems().clear();
    }
}
