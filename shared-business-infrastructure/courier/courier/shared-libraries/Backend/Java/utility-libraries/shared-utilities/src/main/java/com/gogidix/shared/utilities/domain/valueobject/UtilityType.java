package com.gogidix.shared.utilities.domain.valueobject;

/**
 * Enumeration of all utility types supported by the shared utilities service
 */
public enum UtilityType {
    
    // Date and Time Utilities
    DATETIME_FORMATTING("Date/Time formatting operations"),
    DATETIME_PARSING("Date/Time parsing operations"),
    DATETIME_CALCULATION("Date/Time calculation operations"),
    DATETIME_CONVERSION("Date/Time conversion operations"),
    DATETIME_VALIDATION("Date/Time validation operations"),
    TIMEZONE_CONVERSION("Timezone conversion operations"),
    
    // JSON Processing Utilities
    JSON_SERIALIZATION("JSON serialization operations"),
    JSON_DESERIALIZATION("JSON deserialization operations"),
    JSON_VALIDATION("JSON validation operations"),
    JSON_TRANSFORMATION("JSON transformation operations"),
    JSON_SCHEMA_VALIDATION("JSON schema validation operations"),
    
    // String Processing Utilities
    STRING_FORMATTING("String formatting operations"),
    STRING_VALIDATION("String validation operations"),
    STRING_TRANSFORMATION("String transformation operations"),
    STRING_SANITIZATION("String sanitization operations"),
    STRING_ENCRYPTION("String encryption operations"),
    
    // Collection Processing Utilities
    COLLECTION_FILTERING("Collection filtering operations"),
    COLLECTION_MAPPING("Collection mapping operations"),
    COLLECTION_AGGREGATION("Collection aggregation operations"),
    COLLECTION_SORTING("Collection sorting operations"),
    COLLECTION_VALIDATION("Collection validation operations"),
    
    // File Processing Utilities
    FILE_EXCEL_PROCESSING("Excel file processing operations"),
    FILE_CSV_PROCESSING("CSV file processing operations"),
    FILE_TEXT_PROCESSING("Text file processing operations"),
    FILE_VALIDATION("File validation operations"),
    FILE_CONVERSION("File format conversion operations"),
    
    // HTTP and Web Utilities
    HTTP_REQUEST_PROCESSING("HTTP request processing operations"),
    HTTP_RESPONSE_PROCESSING("HTTP response processing operations"),
    URL_VALIDATION("URL validation operations"),
    HTTP_HEADER_PROCESSING("HTTP header processing operations"),
    
    // Validation Utilities
    EMAIL_VALIDATION("Email validation operations"),
    PHONE_VALIDATION("Phone number validation operations"),
    BUSINESS_RULE_VALIDATION("Business rule validation operations"),
    DATA_INTEGRITY_VALIDATION("Data integrity validation operations"),
    CONSTRAINT_VALIDATION("Constraint validation operations"),
    
    // Mathematical and Statistical Utilities
    MATHEMATICAL_CALCULATION("Mathematical calculation operations"),
    STATISTICAL_CALCULATION("Statistical calculation operations"),
    FINANCIAL_CALCULATION("Financial calculation operations"),
    PERCENTAGE_CALCULATION("Percentage calculation operations"),
    
    // Cryptographic Utilities
    HASHING("Hashing operations"),
    ENCODING("Encoding operations"),
    DECODING("Decoding operations"),
    ENCRYPTION("Encryption operations"),
    DECRYPTION("Decryption operations"),
    
    // Reflection and Introspection Utilities
    OBJECT_INTROSPECTION("Object introspection operations"),
    DYNAMIC_INVOCATION("Dynamic method invocation operations"),
    ANNOTATION_PROCESSING("Annotation processing operations"),
    CLASS_LOADING("Dynamic class loading operations"),
    
    // General Utilities
    UTILITY_HEALTH_CHECK("Utility service health check operations"),
    UTILITY_PERFORMANCE_TEST("Utility performance testing operations"),
    UTILITY_BATCH_PROCESSING("Batch utility processing operations");
    
    private final String description;
    
    UtilityType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets category of the utility type
     */
    public String getCategory() {
        String name = this.name();
        int underscoreIndex = name.indexOf('_');
        return underscoreIndex > 0 ? name.substring(0, underscoreIndex).toLowerCase() : "general";
    }
    
    /**
     * Checks if this is a processing-intensive operation
     */
    public boolean isProcessingIntensive() {
        return this == FILE_EXCEL_PROCESSING || 
               this == FILE_CSV_PROCESSING ||
               this == COLLECTION_AGGREGATION ||
               this == MATHEMATICAL_CALCULATION ||
               this == STATISTICAL_CALCULATION ||
               this == ENCRYPTION ||
               this == DECRYPTION ||
               this == UTILITY_BATCH_PROCESSING;
    }
    
    /**
     * Checks if this operation requires caching
     */
    public boolean isCacheable() {
        return this == JSON_SCHEMA_VALIDATION ||
               this == BUSINESS_RULE_VALIDATION ||
               this == MATHEMATICAL_CALCULATION ||
               this == STATISTICAL_CALCULATION ||
               this == OBJECT_INTROSPECTION ||
               this == CLASS_LOADING;
    }
    
    /**
     * Checks if this is a critical operation
     */
    public boolean isCritical() {
        return this == DATA_INTEGRITY_VALIDATION ||
               this == BUSINESS_RULE_VALIDATION ||
               this == ENCRYPTION ||
               this == DECRYPTION ||
               this == FINANCIAL_CALCULATION;
    }
    
    /**
     * Checks if operation requires input data
     */
    public boolean requiresData() {
        return this != UTILITY_HEALTH_CHECK &&
               this != UTILITY_PERFORMANCE_TEST;
    }
    
    /**
     * Checks if operation requires sequential processing
     */
    public boolean requiresSequentialProcessing() {
        return this == FILE_EXCEL_PROCESSING ||
               this == FILE_CSV_PROCESSING ||
               this == UTILITY_BATCH_PROCESSING;
    }
    
    /**
     * Gets estimated processing time in milliseconds
     */
    public long getEstimatedProcessingTimeMs() {
        if (isProcessingIntensive()) return 5000L;
        if (isCacheable()) return 100L;
        return 500L;
    }
    
    /**
     * Gets resource intensity level (1-10)
     */
    public int getResourceIntensity() {
        if (isProcessingIntensive()) return 8;
        if (isCritical()) return 6;
        return 3;
    }
    
    /**
     * Gets priority score for operation scheduling
     */
    public int getPriorityScore() {
        if (isCritical()) return 10;
        if (isProcessingIntensive()) return 5;
        return 3;
    }
}