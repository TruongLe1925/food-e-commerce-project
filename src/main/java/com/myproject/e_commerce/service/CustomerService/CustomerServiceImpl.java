package com.myproject.e_commerce.service.CustomerService;
import com.myproject.e_commerce.constants.Role;
import com.myproject.e_commerce.dao.CustomerDAO.CustomerDAO;
import com.myproject.e_commerce.dto.CustomerDetailDTO;
import com.myproject.e_commerce.dto.CustomerRegistrationDTO;
import com.myproject.e_commerce.entity.Authority;
import com.myproject.e_commerce.entity.CustomerDetails;
import com.myproject.e_commerce.entity.Orders;
import com.myproject.e_commerce.entity.User;
import com.myproject.e_commerce.exception.exception.EmailAlreadyExistsException;
import com.myproject.e_commerce.exception.exception.PasswordMismatchException;
import com.myproject.e_commerce.exception.exception.UsernameAlreadyExistsException;
import com.myproject.e_commerce.repository.AuthorityRepository;
import com.myproject.e_commerce.repository.CustomerDetailsRepository;
import com.myproject.e_commerce.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final CustomerDetailsRepository customerDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDAO customerDAO;
    public CustomerServiceImpl(CustomerDetailsRepository customerDetailsRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, AuthorityRepository authorityRepository, CustomerDAO customerDAO) {
        this.customerDAO=customerDAO;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.customerDetailsRepository = customerDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    @Override
    public void save(CustomerRegistrationDTO dto) {
        if (customerDAO.existsByUsername(dto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        if (customerDAO.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }
        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .enabled(true)
                .build();
        Authority authority = Authority.builder()
                .authority(Role.ROLE_CUSTOMER)
                .build();
        CustomerDetails customerDetails = CustomerDetails.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .build();
        authority.setUser(user);
        customerDetails.setUser(user);
        user.addAuthority(authority);
        user.setCustomerDetails(customerDetails);
        userRepository.save(user);

    }

    @Override
    public CustomerDetailDTO getCustomerDetailsByUsername(String username) {
        User user = customerDAO.getUserAndUserDetailsByUsername(username);
        CustomerDetailDTO customerDetailsDTO = CustomerDetailDTO.builder()
                .fullName(user.getCustomerDetails().getFullName())
                .email(user.getCustomerDetails().getEmail())
                .phoneNumber(user.getCustomerDetails().getPhoneNumber())
                .address(user.getCustomerDetails().getAddress())
                .build();
        return customerDetailsDTO;
    }

    @Transactional
    @Override
    public void updateCustomerDetails(String username, CustomerDetailDTO customerDetailDTO) {
        User user = customerDAO.getUserAndUserDetailsByUsername(username);
        CustomerDetails customerDetails = user.getCustomerDetails();
        customerDetails.setFullName(customerDetailDTO.getFullName());
        customerDetails.setEmail(customerDetailDTO.getEmail());
        customerDetails.setPhoneNumber(customerDetailDTO.getPhoneNumber());
        customerDetails.setAddress(customerDetailDTO.getAddress());
        user.setCustomerDetails(customerDetails);
        userRepository.save(user);
    }
    @Transactional
    @Override
    public void deleteCustomerById(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user.isEnabled() == true) {
            return;
        }
        user.setEnabled(false);
        userRepository.save(user);
    }
    @Transactional
    @Override
    public void updateCustomerStatus(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    @Override
    public CustomerDetailDTO getCustomerDetails(Integer id) {
        CustomerDetails customerDetails = customerDetailsRepository.findById(id).orElse(null);
        List<Orders> orders = customerDetails.getOrders();
        BigDecimal totalExpenditure = orders.stream()
                .filter(order -> order.getOrderDetails() != null)
                .flatMap(order -> order.getOrderDetails().stream())
                .map(detail -> {
                    BigDecimal price = detail.getDiscountPrice() != null ? detail.getDiscountPrice() : BigDecimal.ZERO;
                    return price;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        CustomerDetailDTO customerDetailDTO = CustomerDetailDTO.builder()
                .email(customerDetails.getEmail())
                .fullName(customerDetails.getFullName())
                .totalExpenditure(totalExpenditure)
                .phoneNumber(customerDetails.getPhoneNumber())
                .address(customerDetails.getAddress())
                .build();
        return customerDetailDTO;
    }

    @Override
    public List<CustomerDetailDTO> searchCustomer(String keyword) {
        String lowercase = keyword.toLowerCase();
        List<CustomerDetails> customerDetails = customerDAO.searchCustomer(lowercase);
        return customerDetails.stream().map(cus -> CustomerDetailDTO.builder()
                        .username(cus.getUser().getUsername())
                        .email(cus.getEmail())
                        .id(cus.getId())
                        .fullName(cus.getFullName())
                        .enabled(cus.getUser().isEnabled() == true?"active":"inactive")
                        .phoneNumber(cus.getFullName())
                        .address(cus.getAddress())
                        .build())
                .toList();
    }

    @Override
    public Page<CustomerDetailDTO> searchCustomer(String keyword, Pageable pageable) {
        return customerDetailsRepository.findByKeyword(keyword, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public Page<CustomerDetailDTO> findAllCustomer(Pageable pageable) {
        return customerDetailsRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    private CustomerDetailDTO convertToDTO(CustomerDetails cus) {
        return CustomerDetailDTO.builder()
                .username(cus.getUser().getUsername())
                .email(cus.getEmail())
                .id(cus.getId())
                .fullName(cus.getFullName())
                .enabled(cus.getUser().isEnabled() ? "active" : "inactive")
                .phoneNumber(cus.getPhoneNumber())
                .address(cus.getAddress())
                .build();
    }
    @Override
    public List<CustomerDetailDTO> findAllCustomer() {
        List<CustomerDetails> customerDetails = customerDAO.findAllCustomerDetails();
        return  customerDetails.stream().map(cus -> CustomerDetailDTO.builder()
                        .username(cus.getUser().getUsername())
                        .email(cus.getEmail())
                        .id(cus.getId())
                        .fullName(cus.getFullName())
                        .enabled(cus.getUser().isEnabled() == true?"active":"inactive")
                        .phoneNumber(cus.getPhoneNumber())
                        .address(cus.getAddress())
                        .build())
                .toList();
    }

}
