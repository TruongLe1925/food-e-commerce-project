package com.myproject.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InCartDTO {
    private Integer cartItemsId;
    private String username;
    private String productName;
    private int quantity;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private BigDecimal totalPrice;
}
