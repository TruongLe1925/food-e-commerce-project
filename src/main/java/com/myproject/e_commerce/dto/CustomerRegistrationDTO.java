package com.myproject.e_commerce.dto;

import com.myproject.e_commerce.contants.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegistrationDTO {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String confirmPassword;
}
