package com.myproject.e_commerce.service.CustomerService;

import com.myproject.e_commerce.dto.CustomerDetailDTO;
import com.myproject.e_commerce.dto.CustomerRegistrationDTO;
import com.myproject.e_commerce.entity.CustomerDetails;

public interface CustomerService {
    void  save(CustomerRegistrationDTO customerRegistrationDTO);
    CustomerDetailDTO getCustomerDetailsByUsername(String username);
    void UpdateCustomerDetails(String username,CustomerDetailDTO customerDetailDTO);
    void DeleteCustomerById(String username);
}
