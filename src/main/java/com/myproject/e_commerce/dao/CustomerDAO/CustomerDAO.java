package com.myproject.e_commerce.dao.CustomerDAO;

import com.myproject.e_commerce.entity.User;

public interface CustomerDAO {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User getUserAndUserDetailsByUsername(String username);
}