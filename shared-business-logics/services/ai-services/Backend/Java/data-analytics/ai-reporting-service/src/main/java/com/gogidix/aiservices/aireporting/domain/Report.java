package com.gogidix.aiservices.aireporting.domain;

import com.gogidix.aiservices.aireporting.domain.model.ReportType;
import com.gogidix.aiservices.aireporting.domain.model.ExportFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Domain entity representing a report.
 */
public class Report {

    private final String reportId;
    private final ReportType type;
    private final ExportFormat format;
    private final LocalDateTime generatedAt;
    private ReportStatus status;
    private String downloadUrl;
    private final LocalDateTime expiresAt;
    private String errorMessage;
    private final Map<String, Object> metadata;

    public enum ReportStatus {
        PROCESSING,
        COMPLETED,
        FAILED
    }

    public Report(String reportId,
                 ReportType type,
                 ExportFormat format,
                 LocalDateTime generatedAt,
                 ReportStatus status,
                 String downloadUrl,
                 LocalDateTime expiresAt) {
        if (reportId == null || reportId.trim().isEmpty()) {
            throw new IllegalArgumentException("reportId cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        if (format == null) {
            throw new IllegalArgumentException("format cannot be null");
        }
        if (generatedAt == null) {
            throw new IllegalArgumentException("generatedAt cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }

        this.reportId = reportId;
        this.type = type;
        this.format = format;
        this.generatedAt = generatedAt;
        this.status = status;
        this.downloadUrl = downloadUrl;
        this.expiresAt = expiresAt;
        this.metadata = Map.of();
    }

    public String getReportId() {
        return reportId;
    }

    public ReportType getType() {
        return type;
    }

    public ExportFormat getFormat() {
        return format;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isAvailable() {
        return status == ReportStatus.COMPLETED && downloadUrl != null && !isExpired();
    }
}
