package com.gogidix.dashboard.reporting.adapter.in.web.dto;

import com.gogidix.dashboard.reporting.domain.model.ReportType;
import com.gogidix.dashboard.reporting.domain.model.OutputFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Generate Report Request DTO
 */
public class GenerateReportRequestDTO {
    @NotNull
    private ReportType reportType;

    @NotNull
    private OutputFormat outputFormat;

    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String domain;
    private Map<String, Object> parameters;

    // Getters and Setters
    public ReportType getReportType() { return reportType; }
    public void setReportType(ReportType reportType) { this.reportType = reportType; }

    public OutputFormat getOutputFormat() { return outputFormat; }
    public void setOutputFormat(OutputFormat outputFormat) { this.outputFormat = outputFormat; }

    public LocalDateTime getFromDate() { return fromDate; }
    public void setFromDate(LocalDateTime fromDate) { this.fromDate = fromDate; }

    public LocalDateTime getToDate() { return toDate; }
    public void setToDate(LocalDateTime toDate) { this.toDate = toDate; }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }

    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }

    // Additional getters for controller compatibility
    public String getReportName() { return "Report_" + reportType.name(); }
    public ReportType getType() { return reportType; }
    public com.gogidix.dashboard.reporting.domain.model.ReportConfiguration getConfiguration() {
        return new com.gogidix.dashboard.reporting.domain.model.ReportConfiguration(outputFormat, true, true);
    }
}
