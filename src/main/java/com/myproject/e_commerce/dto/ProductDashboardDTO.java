package com.myproject.e_commerce.dto;

import com.myproject.e_commerce.constants.ProductStock;
import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.entity.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDashboardDTO {
    private String productName;
    private BigDecimal price;
    private LocalDateTime createdDate;
    @Enumerated(EnumType.STRING)
    private ProductStock productStock;
    private Integer productId;
    private String imageUrl;
    private String thumbnailUrl;
    private BigDecimal grossIncome;
    private String description;
    private int stock;
    private List<CategoryDTO> categories;
}
