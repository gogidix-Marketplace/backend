package com.gogidix.digitalmarketing.seo.application.mapper;

import com.gogidix.digitalmarketing.seo.domain.model.Keyword;
import com.gogidix.digitalmarketing.seo.application.dto.KeywordRequestDto;
import com.gogidix.digitalmarketing.seo.application.dto.KeywordResponseDto;
import org.springframework.stereotype.Component;

@Component
public class KeywordMapper {

    public Keyword toEntity(KeywordRequestDto dto) {
        return Keyword.builder()
            .tenantId(dto.getTenantId())
            .keyword(dto.getKeyword())
            .domain(dto.getDomain())
            .volume(dto.getVolume() != null ? Integer.parseInt(dto.getVolume()) : null)
            .difficulty(dto.getDifficulty() != null ? Integer.parseInt(dto.getDifficulty()) : null)
            .ranking(dto.getRanking() != null ? Integer.parseInt(dto.getRanking()) : null)
            .previousRanking(dto.getPreviousRanking() != null ? Integer.parseInt(dto.getPreviousRanking()) : null)
            .build();
    }

    public KeywordResponseDto toResponseDto(Keyword entity) {
        return KeywordResponseDto.builder()
            .id(entity.getId())
            .tenantId(entity.getTenantId())
            .keyword(entity.getKeyword())
            .domain(entity.getDomain())
            .volume(entity.getVolume() != null ? String.valueOf(entity.getVolume()) : null)
            .difficulty(entity.getDifficulty() != null ? String.valueOf(entity.getDifficulty()) : null)
            .ranking(entity.getRanking() != null ? String.valueOf(entity.getRanking()) : null)
            .previousRanking(entity.getPreviousRanking() != null ? String.valueOf(entity.getPreviousRanking()) : null)
            .createdAt(entity.getLastChecked())
            .updatedAt(entity.getLastChecked())
            .build();
    }
}
