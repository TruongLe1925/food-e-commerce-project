package com.myproject.e_commerce.service.OrderService;

import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.dao.OrderDAO.OrderDetailsDAO;
import com.myproject.e_commerce.dto.*;
import com.myproject.e_commerce.entity.*;
import com.myproject.e_commerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final CartItemsRepository cartItemsRepository;
    private final OrdersRepository ordersRepository;
    private final CustomerDetailsRepository customerDetailsRepository;
    private final CartRepository cartRepository;
    private final StatusRepository statusRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderDetailsDAO orderDetailsDAO;
    private final OrderProcess orderProcess;
    public OrderServiceImpl(OrderProcess orderProcess,OrderDetailsDAO orderDetailsDAO,OrdersRepository ordersRepository,CustomerDetailsRepository customerDetailsRepository, StatusRepository statusRepository,CartItemsRepository cartItemsRepository, OrderDetailsRepository orderDetailsRepository,CartRepository cartRepository) {
        this.orderProcess = orderProcess;
        this.orderDetailsDAO = orderDetailsDAO;
        this.orderDetailsRepository = orderDetailsRepository;
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
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ hàng!");
            }
            int stock = product.getStock() - item.getQuantity();
            product.setStock(stock);
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

    @Override
    public List<OrderDTO> getOrder(String username) {
        CustomerDetails customerDetails = customerDetailsRepository.findByUserUsername(username).orElse(null);
        List<Orders> ordersList = orderDetailsDAO.findAllOrderByUsername(username);
        return ordersList.stream().map(orders -> OrderDTO.builder()
                .orderId(orders.getId())
                .status(orders.getStatus().getStatus())
                .orderDate(orders.getOrderDate())
                .note(orders.getNote())
                .build())
        .toList();
    }

    @Override
    public OrderDetailsWrapperDTO getOrderDetails(Integer orderId,String username) {
        Orders orders = orderDetailsDAO.findOrderById(orderId);
        if (!orders.getCustomerDetails().getUser().getUsername().equals(username)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "Bạn không có quyền xem đơn hàng này!");
        }
        return orderProcess.getOrderDetails(orders,orderId);
    }
    @Override
    public OrderDetailsWrapperDTO getOrderDetailsForEmployee(Integer orderId) {
        Orders orders= orderDetailsDAO.findOrderById(orderId);
        return orderProcess.getOrderDetails(orders,orderId);
    }

    @Transactional
    @Override
    public void cancelOrder(Integer orderId) {
        Orders orders = orderDetailsDAO.findOrderById(orderId);
        StatusOrder statusOrder = StatusOrder.CANCELLED;
        Status status = statusRepository.findByStatus(statusOrder);
        if (orders.getStatus().getStatus() != StatusOrder.PENDING) {
            throw new RuntimeException("Chỉ có thể hủy đơn hàng khi đang ở trạng thái PENDING!");
        }
        orders.setStatus(status);
        for(OrderDetails orderDetails : orders.getOrderDetails()) {
            Product product = orderDetails.getProduct();
            product.setStock(product.getStock() + orderDetails.getQuantity());
        }
        ordersRepository.save(orders);
    }

    @Override
    public List<OrderProcessDTO> getOrderProcess() {
        List<Orders> orders = orderDetailsDAO.findAllOrder();
        return orderProcess.findOrderForEmployee(orders);
    }

    @Override
    public List<OrderProcessDTO> getOrderProcessByStatus(StatusOrder status) {
        List<Orders> orders = orderDetailsDAO.findAllOrderByStatus(status);
        return orderProcess.findOrderForEmployee(orders);
    }
    @Transactional
    @Override
    public void updateToNextStatus(Integer orderId) {
        Orders orders = orderDetailsDAO.findOrderById(orderId);
        StatusOrder nextEnum = orders.getStatus().getStatus().next();
        Status nextStatusEntity = statusRepository.findByStatus(nextEnum);
        orders.setStatus(nextStatusEntity);
        ordersRepository.save(orders);
    }
}
