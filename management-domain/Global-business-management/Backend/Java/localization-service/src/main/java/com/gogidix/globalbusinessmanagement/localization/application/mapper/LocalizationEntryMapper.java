package com.gogidix.globalbusinessmanagement.localization.application.mapper;

import com.gogidix.globalbusinessmanagement.localization.domain.model.LocalizationEntry;
import com.gogidix.globalbusinessmanagement.localization.application.dto.LocalizationEntryRequestDto;
import com.gogidix.globalbusinessmanagement.localization.application.dto.LocalizationEntryResponseDto;
import org.springframework.stereotype.Component;

@Component
public class LocalizationEntryMapper {

    public LocalizationEntry toEntity(LocalizationEntryRequestDto dto) {
        return LocalizationEntry.builder()
            .tenantId(dto.getTenantId())
            .key(dto.getKey())
            .value(dto.getValue())
            .language(dto.getLanguage())
            .region(dto.getRegion())
            .module(dto.getModule())
            .isActive(dto.getIsActive())
            .build();
    }

    public LocalizationEntryResponseDto toResponseDto(LocalizationEntry entity) {
        return LocalizationEntryResponseDto.builder()
            .id(entity.getId())
            .tenantId(entity.getTenantId())
            .key(entity.getKey())
            .value(entity.getValue())
            .language(entity.getLanguage())
            .region(entity.getRegion())
            .module(entity.getModule())
            .isActive(entity.getIsActive())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
