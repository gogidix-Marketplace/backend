package com.gogidix.aiservices.aireporting.domain.model;

import java.time.Instant;
import java.util.List;

public class Report {
    private String reportId;
    private ReportType type;
    private List<String> includeMetrics;
    private ExportFormat format;
    private Status status;
    private String downloadUrl;
    private Instant expiresAt;
    private String errorMessage;
    private final Instant createdAt;

    public enum Status {
        PROCESSING, COMPLETED, FAILED
    }

    public Report(ReportType type, List<String> includeMetrics, ExportFormat format) {
        if (type == null) {
            throw new IllegalArgumentException("Report type cannot be null");
        }
        if (includeMetrics == null) {
            throw new IllegalArgumentException("Include metrics cannot be null");
        }

        this.reportId = java.util.UUID.randomUUID().toString();
        this.type = type;
        this.includeMetrics = List.copyOf(includeMetrics);
        this.format = format != null ? format : ExportFormat.PDF;
        this.status = Status.PROCESSING;
        this.createdAt = Instant.now();
        this.expiresAt = Instant.now().plusSeconds(2592000); // 30 days
    }

    public static Report create(ReportType type, List<String> includeMetrics) {
        return new Report(type, includeMetrics, ExportFormat.PDF);
    }

    public void complete(String downloadUrl) {
        this.status = Status.COMPLETED;
        this.downloadUrl = downloadUrl;
    }

    public void fail(String errorMessage) {
        this.status = Status.FAILED;
        this.errorMessage = errorMessage;
    }

    public String getReportId() { return reportId; }
    public ReportType getType() { return type; }
    public List<String> getIncludeMetrics() { return includeMetrics; }
    public ExportFormat getFormat() { return format; }
    public Status getStatus() { return status; }
    public String getDownloadUrl() { return downloadUrl; }
    public Instant getExpiresAt() { return expiresAt; }
    public String getErrorMessage() { return errorMessage; }
    public Instant getCreatedAt() { return createdAt; }
}
