package com.gogidix.globalbusinessmanagement.scheduledreport.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledReportRequestDto {
    private String tenantId;
    private String name;
    private String reportId;
    private String cronExpression;
    private String recipients;
    private String format;
    private String isActive;
    private String lastRunAt;
}
