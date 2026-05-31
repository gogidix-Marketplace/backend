package com.gogidix.globalbusinessmanagement.reportbuilder.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDto {
    private String tenantId;
    private String name;
    private String reportType;
    private String dataSource;
    private String format;
    private String status;
    private String schedule;
}
