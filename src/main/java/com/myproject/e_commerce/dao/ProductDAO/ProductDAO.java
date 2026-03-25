package com.myproject.e_commerce.dao.ProductDAO;

import com.myproject.e_commerce.entity.Product;

import java.util.List;

public interface ProductDAO {
    List<Product> getProducts();
    List<Product> getProductsByCategory(Integer id);
    List<Product> getProductsByInStock();
    List<Product> getProductsByOutOfStock();
    List<Product> SearchProduct(String keyword);
    Product getProductById(Integer id);
}
