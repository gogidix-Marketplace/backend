package com.gogidix.centralizeddashboard.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for export response
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExportResponse {
    // Original fields
    private String id;
    private String status;
    private String fileUrl;
    private String fileType;
    private long fileSize;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String message;
    
    // Additional fields needed by ExportService
    private String exportId;
    private String downloadUrl;
    private String filename;
    private String contentType;

    /**
     * Manual builder() method (Lombok @Builder not generating it)
     */
    public static ExportResponseBuilder builder() {
        return new ExportResponseBuilder();
    }

    /**
     * Builder method for exportId to maintain compatibility
     * @param exportId The export ID
     * @return The builder instance
     */
    public static class ExportResponseBuilder {
        private String id;
        private String status;
        private String fileUrl;
        private String fileType;
        private long fileSize;
        private LocalDateTime createdAt;
        private LocalDateTime expiresAt;
        private String message;
        private String exportId;
        private String downloadUrl;
        private String filename;
        private String contentType;

        public ExportResponseBuilder exportId(String exportId) {
            this.exportId = exportId;
            this.id = exportId; // For backward compatibility
            return this;
        }
        
        public ExportResponseBuilder downloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            this.fileUrl = downloadUrl; // For backward compatibility
            return this;
        }

        public ExportResponseBuilder status(String status) {
            this.status = status;
            return this;
        }

        public ExportResponseBuilder fileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public ExportResponseBuilder fileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public ExportResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ExportResponseBuilder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public ExportResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ExportResponseBuilder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public ExportResponseBuilder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public ExportResponse build() {
            ExportResponse response = new ExportResponse();
            response.id = this.id != null ? this.id : this.exportId;
            response.status = this.status;
            response.fileUrl = this.fileUrl != null ? this.fileUrl : this.downloadUrl;
            response.fileType = this.fileType;
            response.fileSize = this.fileSize;
            response.createdAt = this.createdAt;
            response.expiresAt = this.expiresAt;
            response.message = this.message;
            response.exportId = this.exportId;
            response.downloadUrl = this.downloadUrl;
            response.filename = this.filename;
            response.contentType = this.contentType;
            return response;
        }
    }
}
