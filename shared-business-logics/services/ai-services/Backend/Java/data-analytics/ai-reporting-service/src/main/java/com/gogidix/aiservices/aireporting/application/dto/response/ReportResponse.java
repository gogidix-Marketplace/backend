package com.gogidix.aiservices.aireporting.application.dto.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public class ReportResponse {
    private String reportId;
    private String status;
    private String downloadUrl;
    private Instant expiresAt;

    public String getReportId() {
        return reportId;
    }

    public String getStatus() {
        return status;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}
