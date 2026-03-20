package com.myproject.e_commerce.service.AdminService;

import com.myproject.e_commerce.constants.ProductStock;
import com.myproject.e_commerce.dto.*;
import com.myproject.e_commerce.entity.Authority;
import com.myproject.e_commerce.entity.Product;

import java.util.List;

public interface AdminService {
    AdminDashboardDTO AdminDashboard();
    List<ProductDashboardDTO> findAllProducts();
    List<AuthorityDTO> findAllAuthorities();
    List<ProductDashboardDTO> findAllProductsByCategory(Integer id);
    List<ProductDashboardDTO> findProductsByStock(ProductStock productStock);
    List<ProductDashboardDTO> searchProduct(String keyword);
    void changeBanner(String imageUrl);
    BannerDTO banner();
}
