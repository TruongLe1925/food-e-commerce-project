package com.myproject.e_commerce.restController.UserRestController;

import com.myproject.e_commerce.dto.CartDTO;
import com.myproject.e_commerce.dto.ProductHomePageDTO;
import com.myproject.e_commerce.service.CartService.CartService;
import com.myproject.e_commerce.service.ProductService.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/home")
public class UserRestController {
    private final ProductService  productService;
    private final CartService cartService;
    private final JsonMapper jsonMapper;
    public UserRestController(CartService cartService,ProductService productService, JsonMapper jsonMapper) {
        this.cartService = cartService;
        this.jsonMapper = jsonMapper;
        this.productService = productService;
    }
    @GetMapping("/product")
    public List<ProductHomePageDTO> productList() {
        return productService.findAllProducts();
    }
    @PostMapping("/addToCart")
    public ResponseEntity<Void> addtoCart(@RequestBody CartDTO cartdto, Principal principal){
        String username = principal.getName();
        cartdto.setUsername(username);
        cartService.addCart(cartdto);
        return ResponseEntity.ok().build();
    }

}
