package com.myproject.e_commerce.restController;

import com.myproject.e_commerce.dto.ProductHomePageDTO;
import com.myproject.e_commerce.service.ProductService.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class UserRestController {
    private final ProductService  productService;
    public UserRestController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/product")
    public List<ProductHomePageDTO> productList(){
        return productService.findAllProducts();
    }

}
