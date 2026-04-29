package com.myproject.e_commerce.restController.adminRestController;

import com.myproject.e_commerce.constants.ProductStock;
import com.myproject.e_commerce.dto.*;
import com.myproject.e_commerce.service.AdminService.AdminService;
import com.myproject.e_commerce.service.FileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRestController {
    private final AdminService adminService;
    private final FileService fileService;

    public AdminRestController(AdminService adminService, FileService fileService) {
        this.adminService = adminService;
        this.fileService = fileService;
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
}
