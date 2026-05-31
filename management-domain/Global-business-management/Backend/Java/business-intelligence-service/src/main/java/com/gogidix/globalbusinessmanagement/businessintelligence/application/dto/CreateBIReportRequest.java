package com.gogidix.globalbusinessmanagement.businessintelligence.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBIReportRequest {
    private String reportName;
    private String reportDescription;
    private String reportType;
    private String reportPeriod;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String createdBy;
    private List<String> viewers;
    private String templateId;
    private String regionCode;
    private String businessUnit;
    private Map<String, Object> parameters;
}
