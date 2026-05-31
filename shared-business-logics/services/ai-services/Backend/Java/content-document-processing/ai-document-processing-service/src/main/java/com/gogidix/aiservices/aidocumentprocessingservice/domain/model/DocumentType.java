package com.gogidix.aiservices.aidocumentprocessingservice.domain.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum DocumentType {
    INVOICE("invoice", 1, 50),
    CONTRACT("contract", 2, 50),
    REPORT("report", 3, 50),
    FORM("form", 4, 50),
    RECEIPT("receipt", 5, 20),
    CERTIFICATE("certificate", 6, 30),
    UNKNOWN("unknown", 99, 50);

    private final String keyword;
    private final int priority;
    private final int maxFileSizeMB;

    private static final List<String> SUPPORTED_FORMATS = Arrays.asList("PDF", "DOCX", "TXT", "PNG", "JPG", "JPEG");

    DocumentType(String keyword, int priority, int maxFileSizeMB) {
        this.keyword = keyword;
        this.priority = priority;
        this.maxFileSizeMB = maxFileSizeMB;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getPriority() {
        return priority;
    }

    public int getMaxFileSizeMB() {
        return maxFileSizeMB;
    }

    public List<String> getSupportedFormats() {
        return SUPPORTED_FORMATS;
    }

    public boolean requiresOcr() {
        return this != UNKNOWN;
    }

    public static DocumentType fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return UNKNOWN;
        }

        String upperValue = value.toUpperCase().trim();
        for (DocumentType type : DocumentType.values()) {
            if (type.name().equals(upperValue)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public static DocumentType fromFilename(String filename) {
        if (filename == null || filename.isEmpty()) {
            return UNKNOWN;
        }

        String lowerFilename = filename.toLowerCase();
        for (DocumentType type : DocumentType.values()) {
            if (type != UNKNOWN && lowerFilename.contains(type.keyword)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
