package com.myproject.e_commerce.service.OrderService;

import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.dao.OrderDAO.OrderDetailsDAO;
import com.myproject.e_commerce.dto.*;
import com.myproject.e_commerce.entity.*;
import com.myproject.e_commerce.exception.exception.AccessDeniedException;
import com.myproject.e_commerce.exception.exception.InsufficientStockException;
import com.myproject.e_commerce.exception.exception.OrderNotFoundException;
import com.myproject.e_commerce.exception.exception.ProductNotFoundException;
import com.myproject.e_commerce.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;
    @Value("${orderAddress}")
    private String orderAddress;
    public OrderServiceImpl(ProductRepository productRepository,PromotionRepository promotionRepository,OrderProcess orderProcess,OrderDetailsDAO orderDetailsDAO,OrdersRepository ordersRepository,CustomerDetailsRepository customerDetailsRepository, StatusRepository statusRepository,CartItemsRepository cartItemsRepository, OrderDetailsRepository orderDetailsRepository,CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
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
    public void addToOrder(String username, String note,String code,BigDecimal cartTotalPrice,BigDecimal discountTotalPrice) {
        CustomerDetails customerDetails =  customerDetailsRepository.findByUserUsername(username).orElse(null);
        Status status = statusRepository.findByStatus(StatusOrder.PENDING);
        Promotion promotion = promotionRepository.findByName(code);
        Cart cart = cartRepository.findByUser(customerDetails.getUser()).orElse(null);
        Orders orders = Orders.builder()
                .customerDetails(customerDetails)
                .status(status)
                .promotion(promotion)
                .orderAddress(orderAddress)
                .originalPrice(cartTotalPrice)
                .discountPrice(discountTotalPrice)
                .note(note)
                .build();
        for (CartItems item : cart.getCartItems()) {
            Product product = productRepository.findByIdForUpdate(item.getProduct().getId())
                    .orElseThrow(() -> new ProductNotFoundException("Sản phẩm không tồn tại!"));
            if (product.getStock() < item.getQuantity()) {
                throw new InsufficientStockException("Sản phẩm " + product.getName() + " không đủ hàng!");
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
        Orders orders = orderDetailsDAO.findOrderById(orderId).orElseThrow(() -> new OrderNotFoundException("Không tồn tại đơn hàng này") );
        Promotion promotion = orders.getPromotion();
        if (!orders.getCustomerDetails().getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Bạn không có quyền xem đơn hàng này!");
        }
        return orderProcess.getOrderDetails(orders,orderId);
    }

    @Override
    public OrderDetailsWrapperDTO getOrderDetailsForEmployee(Integer orderId) {
        Orders orders= orderDetailsDAO.findOrderById(orderId).orElse(null);
        return orderProcess.getOrderDetails(orders,orderId);
    }

    @Transactional
    @Override
    public void cancelOrder(Integer orderId) {
        Orders orders = orderDetailsDAO.findOrderById(orderId).orElse(null);
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
        Orders orders = orderDetailsDAO.findOrderById(orderId).orElse(null);
        StatusOrder nextEnum = orders.getStatus().getStatus().next();
        Status nextStatusEntity = statusRepository.findByStatus(nextEnum);
        orders.setStatus(nextStatusEntity);
        ordersRepository.save(orders);
    }
}
