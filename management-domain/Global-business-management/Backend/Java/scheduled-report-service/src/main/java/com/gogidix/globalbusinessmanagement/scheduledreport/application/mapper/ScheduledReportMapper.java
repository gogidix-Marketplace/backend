package com.gogidix.globalbusinessmanagement.scheduledreport.application.mapper;

import com.gogidix.globalbusinessmanagement.scheduledreport.domain.model.ScheduledReport;
import com.gogidix.globalbusinessmanagement.scheduledreport.application.dto.ScheduledReportRequestDto;
import com.gogidix.globalbusinessmanagement.scheduledreport.application.dto.ScheduledReportResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ScheduledReportMapper {

    public ScheduledReport toEntity(ScheduledReportRequestDto dto) {
        return ScheduledReport.builder()
            .tenantId(dto.getTenantId())
            .name(dto.getName())
            .reportId(dto.getReportId())
            .cronExpression(dto.getCronExpression())
            .recipients(dto.getRecipients())
            .format(dto.getFormat())
            .isActive(dto.getIsActive())
            .lastRunAt(dto.getLastRunAt())
            .build();
    }

    public ScheduledReportResponseDto toResponseDto(ScheduledReport entity) {
        return ScheduledReportResponseDto.builder()
            .id(entity.getId())
            .tenantId(entity.getTenantId())
            .name(entity.getName())
            .reportId(entity.getReportId())
            .cronExpression(entity.getCronExpression())
            .recipients(entity.getRecipients())
            .format(entity.getFormat())
            .isActive(entity.getIsActive())
            .lastRunAt(entity.getLastRunAt())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
