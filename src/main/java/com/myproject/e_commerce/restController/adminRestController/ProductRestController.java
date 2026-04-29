package com.myproject.e_commerce.restController.adminRestController;

import com.myproject.e_commerce.dto.AddProductDTO;
import com.myproject.e_commerce.dto.ProductDashboardDTO;
import com.myproject.e_commerce.service.FileService;
import com.myproject.e_commerce.service.ProductService.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/products")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class ProductRestController {
    private final ProductService productService;
    private final FileService fileService;

    public ProductRestController(ProductService productService, FileService fileService) {
        this.productService = productService;
        this.fileService = fileService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDashboardDTO> getProductById(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.findProductById(productId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable Integer productId,
                                            @Valid @RequestBody ProductDashboardDTO productDashboardDTO) {
        productService.updateProduct(productId, productDashboardDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(@Valid @RequestBody AddProductDTO addProductDTO) {
        productService.addProduct(addProductDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/image")
    public ResponseEntity<String> uploadProductImage(@PathVariable Integer productId,
                                                     @RequestParam("file") MultipartFile file) {
        String fileName = fileService.save(file);
        return ResponseEntity.ok(fileName);
    }
}
