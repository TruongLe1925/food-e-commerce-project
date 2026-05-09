package com.myproject.e_commerce.restController.userRestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.e_commerce.dto.CustomerDetailDTO;
import com.myproject.e_commerce.service.CustomerService.CustomerService;
import com.myproject.e_commerce.service.OrderService.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('CUSTOMER')")
public class UserRestController {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final JsonMapper jsonMapper;

    public UserRestController(CustomerService customerService, OrderService orderService, JsonMapper jsonMapper) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.jsonMapper = jsonMapper;
    }
    @GetMapping("/customerdetail")
    public ResponseEntity<CustomerDetailDTO> customerDetail(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        return ResponseEntity.ok(customerService.getCustomerDetailsByUsername(username));
    }
    @PatchMapping("/profile")
    public ResponseEntity<CustomerDetailDTO> updateProfile(@AuthenticationPrincipal Jwt jwt,
                                           @RequestBody Map<String, Object> patchPayload) {
        String username = jwt.getSubject();
        CustomerDetailDTO customer = customerService.getCustomerDetailsByUsername(username);
        if (customer == null) {
            throw new RuntimeException("Customer not found");
        }
        CustomerDetailDTO patchedCustomer = jsonMapper.updateValue(customer, patchPayload);
        customerService.updateCustomerDetails(username, patchedCustomer);
        return ResponseEntity.ok(patchedCustomer);
    }

}
