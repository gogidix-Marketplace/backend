package com.gogidix.aiservices.aireporting.domain;

import com.gogidix.aiservices.aireporting.domain.model.Report;
import com.gogidix.aiservices.aireporting.domain.model.ReportType;
import com.gogidix.aiservices.aireporting.domain.model.ExportFormat;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Value object for report generation request.
 */
@Builder
public class ReportRequest {

    @NonNull
    private String reportId;

    @NonNull
    private ReportType type;

    @NonNull
    private ExportFormat format;

    @NonNull
    private LocalDateTime startDate;

    @NonNull
    private LocalDateTime endDate;

    @Builder.Default
    private List<String> includeMetrics = List.of();

    @Builder.Default
    private Map<String, Object> customParameters = Map.of();

    public ReportRequest(String reportId, ReportType type, ExportFormat format,
                        LocalDateTime startDate, LocalDateTime endDate,
                        List<String> includeMetrics, Map<String, Object> customParameters) {
        if (reportId == null || reportId.trim().isEmpty()) {
            throw new IllegalArgumentException("reportId cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        if (format == null) {
            throw new IllegalArgumentException("format cannot be null");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("startDate cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("endDate cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate cannot be before startDate");
        }

        this.reportId = reportId;
        this.type = type;
        this.format = format;
        this.startDate = startDate;
        this.endDate = endDate;
        this.includeMetrics = includeMetrics != null ? includeMetrics : List.of();
        this.customParameters = customParameters != null ? customParameters : Map.of();
    }

    public String getReportId() { return reportId; }
    public ReportType getType() { return type; }
    public ExportFormat getFormat() { return format; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public List<String> getIncludeMetrics() { return includeMetrics; }
    public Map<String, Object> getCustomParameters() { return customParameters; }

    public boolean isValidDateRange() {
        return !endDate.isBefore(startDate) &&
               !startDate.isAfter(LocalDateTime.now());
    }
}
