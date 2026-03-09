package com.myproject.e_commerce.dto;

import com.myproject.e_commerce.constants.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProcessDTO {
    private Integer orderId;
    private String fullname;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private StatusOrder status;
}
