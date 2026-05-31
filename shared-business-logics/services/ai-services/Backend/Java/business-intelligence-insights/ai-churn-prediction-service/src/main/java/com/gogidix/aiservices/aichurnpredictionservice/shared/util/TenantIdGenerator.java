package com.gogidix.aiservices.aichurnpredictionservice.shared.util;

import java.util.UUID;

/**
 * Utility class for generating tenant-related identifiers.
 */
public final class TenantIdGenerator {

    private static final String TENANT_PREFIX = "tenant_";

    private TenantIdGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Generate a new unique tenant ID.
     *
     * @return a unique tenant ID
     */
    public static String generate() {
        return TENANT_PREFIX + UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Validate if a string is a valid tenant ID format.
     *
     * @param tenantId the tenant ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            return false;
        }
        return tenantId.startsWith(TENANT_PREFIX) && tenantId.length() > TENANT_PREFIX.length();
    }

    /**
     * Extract the UUID portion from a tenant ID.
     *
     * @param tenantId the full tenant ID
     * @return the UUID portion, or null if invalid
     */
    public static String extractUuid(String tenantId) {
        if (!isValid(tenantId)) {
            return null;
        }
        return tenantId.substring(TENANT_PREFIX.length());
    }
}
