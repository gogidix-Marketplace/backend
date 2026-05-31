package com.gogidix.globalbusinessmanagement.datavalidation.domain.dto;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.DataQualityReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for DataQualityReport
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataQualityReportDTO {

    private String id;

    @NotNull(message = "Report ID is required")
    private String reportId;

    private String entityType;

    private String entityId;

    private String entityName;

    private String tenantId;

    @NotNull(message = "Report type is required")
    private DataQualityReport.ReportType reportType;

    private String dataSource;

    private String dataSourceType;

    private LocalDateTime reportDate;

    private LocalDateTime periodStart;

    private LocalDateTime periodEnd;

    private int totalRecords;

    private int validRecords;

    private int invalidRecords;

    private int pendingRecords;

    private double qualityScore;

    private DataQualityReport.QualityLevel qualityLevel;

    private Map<String, DataQualityReport.FieldQualityMetrics> fieldMetrics;

    private List<DataQualityReport.RuleExecutionSummary> ruleSummaries;

    private List<DataQualityReport.QualityIssue> topIssues;

    private Map<String, Integer> errorDistribution;

    private DataQualityReport.QualityTrend trend;

    private List<DataQualityReport.QualityRecommendation> recommendations;

    private String generatedBy;

    private LocalDateTime generatedAt;

    private String batchId;

    private Map<String, Object> metadata;

    private boolean archived;
}
