package com.myproject.e_commerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDetailDTO {
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private BigDecimal totalExpenditure;
    private String enabled;
    private Integer id;
}
