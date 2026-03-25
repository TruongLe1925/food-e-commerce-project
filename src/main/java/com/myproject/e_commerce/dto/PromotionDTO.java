package com.myproject.e_commerce.dto;

import com.myproject.e_commerce.constants.DiscountType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDTO {
    @NotBlank(message = "Tên khuyến mãi không được để trống")
    @Size(min = 3, max = 50, message = "Tên khuyến mãi phải từ 3 đến 50 ký tự")
    private String promotionName;

    @NotNull(message = "Loại giảm giá là bắt buộc (PERCENTAGE hoặc FIXED)")
    private DiscountType discountType;

    @NotNull(message = "Giá trị giảm giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá trị giảm giá phải lớn hơn 0")
    private BigDecimal discountValue;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @FutureOrPresent(message = "Ngày bắt đầu phải là hiện tại hoặc tương lai")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotBlank(message = "Trạng thái không được để trống")
    private String status;
}
