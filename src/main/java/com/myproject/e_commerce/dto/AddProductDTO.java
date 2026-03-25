package com.myproject.e_commerce.dto;

import com.myproject.e_commerce.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductDTO {
    private String productName;
    private BigDecimal price;
    private int quantity;
    private String thumbnailUrl;
    private String imageUrl;
    private List<Integer> categories;
    private String description;
}
