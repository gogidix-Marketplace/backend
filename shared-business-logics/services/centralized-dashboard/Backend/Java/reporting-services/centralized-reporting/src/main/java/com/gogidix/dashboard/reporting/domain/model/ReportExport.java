package com.gogidix.dashboard.reporting.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Report Export Domain Entity
 * 
 * Represents an exported report in a specific format
 */
public class ReportExport {
    private final String exportId;
    private final ReportId reportId;
    private final ExportFormat format;
    private final String filePath;
    private final String fileName;
    private final long fileSize;
    private final LocalDateTime exportedAt;
    private final String exportedBy;
    private final LocalDateTime expiresAt;
    private final ExportStatus status;
    private final String downloadUrl;

    private ReportExport(Builder builder) {
        this.exportId = Objects.requireNonNull(builder.exportId, "Export ID is required");
        this.reportId = Objects.requireNonNull(builder.reportId, "Report ID is required");
        this.format = Objects.requireNonNull(builder.format, "Format is required");
        this.filePath = Objects.requireNonNull(builder.filePath, "File path is required");
        this.fileName = Objects.requireNonNull(builder.fileName, "File name is required");
        this.fileSize = builder.fileSize;
        this.exportedAt = builder.exportedAt != null ? builder.exportedAt : LocalDateTime.now();
        this.exportedBy = Objects.requireNonNull(builder.exportedBy, "Exported by is required");
        this.expiresAt = builder.expiresAt;
        this.status = builder.status != null ? builder.status : ExportStatus.COMPLETED;
        this.downloadUrl = builder.downloadUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Business methods
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isDownloadable() {
        return status == ExportStatus.COMPLETED && !isExpired();
    }

    public String getFileExtension() {
        return format.name().toLowerCase();
    }

    public String getFormattedFileSize() {
        if (fileSize < 1024) {
            return fileSize + " B";
        } else if (fileSize < 1024 * 1024) {
            return (fileSize / 1024) + " KB";
        } else {
            return (fileSize / (1024 * 1024)) + " MB";
        }
    }

    // Getters
    public String getExportId() { return exportId; }
    public ReportId getReportId() { return reportId; }
    public ExportFormat getFormat() { return format; }
    public String getFilePath() { return filePath; }
    public String getFileName() { return fileName; }
    public long getFileSize() { return fileSize; }
    public LocalDateTime getExportedAt() { return exportedAt; }
    public String getExportedBy() { return exportedBy; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public ExportStatus getStatus() { return status; }
    public String getDownloadUrl() { return downloadUrl; }

    public static class Builder {
        private String exportId;
        private ReportId reportId;
        private ExportFormat format;
        private String filePath;
        private String fileName;
        private long fileSize;
        private LocalDateTime exportedAt;
        private String exportedBy;
        private LocalDateTime expiresAt;
        private ExportStatus status;
        private String downloadUrl;

        public Builder withExportId(String exportId) {
            this.exportId = exportId;
            return this;
        }

        public Builder withReportId(ReportId reportId) {
            this.reportId = reportId;
            return this;
        }

        public Builder withFormat(ExportFormat format) {
            this.format = format;
            return this;
        }

        public Builder withFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder withFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder withFileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public Builder withExportedAt(LocalDateTime exportedAt) {
            this.exportedAt = exportedAt;
            return this;
        }

        public Builder withExportedBy(String exportedBy) {
            this.exportedBy = exportedBy;
            return this;
        }

        public Builder withExpiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder withStatus(ExportStatus status) {
            this.status = status;
            return this;
        }

        public Builder withDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        public ReportExport build() {
            if (exportId == null) {
                exportId = java.util.UUID.randomUUID().toString();
            }
            if (fileName == null && reportId != null && format != null) {
                fileName = "report_" + reportId.getValue() + "." + format.name().toLowerCase();
            }
            if (exportedBy == null) {
                exportedBy = "system";
            }
            return new ReportExport(this);
        }
    }

    public enum ExportStatus {
        PENDING, PROCESSING, COMPLETED, FAILED, EXPIRED
    }
}