package com.myproject.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDetailDTO {
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String enabled;
}
