package com.gogidix.ecommerce.loyalty.application.mapper;

import com.gogidix.ecommerce.loyalty.application.dto.*;
import com.gogidix.ecommerce.loyalty.domain.model.Loyalty;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyMapper {

    public LoyaltyResponse toResponse(Loyalty entity) {
        if (entity == null) return null;
        return new LoyaltyResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getPointsBalance(),
            entity.getTierLevel(),
            entity.getMemberSince(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Loyalty toEntity(CreateLoyaltyRequest request) {
        Loyalty entity = new Loyalty();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setPointsBalance(request.pointsBalance());
        entity.setTierLevel(request.tierLevel());
        entity.setMemberSince(request.memberSince());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Loyalty entity, UpdateLoyaltyRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.pointsBalance() != null) entity.setPointsBalance(request.pointsBalance());
        if (request.tierLevel() != null) entity.setTierLevel(request.tierLevel());
        if (request.memberSince() != null) entity.setMemberSince(request.memberSince());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
