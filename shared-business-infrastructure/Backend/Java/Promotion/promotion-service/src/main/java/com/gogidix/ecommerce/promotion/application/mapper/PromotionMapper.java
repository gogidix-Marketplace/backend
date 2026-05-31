package com.gogidix.ecommerce.promotion.application.mapper;

import com.gogidix.ecommerce.promotion.application.dto.*;
import com.gogidix.ecommerce.promotion.domain.model.Promotion;
import org.springframework.stereotype.Component;

@Component
public class PromotionMapper {

    public PromotionResponse toResponse(Promotion entity) {
        if (entity == null) return null;
        return new PromotionResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getPromotionCode(),
            entity.getPromotionType(),
            entity.getDiscountValue(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Promotion toEntity(CreatePromotionRequest request) {
        Promotion entity = new Promotion();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setPromotionCode(request.promotionCode());
        entity.setPromotionType(request.promotionType());
        entity.setDiscountValue(request.discountValue());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Promotion entity, UpdatePromotionRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.promotionCode() != null) entity.setPromotionCode(request.promotionCode());
        if (request.promotionType() != null) entity.setPromotionType(request.promotionType());
        if (request.discountValue() != null) entity.setDiscountValue(request.discountValue());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
