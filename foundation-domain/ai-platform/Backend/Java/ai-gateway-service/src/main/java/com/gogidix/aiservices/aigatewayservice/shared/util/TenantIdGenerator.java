package com.gogidix.aiservices.aigatewayservice.shared.util;

import java.util.UUID;

/**
 * Utility class for generating tenant IDs.
 */
public final class TenantIdGenerator {

    private TenantIdGenerator() {
    }

    /**
     * Generate a new unique tenant ID.
     *
     * @return the generated tenant ID
     */
    public static String generate() {
        return "tenant_" + UUID.randomUUID();
    }

    /**
     * Generate a tenant ID with a specific prefix.
     *
     * @param prefix the prefix to use
     * @return the generated tenant ID
     */
    public static String generate(String prefix) {
        return prefix + "_" + UUID.randomUUID();
    }
}
