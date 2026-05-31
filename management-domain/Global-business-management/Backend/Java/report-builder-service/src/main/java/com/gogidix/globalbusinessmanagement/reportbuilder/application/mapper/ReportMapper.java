package com.gogidix.globalbusinessmanagement.reportbuilder.application.mapper;

import com.gogidix.globalbusinessmanagement.reportbuilder.domain.model.Report;
import com.gogidix.globalbusinessmanagement.reportbuilder.application.dto.ReportRequestDto;
import com.gogidix.globalbusinessmanagement.reportbuilder.application.dto.ReportResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public Report toEntity(ReportRequestDto dto) {
        return Report.builder()
            .tenantId(dto.getTenantId())
            .name(dto.getName())
            .reportType(dto.getReportType())
            .dataSource(dto.getDataSource())
            .format(dto.getFormat())
            .status(dto.getStatus())
            .schedule(dto.getSchedule())
            .build();
    }

    public ReportResponseDto toResponseDto(Report entity) {
        return ReportResponseDto.builder()
            .id(entity.getId())
            .tenantId(entity.getTenantId())
            .name(entity.getName())
            .reportType(entity.getReportType())
            .dataSource(entity.getDataSource())
            .format(entity.getFormat())
            .status(entity.getStatus())
            .schedule(entity.getSchedule())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
