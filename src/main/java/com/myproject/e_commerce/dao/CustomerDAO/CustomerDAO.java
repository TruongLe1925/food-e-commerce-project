package com.myproject.e_commerce.dao.CustomerDAO;

public interface CustomerDAO {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}