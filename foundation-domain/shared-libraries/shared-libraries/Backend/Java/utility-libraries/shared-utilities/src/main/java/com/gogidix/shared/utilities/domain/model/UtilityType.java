package com.gogidix.shared.utilities.domain.model;

/**
 * Enumeration of utility operation types
 */
public enum UtilityType {
    
    /** Data transformation utilities */
    DATA_TRANSFORMATION("Data Transformation", true, true, 15),
    
    /** JSON processing utilities */
    JSON_PROCESSING("JSON Processing", true, true, 5),
    
    /** XML processing utilities */
    XML_PROCESSING("XML Processing", true, true, 8),
    
    /** CSV processing utilities */
    CSV_PROCESSING("CSV Processing", true, true, 10),
    
    /** Date and time utilities */
    DATE_TIME("Date Time Operations", true, true, 2),
    
    /** String manipulation utilities */
    STRING_MANIPULATION("String Manipulation", true, true, 1),
    
    /** Encryption/Decryption utilities */
    ENCRYPTION("Encryption Operations", false, false, 20),
    
    /** Hashing utilities */
    HASHING("Hashing Operations", true, true, 3),
    
    /** File processing utilities */
    FILE_PROCESSING("File Processing", false, true, 25),
    
    /** Image processing utilities */
    IMAGE_PROCESSING("Image Processing", true, true, 30),
    
    /** Email utilities */
    EMAIL_OPERATIONS("Email Operations", false, false, 12),
    
    /** HTTP request utilities */
    HTTP_CLIENT("HTTP Client Operations", false, false, 8),
    
    /** Database utilities */
    DATABASE_OPERATIONS("Database Operations", false, false, 18),
    
    /** Cache operations */
    CACHE_OPERATIONS("Cache Operations", true, true, 5),
    
    /** Validation utilities */
    VALIDATION("Validation Operations", true, true, 7),
    
    /** Compression utilities */
    COMPRESSION("Compression Operations", true, true, 15),
    
    /** QR Code utilities */
    QR_CODE("QR Code Operations", true, true, 8),
    
    /** Barcode utilities */
    BARCODE("Barcode Operations", true, true, 8),
    
    /** PDF utilities */
    PDF_PROCESSING("PDF Processing", true, true, 20),
    
    /** Logging utilities */
    LOGGING("Logging Operations", false, false, 2),
    
    /** Metrics collection */
    METRICS("Metrics Collection", false, false, 5),
    
    /** Configuration utilities */
    CONFIGURATION("Configuration Operations", true, true, 3),
    
    /** Notification utilities */
    NOTIFICATION("Notification Operations", false, false, 10),
    
    /** Template processing */
    TEMPLATE_PROCESSING("Template Processing", true, true, 12),
    
    /** Report generation */
    REPORT_GENERATION("Report Generation", false, true, 25),
    
    /** Data export utilities */
    DATA_EXPORT("Data Export Operations", false, true, 15),
    
    /** Data import utilities */
    DATA_IMPORT("Data Import Operations", false, true, 20),
    
    /** Backup utilities */
    BACKUP_OPERATIONS("Backup Operations", false, false, 30),
    
    /** System utilities */
    SYSTEM_OPERATIONS("System Operations", false, false, 25),
    
    /** Custom utility operations */
    CUSTOM("Custom Operations", true, true, 10);
    
    private final String displayName;
    private final boolean cacheable;
    private final boolean retryable;
    private final int complexityWeight;
    
