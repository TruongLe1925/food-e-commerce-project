package com.myproject.e_commerce.service.CustomerService;

import com.myproject.e_commerce.dto.CustomerDetailDTO;
import com.myproject.e_commerce.dto.CustomerRegistrationDTO;
import com.myproject.e_commerce.entity.CustomerDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerService {
    void  save(CustomerRegistrationDTO customerRegistrationDTO);
    CustomerDetailDTO getCustomerDetailsByUsername(String username);
    List<CustomerDetailDTO> findAllCustomer();
    void updateCustomerDetails(String username, CustomerDetailDTO customerDetailDTO);
    void deleteCustomerById(String username);
    void updateCustomerStatus(String username);
    CustomerDetailDTO getCustomerDetails(Integer id);
    List<CustomerDetailDTO> searchCustomer(String keyword);
    Page<CustomerDetailDTO> searchCustomer(String keyword, Pageable pageable);
    Page<CustomerDetailDTO> findAllCustomer(Pageable pageable);
}
