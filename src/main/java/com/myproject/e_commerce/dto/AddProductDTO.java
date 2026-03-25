package com.myproject.e_commerce.dto;

import com.myproject.e_commerce.entity.Category;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductDTO {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 5, max = 150, message = "Tên sản phẩm phải từ 5 đến 150 ký tự")
    private String productName;

    @NotNull(message = "Giá bán không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá bán phải lớn hơn 0")
    @Digits(integer = 12, fraction = 2, message = "Giá tiền không đúng định dạng (tối đa 2 số thập phân)")
    private BigDecimal price;

    @Min(value = 0, message = "Số lượng trong kho không được âm")
    private int quantity;

    @NotBlank(message = "Ảnh thu nhỏ (thumbnail) là bắt buộc")
    @URL(message = "Thumbnail phải là một đường dẫn URL hợp lệ")
    private String thumbnailUrl;

    @NotBlank(message = "Ảnh chi tiết là bắt buộc")
    @URL(message = "Image URL phải là một đường dẫn URL hợp lệ")
    private String imageUrl;

    private List<Integer> categories;

    @NotBlank(message = "Mô tả sản phẩm không được để trống")
    @Size(min = 20, message = "Mô tả sản phẩm phải có ít nhất 20 ký tự để khách hàng dễ hình dung")
    private String description;
}
