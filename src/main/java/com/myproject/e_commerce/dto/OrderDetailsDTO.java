package com.myproject.e_commerce.dto;

import com.myproject.e_commerce.constants.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDTO {
    private String productName;
    private int quantity;
    private String description;
    private BigDecimal originalPrice;
    private String imageUrl;
    private BigDecimal discountPromotion;
    private BigDecimal totalPrice;
    private String note;

}
