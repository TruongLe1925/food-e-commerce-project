package com.myproject.e_commerce.restController.adminRestController;

import com.myproject.e_commerce.constants.ProductStock;
import com.myproject.e_commerce.constants.Role;
import com.myproject.e_commerce.dto.*;
import com.myproject.e_commerce.service.AdminService.AdminService;
import com.myproject.e_commerce.service.CustomerService.CustomerService;
import com.myproject.e_commerce.service.EmployeeService.EmployeeService;
import com.myproject.e_commerce.service.FileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRestController {
    private final AdminService adminService;
    private final FileService fileService;
    private final CustomerService customerService;
    private final EmployeeService employeeService;

    @Value("${page-size:10}")
    private int pageSize;

    public AdminRestController(AdminService adminService, FileService fileService, 
                               CustomerService customerService, EmployeeService employeeService) {
        this.adminService = adminService;
        this.fileService = fileService;
        this.customerService = customerService;
        this.employeeService = employeeService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDTO> getDashboard() {
        return ResponseEntity.ok(adminService.AdminDashboard());
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDashboardDTO>> getAllProducts() {
        return ResponseEntity.ok(adminService.findAllProducts());
    }

    @GetMapping("/products/category/{id}")
    public ResponseEntity<List<ProductDashboardDTO>> getProductsByCategory(@PathVariable Integer id) {
        return ResponseEntity.ok(adminService.findAllProductsByCategory(id));
    }

    @GetMapping("/products/stock/{stock}")
    public ResponseEntity<List<ProductDashboardDTO>> getProductsByStock(@PathVariable ProductStock stock) {
        return ResponseEntity.ok(adminService.findProductsByStock(stock));
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductDashboardDTO>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(adminService.searchProduct(keyword));
    }

    @GetMapping("/authorities")
    public ResponseEntity<List<AuthorityDTO>> getAllAuthorities() {
        return ResponseEntity.ok(adminService.findAllAuthorities());
    }

    @GetMapping("/banner")
    public ResponseEntity<BannerDTO> getBanner() {
        return ResponseEntity.ok(adminService.banner());
    }

    @PostMapping("/banner")
    public ResponseEntity<Void> changeBanner(@RequestParam("image") MultipartFile file) {
        String imageUrl = fileService.saveBanner(file);
        adminService.changeBanner(imageUrl);
        return ResponseEntity.ok().build();
    }

    // ==================== CUSTOMER MANAGEMENT ====================

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

    // ==================== EMPLOYEE MANAGEMENT ====================

    @GetMapping("/employees")
    public ResponseEntity<List<AuthorityDTO>> getAllEmployees() {
        return ResponseEntity.ok(adminService.findAllAuthorities());
    }

    @PostMapping("/employees")
    public ResponseEntity<Void> createEmployee(@Valid @RequestBody AuthorityDTO authorityDTO,
                                               @RequestParam Set<Role> roles) {
        employeeService.saveEmployee(authorityDTO, roles);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/employees/{username}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String username) {
        employeeService.deleteEmployee(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/employees/{username}")
    public ResponseEntity<AuthorityDTO> getEmployeeByUsername(@PathVariable String username) {
        return ResponseEntity.ok(employeeService.getUserByUsername(username));
    }

    @PutMapping("/employees/{username}")
    public ResponseEntity<Void> updateEmployee(@PathVariable String username,
                                             @RequestParam String newPassword) {
        employeeService.updateEmployee(username, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/employees/{username}/toggle-admin")
    public ResponseEntity<Void> toggleAdminRole(@PathVariable String username) {
        employeeService.toggleAdminRole(username);
        return ResponseEntity.ok().build();
    }
}
