package com.myproject.e_commerce.restController.adminRestController;

import com.myproject.e_commerce.dto.PromotionDTO;
import com.myproject.e_commerce.service.PromotionService.PromotionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/promotions")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class PromotionRestController {
    private final PromotionService promotionService;

    public PromotionRestController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    public ResponseEntity<List<PromotionDTO>> getAllPromotions() {
        return ResponseEntity.ok(promotionService.findAllPromotion());
    }

    @PostMapping
    public ResponseEntity<Void> addPromotion(@Valid @RequestBody PromotionDTO promotionDTO) {
        promotionService.addPromotion(promotionDTO);
        return ResponseEntity.ok().build();
    }
}
