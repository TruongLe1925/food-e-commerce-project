package com.myproject.e_commerce.service.AdminService;

import com.myproject.e_commerce.dto.ProductDashboardDTO;
import com.myproject.e_commerce.entity.Product;

import java.util.List;

public interface Admin {
    List<ProductDashboardDTO> getProducts(List<Product> products);
}
