package com.myproject.e_commerce.restController.employeeRestController;

import com.myproject.e_commerce.constants.Role;
import com.myproject.e_commerce.dto.AuthorityDTO;
import com.myproject.e_commerce.service.EmployeeService.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/employee")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class EmployeeRestController {
    private final EmployeeService employeeService;

    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Void> createEmployee(@Valid @RequestBody AuthorityDTO authorityDTO,
                                               @RequestParam Set<Role> roles) {
        employeeService.saveEmployee(authorityDTO, roles);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String username) {
        employeeService.deleteEmployee(username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{username}/password")
    public ResponseEntity<Void> updateEmployeePassword(@PathVariable String username,
                                                       @RequestParam String newPassword) {
        employeeService.updateEmployee(username, newPassword);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<AuthorityDTO> getEmployeeByUsername(@PathVariable String username) {
        return ResponseEntity.ok(employeeService.getUserByUsername(username));
    }

    @PutMapping("/{username}/toggle-admin")
    public ResponseEntity<Void> toggleAdminRole(@PathVariable String username) {
        employeeService.toggleAdminRole(username);
        return ResponseEntity.ok().build();
    }
}
