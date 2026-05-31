package com.gogidix.aiservices.aireporting.application.dto.request;

import com.gogidix.aiservices.aireporting.domain.model.ExportFormat;
import com.gogidix.aiservices.aireporting.domain.model.ReportType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Builder
public class GenerateReportRequest {
    private ReportType type;
    private Map<String, Instant> dateRange;
    private List<String> includeMetrics;
    private ExportFormat format;

    public ReportType getType() {
        return type;
    }

    public Map<String, Instant> getDateRange() {
        return dateRange;
    }

    public List<String> getIncludeMetrics() {
        return includeMetrics;
    }

    public ExportFormat getFormat() {
        return format;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public void setDateRange(Map<String, Instant> dateRange) {
        this.dateRange = dateRange;
    }

    public void setIncludeMetrics(List<String> includeMetrics) {
        this.includeMetrics = includeMetrics;
    }

    public void setFormat(ExportFormat format) {
        this.format = format;
    }
}
