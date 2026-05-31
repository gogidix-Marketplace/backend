package com.gogidix.dashboard.reporting.adapter.in.web.dto;

import com.gogidix.dashboard.reporting.domain.model.ReportType;
import com.gogidix.dashboard.reporting.domain.model.OutputFormat;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * Schedule Report Request DTO
 */
public class ScheduleReportRequestDTO {
    @NotNull
    private ReportType reportType;

    @NotNull
    private OutputFormat outputFormat;

    @NotNull
    private String cronExpression;

    private String domain;
    private Map<String, Object> parameters;

    // Getters and Setters
    public ReportType getReportType() { return reportType; }
    public void setReportType(ReportType reportType) { this.reportType = reportType; }

    public OutputFormat getOutputFormat() { return outputFormat; }
    public void setOutputFormat(OutputFormat outputFormat) { this.outputFormat = outputFormat; }

    public String getCronExpression() { return cronExpression; }
    public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }

    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }

    // Additional getters for controller compatibility
    public String getReportName() { return "Scheduled_" + reportType.name(); }
    public ReportType getType() { return reportType; }
    public com.gogidix.dashboard.reporting.domain.model.ReportConfiguration getConfiguration() {
        return new com.gogidix.dashboard.reporting.domain.model.ReportConfiguration(outputFormat, true, true);
    }
}
