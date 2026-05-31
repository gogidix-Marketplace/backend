package com.gogidix.digitalmarketing.integration.application.mapper;

import com.gogidix.digitalmarketing.integration.domain.model.Integration;
import com.gogidix.digitalmarketing.integration.application.dto.IntegrationRequestDto;
import com.gogidix.digitalmarketing.integration.application.dto.IntegrationResponseDto;
import org.springframework.stereotype.Component;

@Component
public class IntegrationMapper {

    public Integration toEntity(IntegrationRequestDto dto) {
        return Integration.builder()
            .tenantId(dto.getTenantId())
            .name(dto.getName())
            .type(dto.getType())
            .provider(dto.getProvider())
            .status(dto.getStatus())
            .webhookUrl(dto.getWebhookUrl())
            .apiKey(dto.getApiKey())
            .configuration(dto.getConfiguration())
            .build();
    }

    public IntegrationResponseDto toResponseDto(Integration entity) {
        return IntegrationResponseDto.builder()
            .id(entity.getId())
            .tenantId(entity.getTenantId())
            .name(entity.getName())
            .type(entity.getType())
            .provider(entity.getProvider())
            .status(entity.getStatus())
            .webhookUrl(entity.getWebhookUrl())
            .apiKey(entity.getApiKey())
            .configuration(entity.getConfiguration())
            .createdBy(entity.getCreatedBy())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}