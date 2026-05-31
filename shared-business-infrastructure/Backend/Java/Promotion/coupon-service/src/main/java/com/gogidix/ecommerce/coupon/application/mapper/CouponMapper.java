package com.gogidix.ecommerce.coupon.application.mapper;

import com.gogidix.ecommerce.coupon.application.dto.*;
import com.gogidix.ecommerce.coupon.domain.model.Coupon;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public CouponResponse toResponse(Coupon entity) {
        if (entity == null) return null;
        return new CouponResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getCouponCode(),
            entity.getDiscountPercentage(),
            entity.getMaxUses(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Coupon toEntity(CreateCouponRequest request) {
        Coupon entity = new Coupon();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setCouponCode(request.couponCode());
        entity.setDiscountPercentage(request.discountPercentage());
        entity.setMaxUses(request.maxUses());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Coupon entity, UpdateCouponRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.couponCode() != null) entity.setCouponCode(request.couponCode());
        if (request.discountPercentage() != null) entity.setDiscountPercentage(request.discountPercentage());
        if (request.maxUses() != null) entity.setMaxUses(request.maxUses());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
