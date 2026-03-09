package com.myproject.e_commerce.dto;
import com.myproject.e_commerce.constants.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    Integer orderId;
    LocalDateTime orderDate;
    BigDecimal totalPrice;
    StatusOrder status;
    String note;
}
