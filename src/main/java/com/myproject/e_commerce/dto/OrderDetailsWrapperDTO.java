package com.myproject.e_commerce.dto;

import com.myproject.e_commerce.constants.DiscountType;
import com.myproject.e_commerce.constants.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsWrapperDTO {
    private List<OrderDetailsDTO> orderDetailsHistoryListDTO;
    private CustomerDetailDTO customerDetailDTO;
    private LocalDateTime orderDate;
    private Integer orderId;
    private String discountName;
    private DiscountType discountType;
    private BigDecimal grandTotalPrice;
    private StatusOrder status;
}
