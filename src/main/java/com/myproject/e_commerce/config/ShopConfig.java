package com.myproject.e_commerce.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ShopConfig {
    @Value("${shop.name}")
    private String shopName;
    @Value("${shop.slogan}")
    private String slogan;
}
