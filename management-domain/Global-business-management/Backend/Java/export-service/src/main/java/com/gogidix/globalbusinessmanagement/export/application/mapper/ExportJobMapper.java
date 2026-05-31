package com.gogidix.globalbusinessmanagement.export.application.mapper;

import com.gogidix.globalbusinessmanagement.export.domain.model.ExportJob;
import com.gogidix.globalbusinessmanagement.export.application.dto.ExportJobRequestDto;
import com.gogidix.globalbusinessmanagement.export.application.dto.ExportJobResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ExportJobMapper {

    public ExportJob toEntity(ExportJobRequestDto dto) {
        return ExportJob.builder()
            .tenantId(dto.getTenantId())
            .name(dto.getName())
            .exportType(dto.getExportType())
            .format(dto.getFormat())
            .status(dto.getStatus())
            .filePath(dto.getFilePath())
            .recordCount(dto.getRecordCount())
            .build();
    }

    public ExportJobResponseDto toResponseDto(ExportJob entity) {
        return ExportJobResponseDto.builder()
            .id(entity.getId())
            .tenantId(entity.getTenantId())
            .name(entity.getName())
            .exportType(entity.getExportType())
            .format(entity.getFormat())
            .status(entity.getStatus())
            .filePath(entity.getFilePath())
            .recordCount(entity.getRecordCount())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
