package com.myproject.e_commerce.restController.adminRestController;
import com.myproject.e_commerce.dto.CustomerDetailDTO;
import com.myproject.e_commerce.service.CustomerService.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class CustomerRestController {
    @Value("${page-size:10}")
    private int pageSize;
    private final CustomerService customerService;

    public CustomerRestController (CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public ResponseEntity<Page<CustomerDetailDTO>> getAllCustomers(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<CustomerDetailDTO> customerPage;

        if (keyword != null && !keyword.isEmpty()) {
            customerPage = customerService.searchCustomer(keyword, pageable);
        } else {
            customerPage = customerService.findAllCustomer(pageable);
        }
        return ResponseEntity.ok(customerPage);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDetailDTO> getCustomerById(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.getCustomerDetails(id));
    }

    @PostMapping("/customers/{username}/toggle-status")
    public ResponseEntity<Void> toggleCustomerStatus(@PathVariable String username) {
        customerService.updateCustomerStatus(username);
        return ResponseEntity.ok().build();
    }
}
