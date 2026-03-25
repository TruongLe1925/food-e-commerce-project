package com.myproject.e_commerce.service.PromotionService;

import com.myproject.e_commerce.dto.PromotionDTO;
import com.myproject.e_commerce.entity.Promotion;
import com.myproject.e_commerce.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
@Service
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    public  PromotionServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }
    @Override
    public List<PromotionDTO> findAllPromotion() {
        List<Promotion> promotions = promotionRepository.findAll();
        LocalDate now = LocalDate.now();
        return promotions.stream().map(pro -> PromotionDTO.builder()
                .promotionName(pro.getName())
                .discountType(pro.getDiscountType())
                .discountValue(pro.getDiscountValue())
                .startDate(pro.getStartDate())
                .endDate(pro.getEndDate())
                .status(now.isBefore(pro.getEndDate())?"Đang Khuyến Mãi":"Mã Đã Hết Hạn")
                .build()
        ).toList();
    }
    @Transactional
    @Override
    public void addPromotion(PromotionDTO promotionDTO) {
        Promotion promotion = Promotion.builder()
                .name(promotionDTO.getPromotionName())
                .discountType(promotionDTO.getDiscountType())
                .discountValue(promotionDTO.getDiscountValue())
                .startDate(promotionDTO.getStartDate())
                .endDate(promotionDTO.getEndDate())
                .build();
        promotionRepository.save(promotion);
    }
}
