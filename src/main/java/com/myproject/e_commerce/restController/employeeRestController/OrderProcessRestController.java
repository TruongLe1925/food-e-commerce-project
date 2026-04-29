package com.myproject.e_commerce.restController.employeeRestController;

import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.dto.OrderDetailsWrapperDTO;
import com.myproject.e_commerce.dto.OrderProcessDTO;
import com.myproject.e_commerce.service.OrderService.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee/orders")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class OrderProcessRestController {
    private final OrderService orderService;

    public OrderProcessRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderProcessDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getOrderProcess());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderProcessDTO>> getOrdersByStatus(@PathVariable StatusOrder status) {
        return ResponseEntity.ok(orderService.getOrderProcessByStatus(status));
    }

    @GetMapping("/{orderId}/details")
    public ResponseEntity<OrderDetailsWrapperDTO> getOrderDetails(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.getOrderDetailsForEmployee(orderId));
    }

    @PutMapping("/{orderId}/next-status")
    public ResponseEntity<Void> updateToNextStatus(@PathVariable Integer orderId) {
        orderService.updateToNextStatus(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Integer orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
