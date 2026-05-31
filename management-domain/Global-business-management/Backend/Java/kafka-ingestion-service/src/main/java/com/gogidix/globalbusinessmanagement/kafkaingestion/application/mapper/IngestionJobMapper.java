package com.gogidix.globalbusinessmanagement.kafkaingestion.application.mapper;

import com.gogidix.globalbusinessmanagement.kafkaingestion.domain.model.IngestionJob;
import com.gogidix.globalbusinessmanagement.kafkaingestion.application.dto.IngestionJobRequestDto;
import com.gogidix.globalbusinessmanagement.kafkaingestion.application.dto.IngestionJobResponseDto;
import org.springframework.stereotype.Component;

@Component
public class IngestionJobMapper {

    public IngestionJob toEntity(IngestionJobRequestDto dto) {
        return IngestionJob.builder()
            .tenantId(dto.getTenantId())
            .topic(dto.getTopic())
            .source(dto.getSource())
            .status(dto.getStatus())
            .recordCount(dto.getRecordCount())
            .errorCount(dto.getErrorCount())
            .lastProcessedOffset(dto.getLastProcessedOffset())
            .build();
    }

    public IngestionJobResponseDto toResponseDto(IngestionJob entity) {
        return IngestionJobResponseDto.builder()
            .id(entity.getId())
            .tenantId(entity.getTenantId())
            .topic(entity.getTopic())
            .source(entity.getSource())
            .status(entity.getStatus())
            .recordCount(entity.getRecordCount())
            .errorCount(entity.getErrorCount())
            .lastProcessedOffset(entity.getLastProcessedOffset())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
