package com.myproject.e_commerce.service.CustomerService;
import com.myproject.e_commerce.constants.Role;
import com.myproject.e_commerce.dao.CustomerDAO.CustomerDAO;
import com.myproject.e_commerce.dto.CustomerDetailDTO;
import com.myproject.e_commerce.dto.CustomerRegistrationDTO;
import com.myproject.e_commerce.entity.Authority;
import com.myproject.e_commerce.entity.CustomerDetails;
import com.myproject.e_commerce.entity.User;
import com.myproject.e_commerce.repository.AuthorityRepository;
import com.myproject.e_commerce.repository.CustomerDetailsRepository;
import com.myproject.e_commerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
            throw new RuntimeException("Username already exists");
        }
        if (customerDAO.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
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
        userRepository.deleteById(username);
    }
}
