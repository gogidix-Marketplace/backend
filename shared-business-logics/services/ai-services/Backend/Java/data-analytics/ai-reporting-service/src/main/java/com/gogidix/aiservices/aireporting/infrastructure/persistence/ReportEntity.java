package com.gogidix.aiservices.aireporting.infrastructure.persistence;

import com.gogidix.aiservices.aireporting.domain.aggregate.ReportGeneration;
import com.gogidix.aiservices.aireporting.domain.model.ExportFormat;
import com.gogidix.aiservices.aireporting.domain.model.ReportType;

import java.time.Instant;
import java.util.List;

public class ReportEntity {
    private String generationId;
    private ReportType type;
    private List<String> includeMetrics;
    private ExportFormat format;
    private Instant dateRangeStart;
    private Instant dateRangeEnd;
    private ReportGeneration.GenerationStatus status;
    private int progress;
    private String downloadUrl;
    private Instant expiresAt;
    private String errorMessage;
    private Instant createdAt;
    private Instant completedAt;

    public ReportEntity() {
    }

    public String getGenerationId() {
        return generationId;
    }

    public void setGenerationId(String generationId) {
        this.generationId = generationId;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public List<String> getIncludeMetrics() {
        return includeMetrics;
    }

    public void setIncludeMetrics(List<String> includeMetrics) {
        this.includeMetrics = includeMetrics;
    }

    public ExportFormat getFormat() {
        return format;
    }

    public void setFormat(ExportFormat format) {
        this.format = format;
    }

    public Instant getDateRangeStart() {
        return dateRangeStart;
    }

    public void setDateRangeStart(Instant dateRangeStart) {
        this.dateRangeStart = dateRangeStart;
    }

    public Instant getDateRangeEnd() {
        return dateRangeEnd;
    }

    public void setDateRangeEnd(Instant dateRangeEnd) {
        this.dateRangeEnd = dateRangeEnd;
    }

    public ReportGeneration.GenerationStatus getStatus() {
        return status;
    }

    public void setStatus(ReportGeneration.GenerationStatus status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }
}
