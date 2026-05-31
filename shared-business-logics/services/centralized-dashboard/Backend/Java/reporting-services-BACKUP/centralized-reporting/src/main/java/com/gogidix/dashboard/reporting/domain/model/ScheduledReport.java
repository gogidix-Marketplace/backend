package com.gogidix.dashboard.reporting.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Scheduled Report Domain Entity
 * 
 * Represents an automated report generation schedule
 */
public class ScheduledReport {
    private final String scheduleId;
    private final String reportName;
    private final ReportType type;
    private final String domain;
    private final String cronExpression;
    private final ReportConfiguration configuration;
    private final String createdBy;
    private final LocalDateTime createdAt;
    private LocalDateTime nextExecutionTime;
    private LocalDateTime lastExecutionTime;
    private ScheduleStatus status;
    private String errorMessage;

    private ScheduledReport(Builder builder) {
        this.scheduleId = Objects.requireNonNull(builder.scheduleId, "Schedule ID is required");
        this.reportName = Objects.requireNonNull(builder.reportName, "Report name is required");
        this.type = Objects.requireNonNull(builder.type, "Report type is required");
        this.domain = Objects.requireNonNull(builder.domain, "Domain is required");
        this.cronExpression = Objects.requireNonNull(builder.cronExpression, "Cron expression is required");
        this.configuration = Objects.requireNonNull(builder.configuration, "Configuration is required");
        this.createdBy = Objects.requireNonNull(builder.createdBy, "Creator is required");
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.nextExecutionTime = builder.nextExecutionTime;
        this.lastExecutionTime = builder.lastExecutionTime;
        this.status = builder.status != null ? builder.status : ScheduleStatus.ACTIVE;
        this.errorMessage = builder.errorMessage;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getScheduleId() { return scheduleId; }
    public String getReportName() { return reportName; }
    public ReportType getType() { return type; }
    public String getDomain() { return domain; }
    public String getCronExpression() { return cronExpression; }
    public ReportConfiguration getConfiguration() { return configuration; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getNextExecutionTime() { return nextExecutionTime; }
    public LocalDateTime getLastExecutionTime() { return lastExecutionTime; }
    public ScheduleStatus getStatus() { return status; }
    public String getErrorMessage() { return errorMessage; }

    public static class Builder {
        private String scheduleId;
        private String reportName;
        private ReportType type;
        private String domain;
        private String cronExpression;
        private ReportConfiguration configuration;
        private String createdBy;
        private LocalDateTime createdAt;
        private LocalDateTime nextExecutionTime;
        private LocalDateTime lastExecutionTime;
        private ScheduleStatus status;
        private String errorMessage;

        public Builder withScheduleId(String scheduleId) {
            this.scheduleId = scheduleId;
            return this;
        }

        public Builder withReportName(String reportName) {
            this.reportName = reportName;
            return this;
        }

        public Builder withType(ReportType type) {
            this.type = type;
            return this;
        }

        public Builder withDomain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder withCronExpression(String cronExpression) {
            this.cronExpression = cronExpression;
            return this;
        }

        public Builder withConfiguration(ReportConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public Builder withCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder withNextExecutionTime(LocalDateTime nextExecutionTime) {
            this.nextExecutionTime = nextExecutionTime;
            return this;
        }

        public Builder withLastExecutionTime(LocalDateTime lastExecutionTime) {
            this.lastExecutionTime = lastExecutionTime;
            return this;
        }

        public Builder withStatus(ScheduleStatus status) {
            this.status = status;
            return this;
        }

        public Builder withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public ScheduledReport build() {
            if (scheduleId == null) {
                scheduleId = java.util.UUID.randomUUID().toString();
            }
            return new ScheduledReport(this);
        }
    }

    public enum ScheduleStatus {
        ACTIVE, PAUSED, CANCELLED, FAILED
    }
}