package com.myproject.e_commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCategoryDTO {
    @NotBlank(message = "Tên danh muc không được để trống")
    @Size(min = 2, max = 100, message = "Tên danh muc phải từ 2 đến 100 ký tự")
    private String name;

    @NotBlank(message = "Đường dẫn ảnh không được để trống")
    @URL(message = "Đường dẫn ảnh không đúng định dạng URL")
    private String thumbnailUrl;

    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 10, max = 2000, message = "Mô tả phải có ít nhất 10 ký tự và tối đa 2000 ký tự")
    private String description;
}
