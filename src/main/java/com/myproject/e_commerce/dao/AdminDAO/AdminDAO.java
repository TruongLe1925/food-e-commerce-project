package com.myproject.e_commerce.dao.AdminDAO;

import com.myproject.e_commerce.entity.Authority;
import com.myproject.e_commerce.entity.Product;
import com.myproject.e_commerce.entity.User;

import java.util.List;

public interface AdminDAO {
    long countAllUsers();
    long countAllOrders();
    long countAllProducts();
    List<User> getAllUser();

}
