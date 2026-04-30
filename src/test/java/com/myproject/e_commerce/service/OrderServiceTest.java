package com.myproject.e_commerce.service;

import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.dao.OrderDAO.OrderDetailsDAO;
import com.myproject.e_commerce.dto.OrderDTO;
import com.myproject.e_commerce.dto.OrderDetailsWrapperDTO;
import com.myproject.e_commerce.dto.OrderProcessDTO;
import com.myproject.e_commerce.entity.*;
import com.myproject.e_commerce.exception.exception.AccessDeniedException;
import com.myproject.e_commerce.exception.exception.InsufficientStockException;
import com.myproject.e_commerce.exception.exception.OrderNotFoundException;
import com.myproject.e_commerce.repository.*;
import com.myproject.e_commerce.service.OrderService.OrderProcess;
import com.myproject.e_commerce.service.OrderService.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartItemsRepository cartItemsRepository;

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private CustomerDetailsRepository customerDetailsRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Mock
    private OrderDetailsDAO orderDetailsDAO;

    @Mock
    private OrderProcess orderProcess;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private CustomerDetails customerDetails;
    private Cart cart;
    private Product product;
    private CartItems cartItem;
    private Status pendingStatus;
    private Orders order;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(orderService, "orderAddress", "123 Test Street");

        user = new User();
        user.setUsername("testuser");

        customerDetails = new CustomerDetails();
        customerDetails.setId(1);
        customerDetails.setUser(user);

        cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        Set<CartItems> cartItems = new HashSet<>();
        cart.setCartItems(cartItems);

        product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100000));
        product.setStock(10);

        cartItem = new CartItems();
        cartItem.setId(1);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cart.getCartItems().add(cartItem);

        pendingStatus = new Status();
        pendingStatus.setId(1);
        pendingStatus.setStatus(StatusOrder.PENDING);

        order = new Orders();
        order.setId(1);
        order.setCustomerDetails(customerDetails);
        order.setStatus(pendingStatus);
        order.setOrderDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("Create new order successfully - Deduct stock and clear cart")
    void addToOrder_Success() {
        // Given
        when(customerDetailsRepository.findByUserUsername("testuser")).thenReturn(Optional.of(customerDetails));
        when(statusRepository.findByStatus(StatusOrder.PENDING)).thenReturn(pendingStatus);
        when(promotionRepository.findByName("PROMO10")).thenReturn(null);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findByIdForUpdate(1)).thenReturn(Optional.of(product));
        when(ordersRepository.save(any(Orders.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        orderService.addToOrder("testuser", "Giao giờ hành chính", "PROMO10",
                BigDecimal.valueOf(200000), BigDecimal.valueOf(180000));

        // Then
        assertEquals(8, product.getStock(), "Stock should be deducted correctly");
        verify(ordersRepository).save(any(Orders.class));
        verify(cartItemsRepository).deleteAllByCart(cart);
    }

    @Test
    @DisplayName("Create order failed - Insufficient stock")
    void addToOrder_InsufficientStock_ThrowException() {
        // Given
        product.setStock(1);
        when(customerDetailsRepository.findByUserUsername("testuser")).thenReturn(Optional.of(customerDetails));
        when(statusRepository.findByStatus(StatusOrder.PENDING)).thenReturn(pendingStatus);
        when(promotionRepository.findByName("PROMO10")).thenReturn(null);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findByIdForUpdate(1)).thenReturn(Optional.of(product));

        // When & Then
        InsufficientStockException exception = assertThrows(InsufficientStockException.class, () ->
                orderService.addToOrder("testuser", "Test", "PROMO10",
                        BigDecimal.valueOf(200000), BigDecimal.valueOf(180000)));
        assertTrue(exception.getMessage().contains("không đủ hàng") || exception.getMessage().contains("not enough"));
    }

    @Test
    @DisplayName("Get user order list successfully")
    void getOrder_Success() {
        // Given
        List<Orders> ordersList = Collections.singletonList(order);
        when(customerDetailsRepository.findByUserUsername("testuser")).thenReturn(Optional.of(customerDetails));
        when(orderDetailsDAO.findAllOrderByUsername("testuser")).thenReturn(ordersList);

        // When
        List<OrderDTO> result = orderService.getOrder("testuser");

        // Then
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getOrderId());
        assertEquals(StatusOrder.PENDING, result.get(0).getStatus());
    }

    @Test
    @DisplayName("Get order details successfully")
    void getOrderDetails_Success() {
        // Given
        OrderDetailsWrapperDTO wrapperDTO = OrderDetailsWrapperDTO.builder()
                .orderId(1)
                .status(StatusOrder.PENDING)
                .build();

        when(orderDetailsDAO.findOrderById(1)).thenReturn(Optional.of(order));
        when(orderProcess.getOrderDetails(order, 1)).thenReturn(wrapperDTO);

        // When
        OrderDetailsWrapperDTO result = orderService.getOrderDetails(1, "testuser");

        // Then
        assertEquals(1, result.getOrderId());
        verify(orderProcess).getOrderDetails(order, 1);
    }

    @Test
    @DisplayName("Get order details failed - Access denied")
    void getOrderDetails_WrongUser_ThrowAccessDeniedException() {
        // Given
        User otherUser = new User();
        otherUser.setUsername("otheruser");
        CustomerDetails otherCustomer = new CustomerDetails();
        otherCustomer.setId(2);
        otherCustomer.setUser(otherUser);
        Orders otherOrder = new Orders();
        otherOrder.setId(2);
        otherOrder.setCustomerDetails(otherCustomer);

        when(orderDetailsDAO.findOrderById(2)).thenReturn(Optional.of(otherOrder));

        // When & Then
        assertThrows(AccessDeniedException.class, () -> orderService.getOrderDetails(2, "testuser"));
    }

    @Test
    @DisplayName("Cancel order successfully - Restore stock")
    void cancelOrder_Success() {
        // Given
        Status cancelledStatus = new Status();
        cancelledStatus.setId(2);
        cancelledStatus.setStatus(StatusOrder.CANCELLED);

        OrderDetails orderDetail = new OrderDetails();
        orderDetail.setId(1);
        orderDetail.setProduct(product);
        orderDetail.setQuantity(2);
        order.setOrderDetails(new ArrayList<>(Collections.singletonList(orderDetail)));

        when(orderDetailsDAO.findOrderById(1)).thenReturn(Optional.of(order));
        when(statusRepository.findByStatus(StatusOrder.CANCELLED)).thenReturn(cancelledStatus);
        when(ordersRepository.save(order)).thenReturn(order);

        // When
        orderService.cancelOrder(1);

        // Then
        assertEquals(StatusOrder.CANCELLED, order.getStatus().getStatus());
        assertEquals(12, product.getStock(), "Stock should be restored");
        verify(ordersRepository).save(order);
    }

    @Test
    @DisplayName("Cancel order failed - Order not in PENDING status")
    void cancelOrder_NotPendingStatus_ThrowException() {
        // Given
        Status confirmedStatus = new Status();
        confirmedStatus.setId(3);
        confirmedStatus.setStatus(StatusOrder.CONFIRMED);
        order.setStatus(confirmedStatus);

        when(orderDetailsDAO.findOrderById(1)).thenReturn(Optional.of(order));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.cancelOrder(1));
        assertEquals("Chỉ có thể hủy đơn hàng khi đang ở trạng thái PENDING!", exception.getMessage());
    }

    @Test
    @DisplayName("Update order status to next step")
    void updateToNextStatus_Success() {
        // Given
        Status confirmedStatus = new Status();
        confirmedStatus.setId(3);
        confirmedStatus.setStatus(StatusOrder.CONFIRMED);

        when(orderDetailsDAO.findOrderById(1)).thenReturn(Optional.of(order));
        when(statusRepository.findByStatus(StatusOrder.CONFIRMED)).thenReturn(confirmedStatus);
        when(ordersRepository.save(order)).thenReturn(order);

        // When
        orderService.updateToNextStatus(1);

        // Then
        assertEquals(StatusOrder.CONFIRMED, order.getStatus().getStatus());
        verify(ordersRepository).save(order);
    }

    @Test
    @DisplayName("Employee get order process list")
    void getOrderProcess_Success() {
        // Given
        List<Orders> orders = Collections.singletonList(order);
        List<OrderProcessDTO> processDTOs = Collections.singletonList(
                OrderProcessDTO.builder().orderId(1).fullname("Test User").build());

        when(orderDetailsDAO.findAllOrder()).thenReturn(orders);
        when(orderProcess.findOrderForEmployee(orders)).thenReturn(processDTOs);

        // When
        List<OrderProcessDTO> result = orderService.getOrderProcess();

        // Then
        assertEquals(1, result.size());
        assertEquals("Test User", result.get(0).getFullname());
    }

    @Test
    @DisplayName("Get orders by status")
    void getOrderProcessByStatus_Success() {
        // Given
        List<Orders> orders = Collections.singletonList(order);
        List<OrderProcessDTO> processDTOs = Collections.singletonList(
                OrderProcessDTO.builder().orderId(1).status(StatusOrder.PENDING).build());

        when(orderDetailsDAO.findAllOrderByStatus(StatusOrder.PENDING)).thenReturn(orders);
        when(orderProcess.findOrderForEmployee(orders)).thenReturn(processDTOs);

        // When
        List<OrderProcessDTO> result = orderService.getOrderProcessByStatus(StatusOrder.PENDING);

        // Then
        assertEquals(1, result.size());
        assertEquals(StatusOrder.PENDING, result.get(0).getStatus());
    }

    @Test
    @DisplayName("Get order details for employee")
    void getOrderDetailsForEmployee_Success() {
        // Given
        OrderDetailsWrapperDTO wrapperDTO = OrderDetailsWrapperDTO.builder()
                .orderId(1)
                .status(StatusOrder.PENDING)
                .build();

        when(orderDetailsDAO.findOrderById(1)).thenReturn(Optional.of(order));
        when(orderProcess.getOrderDetails(order, 1)).thenReturn(wrapperDTO);

        // When
        OrderDetailsWrapperDTO result = orderService.getOrderDetailsForEmployee(1);

        // Then
        assertEquals(1, result.getOrderId());
        assertEquals(StatusOrder.PENDING, result.getStatus());
    }
}
