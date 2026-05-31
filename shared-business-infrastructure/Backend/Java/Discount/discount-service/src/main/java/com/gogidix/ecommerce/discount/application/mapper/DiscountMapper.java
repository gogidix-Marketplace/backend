package com.gogidix.ecommerce.discount.application.mapper;

import com.gogidix.ecommerce.discount.application.dto.*;
import com.gogidix.ecommerce.discount.domain.model.Discount;
import org.springframework.stereotype.Component;

@Component
public class DiscountMapper {

    public DiscountResponse toResponse(Discount entity) {
        if (entity == null) return null;
        return new DiscountResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getDiscountPercentage(),
            entity.getDiscountAmount(),
            entity.getMinOrderValue(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Discount toEntity(CreateDiscountRequest request) {
        Discount entity = new Discount();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setDiscountPercentage(request.discountPercentage());
        entity.setDiscountAmount(request.discountAmount());
        entity.setMinOrderValue(request.minOrderValue());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Discount entity, UpdateDiscountRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.discountPercentage() != null) entity.setDiscountPercentage(request.discountPercentage());
        if (request.discountAmount() != null) entity.setDiscountAmount(request.discountAmount());
        if (request.minOrderValue() != null) entity.setMinOrderValue(request.minOrderValue());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
