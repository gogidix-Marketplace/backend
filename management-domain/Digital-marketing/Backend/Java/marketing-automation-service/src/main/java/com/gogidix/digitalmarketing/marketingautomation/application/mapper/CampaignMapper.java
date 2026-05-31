package com.gogidix.digitalmarketing.marketingautomation.application.mapper;

import com.gogidix.digitalmarketing.marketingautomation.domain.model.Campaign;
import com.gogidix.digitalmarketing.marketingautomation.application.dto.CampaignRequestDto;
import com.gogidix.digitalmarketing.marketingautomation.application.dto.CampaignResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CampaignMapper {

    public Campaign toEntity(CampaignRequestDto dto) {
        return Campaign.builder()
            .tenantId(dto.getTenantId())
            .name(dto.getName())
            .type(dto.getType())
            .status(dto.getStatus())
            .triggerType(dto.getTriggerType())
            .triggerCondition(dto.getTriggerCondition())
            .startDate(dto.getStartDate())
            .endDate(dto.getEndDate())
            .isActive(dto.getIsActive())
            .build();
    }

    public CampaignResponseDto toResponseDto(Campaign entity) {
        return CampaignResponseDto.builder()
            .id(entity.getId())
            .tenantId(entity.getTenantId())
            .name(entity.getName())
            .type(entity.getType())
            .status(entity.getStatus())
            .triggerType(entity.getTriggerType())
            .triggerCondition(entity.getTriggerCondition())
            .startDate(entity.getStartDate())
            .endDate(entity.getEndDate())
            .isActive(entity.getIsActive())
            .createdBy(entity.getCreatedBy())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}