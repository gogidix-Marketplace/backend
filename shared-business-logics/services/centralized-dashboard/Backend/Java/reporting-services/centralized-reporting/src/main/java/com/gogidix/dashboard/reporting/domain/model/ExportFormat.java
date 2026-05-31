package com.gogidix.dashboard.reporting.domain.model;

/**
 * Export Format Enumeration
 */
public enum ExportFormat {
    PDF("application/pdf", ".pdf"),
    EXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"),
    CSV("text/csv", ".csv"),
    JSON("application/json", ".json");

    private final String mimeType;
    private final String fileExtension;

    ExportFormat(String mimeType, String fileExtension) {
        this.mimeType = mimeType;
        this.fileExtension = fileExtension;
    }

    public String getMimeType() { return mimeType; }
    public String getFileExtension() { return fileExtension; }
}
