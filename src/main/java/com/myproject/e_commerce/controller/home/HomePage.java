package com.myproject.e_commerce.controller.home;

import com.myproject.e_commerce.dto.BannerDTO;
import com.myproject.e_commerce.dto.CartDTO;
import com.myproject.e_commerce.dto.ProductHomePageDTO;
import com.myproject.e_commerce.service.AdminService.AdminService;
import com.myproject.e_commerce.service.CartService.CartService;
import com.myproject.e_commerce.service.ProductService.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.security.Principal;
import java.util.List;

@Controller
public class HomePage {
    private final ProductService productService;
    private final CartService cartService;
    private final AdminService adminService;
    public HomePage(AdminService adminService,ProductService productService,CartService cartService) {
        this.adminService = adminService;
        this.cartService = cartService;
        this.productService = productService;
    }
    @GetMapping("/")
    public String home(Model model) {
        List<ProductHomePageDTO> productHomePageDTOS = productService.findAllProducts();
        BannerDTO bannerDTO = adminService.banner();
        model.addAttribute("bannerDTO",bannerDTO);
        model.addAttribute("product", productHomePageDTOS);
        return "shop-homepage/index";
    }

    @PostMapping("/addToCart")
    @ResponseBody
    public String addToCart(Principal principal, CartDTO cartDTO, @RequestParam("quantity") Integer quantity
            , @RequestParam("productName") String productName) {
        cartDTO.setUsername(principal.getName());
        cartDTO.setQuantity(quantity);
        cartDTO.setProductName(productName);
        cartService.addCart(cartDTO);
        return "";
    }

}
