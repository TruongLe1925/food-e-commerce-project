package com.myproject.e_commerce.service.PromotionService;

import com.myproject.e_commerce.dto.PromotionDTO;
import com.myproject.e_commerce.entity.Promotion;

import java.util.List;

public interface PromotionService {
    List<PromotionDTO> findAllPromotion();
    void addPromotion(PromotionDTO promotionDTO);
}
