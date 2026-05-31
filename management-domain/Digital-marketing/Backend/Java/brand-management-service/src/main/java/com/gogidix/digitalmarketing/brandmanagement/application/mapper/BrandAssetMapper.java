package com.gogidix.digitalmarketing.brandmanagement.application.mapper;

import com.gogidix.digitalmarketing.brandmanagement.domain.model.BrandAsset;
import com.gogidix.digitalmarketing.brandmanagement.application.dto.BrandAssetRequestDto;
import com.gogidix.digitalmarketing.brandmanagement.application.dto.BrandAssetResponseDto;
import org.springframework.stereotype.Component;

@Component
public class BrandAssetMapper {

    public BrandAsset toEntity(BrandAssetRequestDto dto) {
        return BrandAsset.builder()
            .tenantId(dto.getTenantId())
            .name(dto.getName())
            .type(dto.getType())
            .url(dto.getUrl())
            .category(dto.getCategory())
            .description(dto.getDescription())
            .status(dto.getStatus())
            .version(dto.getVersion())
            .fileFormat(dto.getFileFormat())
            .isActive(dto.getIsActive() != null ? Boolean.parseBoolean(dto.getIsActive()) : null)
            .isPublic(dto.getIsPublic() != null ? Boolean.parseBoolean(dto.getIsPublic()) : null)
            .build();
    }

    public BrandAssetResponseDto toResponseDto(BrandAsset entity) {
        return BrandAssetResponseDto.builder()
            .id(entity.getId())
            .tenantId(entity.getTenantId())
            .name(entity.getName())
            .type(entity.getType())
            .url(entity.getUrl())
            .category(entity.getCategory())
            .description(entity.getDescription())
            .status(entity.getStatus())
            .version(entity.getVersion())
            .fileFormat(entity.getFileFormat())
            .isActive(entity.getIsActive() != null ? String.valueOf(entity.getIsActive()) : null)
            .isPublic(entity.getIsPublic() != null ? String.valueOf(entity.getIsPublic()) : null)
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
