package com.myproject.e_commerce.service.EmployeeService;

import com.myproject.e_commerce.constants.Role;
import com.myproject.e_commerce.dto.AuthorityDTO;
import com.myproject.e_commerce.entity.User;

import java.util.Set;

public interface EmployeeService {
    void saveEmployee(AuthorityDTO authorityDTO, Set<Role> roles);
    void deleteEmployee(String username);
    void updateEmployee(String username, String newPassword);
    AuthorityDTO getUserByUsername(String username);
    void toggleAdminRole(String username);
}
