package com.gogidix.globalbusinessmanagement.batchaggregation.application.mapper;

import com.gogidix.globalbusinessmanagement.batchaggregation.domain.model.BatchAggregation;
import com.gogidix.globalbusinessmanagement.batchaggregation.application.dto.BatchAggregationRequestDto;
import com.gogidix.globalbusinessmanagement.batchaggregation.application.dto.BatchAggregationResponseDto;
import org.springframework.stereotype.Component;

@Component
public class BatchAggregationMapper {

    public BatchAggregation toEntity(BatchAggregationRequestDto dto) {
        return BatchAggregation.builder()
            .tenantId(dto.getTenantId())
            .name(dto.getName())
            .aggregationType(dto.getAggregationType())
            .dataSource(dto.getDataSource())
            .status(dto.getStatus())
            .schedule(dto.getSchedule())
            .region(dto.getRegion())
            .country(dto.getCountry())
            .build();
    }

    public BatchAggregationResponseDto toResponseDto(BatchAggregation entity) {
        return BatchAggregationResponseDto.builder()
            .id(entity.getId())
            .tenantId(entity.getTenantId())
            .name(entity.getName())
            .aggregationType(entity.getAggregationType())
            .dataSource(entity.getDataSource())
            .status(entity.getStatus())
            .schedule(entity.getSchedule())
            .region(entity.getRegion())
            .country(entity.getCountry())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
