package com.myproject.e_commerce.dao.CustomerDAO;

import com.myproject.e_commerce.dto.CustomerDetailDTO;
import com.myproject.e_commerce.entity.CustomerDetails;
import com.myproject.e_commerce.entity.User;

import java.util.List;

public interface CustomerDAO {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User getUserAndUserDetailsByUsername(String username);
    List<CustomerDetails> findAllCustomerDetails();
    CustomerDetails findCustomerDetailsById(Integer id);
    List<CustomerDetails> searchCustomer(String keyword);
    User getEmployeeByUsername(String username);
}