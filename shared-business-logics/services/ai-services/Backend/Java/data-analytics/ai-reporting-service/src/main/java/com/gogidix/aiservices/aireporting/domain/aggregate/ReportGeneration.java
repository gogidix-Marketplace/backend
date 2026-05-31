package com.gogidix.aiservices.aireporting.domain.aggregate;

import com.gogidix.aiservices.aireporting.domain.model.ExportFormat;
import com.gogidix.aiservices.aireporting.domain.model.ReportType;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReportGeneration {
    private final String generationId;
    private final ReportType type;
    private final List<String> includeMetrics;
    private final ExportFormat format;
    private final Instant dateRangeStart;
    private final Instant dateRangeEnd;
    private GenerationStatus status;
    private int progress;
    private String downloadUrl;
    private Instant expiresAt;
    private String errorMessage;
    private final Instant createdAt;
    private Instant completedAt;

    public enum GenerationStatus {
        PROCESSING, COMPLETED, FAILED
    }

    private ReportGeneration(ReportType type, List<String> includeMetrics, ExportFormat format,
                            Instant dateRangeStart, Instant dateRangeEnd) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (includeMetrics == null) {
            throw new IllegalArgumentException("Include metrics cannot be null");
        }

        this.generationId = UUID.randomUUID().toString();
        this.type = type;
        this.includeMetrics = List.copyOf(includeMetrics);
        this.format = format != null ? format : ExportFormat.PDF;
        this.dateRangeStart = dateRangeStart;
        this.dateRangeEnd = dateRangeEnd;
        this.status = GenerationStatus.PROCESSING;
        this.progress = 0;
        this.createdAt = Instant.now();
    }

    public static ReportGeneration create(ReportType type, List<String> includeMetrics, ExportFormat format) {
        return new ReportGeneration(type, includeMetrics, format, null, null);
    }

    public void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }
        this.progress = progress;
    }

    public void complete(String downloadUrl) {
        this.status = GenerationStatus.COMPLETED;
        this.progress = 100;
        this.downloadUrl = downloadUrl;
        this.expiresAt = Instant.now().plusSeconds(2592000);
        this.completedAt = Instant.now();
    }

    public void fail(String errorMessage) {
        this.status = GenerationStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = Instant.now();
    }

    public String getGenerationId() { return generationId; }
    public ReportType getType() { return type; }
    public List<String> getIncludeMetrics() { return includeMetrics; }
    public ExportFormat getFormat() { return format; }
    public Instant getDateRangeStart() { return dateRangeStart; }
    public Instant getDateRangeEnd() { return dateRangeEnd; }
    public GenerationStatus getStatus() { return status; }
    public int getProgress() { return progress; }
    public String getDownloadUrl() { return downloadUrl; }
    public Instant getExpiresAt() { return expiresAt; }
    public String getErrorMessage() { return errorMessage; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getCompletedAt() { return completedAt; }
}
