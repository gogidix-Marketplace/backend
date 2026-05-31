package com.gogidix.globalbusinessmanagement.businessintelligence.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BIReportResponseDto {
    private String id;
    private String reportName;
    private String reportDescription;
    private String reportType;
    private String reportPeriod;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private String createdBy;
    private List<String> viewers;
    private List<String> sectionIds;
    private String executiveSummary;
    private Integer confidenceScore;
    private String regionCode;
    private String businessUnit;
    private Instant generatedAt;
    private Instant scheduledFor;
    private Instant createdAt;
    private Instant updatedAt;
}
