package com.gogidix.digitalmarketing.contentmanagement.application.mapper;

import com.gogidix.digitalmarketing.contentmanagement.domain.model.ContentPiece;
import com.gogidix.digitalmarketing.contentmanagement.application.dto.ContentPieceRequestDto;
import com.gogidix.digitalmarketing.contentmanagement.application.dto.ContentPieceResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ContentPieceMapper {

    public ContentPiece toEntity(ContentPieceRequestDto dto) {
        return ContentPiece.builder()
            .tenantId(dto.getTenantId())
            .title(dto.getTitle())
            .contentType(dto.getContentType())
            .author(dto.getAuthor())
            .isFeatured(dto.getIsFeatured() != null ? Boolean.parseBoolean(dto.getIsFeatured()) : null)
            .build();
    }


    public ContentPieceResponseDto toResponseDto(ContentPiece entity) {
        return ContentPieceResponseDto.builder()
            .id(entity.getId())
            .tenantId(entity.getTenantId())
            .title(entity.getTitle())
            .contentType(entity.getContentType())
            .author(entity.getAuthor())
            .status(entity.getStatus())
            .isFeatured(entity.getIsFeatured() != null ? String.valueOf(entity.getIsFeatured()) : null)
            .createdAt(entity.getPublishedAt())
            .updatedAt(entity.getPublishedAt())
            .build();
    }
}
