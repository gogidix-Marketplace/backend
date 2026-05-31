package com.gogidix.aiservices.aichurnpredictionservice.shared.util;

/**
 * Utility class for generating correlation IDs for request tracing.
 */
public final class CorrelationIdGenerator {

    private static final int CORRELATION_ID_LENGTH = 32;

    private CorrelationIdGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Generate a new correlation ID.
     *
     * @return a unique correlation ID (32-character hex string)
     */
    public static String generate() {
        return UUIDWithoutDashes();
    }

    /**
     * Generate a correlation ID with a prefix.
     *
     * @param prefix the prefix to add
     * @return a correlation ID with prefix
     */
    public static String generateWithPrefix(String prefix) {
        return prefix + "_" + generate();
    }

    /**
     * Validate if a string is a valid correlation ID format.
     *
     * @param correlationId the correlation ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String correlationId) {
        if (correlationId == null || correlationId.isBlank()) {
            return false;
        }
        // Check if it's a 32-character hex string
        return correlationId.matches("^[a-fA-F0-9]{32}$");
    }

    /**
     * Generate a UUID without dashes.
     *
     * @return a 32-character hex string
     */
    private static String UUIDWithoutDashes() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
