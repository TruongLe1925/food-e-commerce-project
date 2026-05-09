package com.myproject.e_commerce.restController.adminRestController;

import com.myproject.e_commerce.constants.Role;
import com.myproject.e_commerce.dto.AuthorityDTO;
import com.myproject.e_commerce.service.AdminService.AdminService;
import com.myproject.e_commerce.service.EmployeeService.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class ManagerEmployeeRestController {
    private final EmployeeService employeeService;
    private final AdminService adminService;
    public ManagerEmployeeRestController(EmployeeService employeeService,AdminService adminService) {
        this.employeeService = employeeService;
        this.adminService = adminService;
    }
    @GetMapping("/employees")
    public ResponseEntity<List<AuthorityDTO>> getAllEmployees() {
        return ResponseEntity.ok(adminService.findAllAuthorities());
    }

    @PostMapping("/employees")
    public ResponseEntity<Void> createEmployee(@Valid @RequestBody AuthorityDTO authorityDTO,
                                               @RequestParam Set<Role> roles) {
        employeeService.saveEmployee(authorityDTO, roles);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/employees/{username}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String username) {
        employeeService.deleteEmployee(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/employees/{username}")
    public ResponseEntity<AuthorityDTO> getEmployeeByUsername(@PathVariable String username) {
        return ResponseEntity.ok(employeeService.getUserByUsername(username));
    }

    @PutMapping("/employees/{username}")
    public ResponseEntity<Void> updateEmployee(@PathVariable String username,
                                               @RequestParam String newPassword) {
        employeeService.updateEmployee(username, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/employees/{username}/toggle-admin")
    public ResponseEntity<Void> toggleAdminRole(@PathVariable String username) {
        employeeService.toggleAdminRole(username);
        return ResponseEntity.ok().build();
    }
}
