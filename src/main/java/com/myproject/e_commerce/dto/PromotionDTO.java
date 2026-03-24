package com.myproject.e_commerce.dto;

import com.myproject.e_commerce.constants.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDTO {
    private String promotionName;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private LocalDate startDate;
    private String status;
    private LocalDate endDate;
}
