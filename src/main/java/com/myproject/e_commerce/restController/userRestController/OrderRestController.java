package com.myproject.e_commerce.restController.userRestController;

import com.myproject.e_commerce.dto.OrderDTO;
import com.myproject.e_commerce.dto.OrderDetailsWrapperDTO;
import com.myproject.e_commerce.service.OrderService.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasRole('CUSTOMER')")
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getUserOrders(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        return ResponseEntity.ok(orderService.getOrder(username));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsWrapperDTO> getOrderDetails(@AuthenticationPrincipal Jwt jwt,
                                                                  @PathVariable Integer orderId) {
        String username = jwt.getSubject();
        return ResponseEntity.ok(orderService.getOrderDetails(orderId, username));
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@AuthenticationPrincipal Jwt jwt,
                                            @RequestParam(required = false) String note,
                                            @RequestParam(required = false) String code,
                                            @RequestParam BigDecimal cartTotalPrice,
                                            @RequestParam BigDecimal discountTotalPrice) {
        String username = jwt.getSubject();
        orderService.addToOrder(username, note, code, cartTotalPrice, discountTotalPrice);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Integer orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
