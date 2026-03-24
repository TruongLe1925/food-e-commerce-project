package com.myproject.e_commerce.service.EmployeeService;

import com.myproject.e_commerce.constants.Role;
import com.myproject.e_commerce.dao.CustomerDAO.CustomerDAO;
import com.myproject.e_commerce.dto.AuthorityDTO;
import com.myproject.e_commerce.entity.Authority;
import com.myproject.e_commerce.entity.User;
import com.myproject.e_commerce.repository.AuthorityRepository;
import com.myproject.e_commerce.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDAO customerDAO;
    private final AuthorityRepository authorityRepository;
    public EmployeeServiceImpl(AuthorityRepository authorityRepository,CustomerDAO customerDAO,PasswordEncoder passwordEncoder,UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.customerDAO = customerDAO;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    @Transactional
    @Override
    public void saveEmployee(AuthorityDTO authorityDTO, Set<Role> roles) {
        User user = User.builder()
                .username(authorityDTO.getUsername())
                .password(passwordEncoder.encode(authorityDTO.getPassword()))
                .enabled(true)
                .build();
        Set<Authority> authorities = roles.stream().map(auth -> Authority.builder()
                        .authority(auth)
                        .user(user)
                        .build())
                        .collect(Collectors.toSet());
        user.setAuthorities(authorities);
        userRepository.save(user);
    }
    @Transactional
    @Override
    public void deleteEmployee(String username) {
        userRepository.deleteById(username);
    }
    @Transactional
    @Override
    public void updateEmployee(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        userRepository.save(user);
    }

    @Override
    public AuthorityDTO getUserByUsername(String username) {
        User user = customerDAO.getEmployeeByUsername(username);
        AuthorityDTO authorityDTO = AuthorityDTO.builder()
                .username(user.getUsername())
                .enable(user.isEnabled()==true?"active":"inactive")
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
        return authorityDTO;
    }
    @Transactional
    @Override
    public void toggleAdminRole(String username) {
        User user = customerDAO.getEmployeeByUsername(username);
        Set<Authority> authorities = user.getAuthorities();
        Authority existingAuth = authorities.stream()
                .filter(a -> a.getAuthority().equals(Role.ROLE_ADMIN))
                .findFirst()
                .orElse(null);
        if (existingAuth != null) {
            authorityRepository.delete(existingAuth);
            authorities.remove(existingAuth);
        } else {
            Authority newAuth = Authority.builder()
                    .authority(Role.ROLE_ADMIN)
                    .user(user)
                    .build();
            authorities.add(newAuth);
        }
        userRepository.save(user);
    }
}