    UtilityType(String displayName, boolean cacheable, boolean retryable, int complexityWeight) {
        this.displayName = displayName;
        this.cacheable = cacheable;
        this.retryable = retryable;
        this.complexityWeight = complexityWeight;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isCacheable() {
        return cacheable;
    }
    
    public boolean isRetryable() {
        return retryable;
    }
    
    public int getComplexityWeight() {
        return complexityWeight;
    }
    
    /**
     * Determines if this utility type requires user authentication
     */
    public boolean requiresAuthentication() {
        return this == ENCRYPTION ||
               this == DATABASE_OPERATIONS ||
               this == BACKUP_OPERATIONS ||
               this == SYSTEM_OPERATIONS ||
               this == EMAIL_OPERATIONS;
    }
    
    /**
     * Determines if this utility type is resource intensive
     */
    public boolean isResourceIntensive() {
        return complexityWeight >= 20;
    }
    
    /**
     * Determines if this utility type supports batch processing
     */
    public boolean supportsBatchProcessing() {
        return this == DATA_TRANSFORMATION ||
               this == JSON_PROCESSING ||
               this == XML_PROCESSING ||
               this == CSV_PROCESSING ||
               this == FILE_PROCESSING ||
               this == IMAGE_PROCESSING ||
               this == VALIDATION ||
               this == COMPRESSION ||
               this == DATA_EXPORT ||
               this == DATA_IMPORT ||
               this == TEMPLATE_PROCESSING;
    }
    
    /**
     * Gets the default timeout in seconds for this utility type
     */
    public long getDefaultTimeoutSeconds() {
        return switch (this) {
            case STRING_MANIPULATION, DATE_TIME, HASHING, VALIDATION -> 10;
            case JSON_PROCESSING, XML_PROCESSING, CACHE_OPERATIONS, CONFIGURATION -> 30;
            case CSV_PROCESSING, QR_CODE, BARCODE, TEMPLATE_PROCESSING -> 60;
            case DATA_TRANSFORMATION, COMPRESSION, HTTP_CLIENT, EMAIL_OPERATIONS -> 120;
            case ENCRYPTION, DATABASE_OPERATIONS, METRICS, NOTIFICATION -> 180;
            case FILE_PROCESSING, PDF_PROCESSING, DATA_EXPORT, DATA_IMPORT -> 300;
            case IMAGE_PROCESSING, REPORT_GENERATION -> 600;
            case BACKUP_OPERATIONS, SYSTEM_OPERATIONS -> 1800;
            case LOGGING, CUSTOM -> 60;
        };
    }
    
    /**
     * Gets the maximum allowed input size in bytes for this utility type
     */
    public long getMaxInputSizeBytes() {
        return switch (this) {
            case STRING_MANIPULATION, DATE_TIME, VALIDATION -> 1024; // 1KB
            case JSON_PROCESSING, XML_PROCESSING, HASHING -> 1048576; // 1MB
            case CSV_PROCESSING, TEMPLATE_PROCESSING, CONFIGURATION -> 5242880; // 5MB
            case DATA_TRANSFORMATION, COMPRESSION, QR_CODE, BARCODE -> 10485760; // 10MB
            case FILE_PROCESSING, PDF_PROCESSING -> 52428800; // 50MB
            case IMAGE_PROCESSING, DATA_EXPORT, DATA_IMPORT -> 104857600; // 100MB
            case REPORT_GENERATION, BACKUP_OPERATIONS -> 524288000; // 500MB
            case SYSTEM_OPERATIONS -> 1073741824; // 1GB
            case ENCRYPTION, DATABASE_OPERATIONS, HTTP_CLIENT, EMAIL_OPERATIONS, 
                 CACHE_OPERATIONS, LOGGING, METRICS, NOTIFICATION, CUSTOM -> 10485760; // 10MB
        };
    }
    
    /**
     * Determines if this utility type produces auditable results
     */
    public boolean isAuditable() {
        return this == ENCRYPTION ||
               this == DATABASE_OPERATIONS ||
               this == BACKUP_OPERATIONS ||
               this == SYSTEM_OPERATIONS ||
               this == EMAIL_OPERATIONS ||
               this == FILE_PROCESSING ||
               this == DATA_EXPORT ||
               this == DATA_IMPORT;
    }
    
    /**
     * Gets the priority adjustment for this utility type
     */
    public int getPriorityAdjustment() {
        if (isResourceIntensive()) return -1; // Lower priority for resource intensive
        if (requiresAuthentication()) return 1; // Higher priority for authenticated
        return 0; // No adjustment
    }
}