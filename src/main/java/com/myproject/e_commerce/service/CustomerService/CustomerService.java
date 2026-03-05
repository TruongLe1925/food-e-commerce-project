package com.myproject.e_commerce.service.CustomerService;

import com.myproject.e_commerce.dto.CustomerDetailDTO;
import com.myproject.e_commerce.dto.CustomerRegistrationDTO;
import com.myproject.e_commerce.entity.CustomerDetails;
import org.springframework.transaction.annotation.Transactional;

public interface CustomerService {
    void  save(CustomerRegistrationDTO customerRegistrationDTO);
    CustomerDetailDTO getCustomerDetailsByUsername(String username);
    void updateCustomerDetails(String username, CustomerDetailDTO customerDetailDTO);
    void deleteCustomerById(String username);
}
