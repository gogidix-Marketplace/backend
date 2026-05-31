package com.gogidix.aiservices.aigatewayservice.shared.util;

import java.util.UUID;

/**
 * Utility class for generating correlation IDs.
 */
public final class CorrelationIdGenerator {

    private CorrelationIdGenerator() {
    }

    /**
     * Generate a new correlation ID.
     *
     * @return the generated correlation ID
     */
    public static String generate() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate a correlation ID with a specific prefix.
     *
     * @param prefix the prefix to use
     * @return the generated correlation ID
     */
    public static String generate(String prefix) {
        return prefix + "_" + UUID.randomUUID();
    }
}
