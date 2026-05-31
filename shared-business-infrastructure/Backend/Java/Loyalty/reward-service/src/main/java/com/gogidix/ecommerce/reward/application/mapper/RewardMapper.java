package com.gogidix.ecommerce.reward.application.mapper;

import com.gogidix.ecommerce.reward.application.dto.*;
import com.gogidix.ecommerce.reward.domain.model.Reward;
import org.springframework.stereotype.Component;

@Component
public class RewardMapper {

    public RewardResponse toResponse(Reward entity) {
        if (entity == null) return null;
        return new RewardResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getRewardType(),
            entity.getPointsCost(),
            entity.getRedemptionCount(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Reward toEntity(CreateRewardRequest request) {
        Reward entity = new Reward();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setRewardType(request.rewardType());
        entity.setPointsCost(request.pointsCost());
        entity.setRedemptionCount(request.redemptionCount());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Reward entity, UpdateRewardRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.rewardType() != null) entity.setRewardType(request.rewardType());
        if (request.pointsCost() != null) entity.setPointsCost(request.pointsCost());
        if (request.redemptionCount() != null) entity.setRedemptionCount(request.redemptionCount());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
