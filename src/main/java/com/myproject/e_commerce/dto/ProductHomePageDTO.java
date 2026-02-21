package com.myproject.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductHomePageDTO {
    private String productName;
    private BigDecimal productPrice;
    private String imageUrl;
}
