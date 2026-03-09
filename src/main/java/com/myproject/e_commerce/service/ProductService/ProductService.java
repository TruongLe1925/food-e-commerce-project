package com.myproject.e_commerce.service.ProductService;

import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.dto.ProductHomePageDTO;
import com.myproject.e_commerce.entity.Product;

import java.util.List;

public interface ProductService {
    List<ProductHomePageDTO> findAllProducts();
}
