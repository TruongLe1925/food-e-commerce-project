package com.myproject.e_commerce.service.AdminService;

import com.myproject.e_commerce.constants.ProductStock;
import com.myproject.e_commerce.dto.ProductDashboardDTO;
import com.myproject.e_commerce.entity.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class AdminImpl implements Admin {
    @Override
    public List<ProductDashboardDTO> getProducts(List<Product> products) {
        List<Product> productList;
        ProductStock productStock;
        return products.stream().map(
                p -> ProductDashboardDTO.builder()
                        .productId(p.getId())
                        .productName(p.getName())
                        .price(p.getPrice())
                        .imageUrl(p.getImageUrl())
                        .stock(p.getStock())
                        .productStock(p.getStock() > 0 ? ProductStock.IN_STOCK : ProductStock.OUT_OF_STOCK)
                        .build()
        ).toList();
    }
}
