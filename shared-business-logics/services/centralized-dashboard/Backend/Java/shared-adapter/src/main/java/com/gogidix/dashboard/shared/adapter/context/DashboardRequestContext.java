package com.gogidix.dashboard.shared.adapter.context;

import com.gogidix.shared.security.context.RequestContext;
import java.util.Optional;

/**
 * Enterprise facade for RequestContext providing a clean, service-friendly API.
 * 
 * <p>This adapter bridges the gap between centralized-dashboard services and the
 * Foundation shared-security library, providing:
 * <ul>
 *   <li>Optional-based return types for null-safe operations</li>
 *   <li>Static convenience methods aligned with service layer expectations</li>
 *   <li>Proper ThreadLocal context management</li>
 * </ul>
 * 
 * <p><strong>Usage Example:</strong>
 * <pre>{@code
 * // Get tenant ID (returns Optional for null-safety)
 * Optional<String> tenantId = DashboardRequestContext.getTenantId();
 * String tenant = tenantId.orElseThrow(() -> new ValidationException("Tenant required"));
 * 
 * // Get user ID
 * Optional<String> userId = DashboardRequestContext.getUserId();
 * 
 * // Set context values for request processing
 * DashboardRequestContext.setTenantId("tenant-123");
 * DashboardRequestContext.setCorrelationId(UUID.randomUUID().toString());
 * 
 * // Clear context at end of request (important for thread pool hygiene)
 * DashboardRequestContext.clear();
 * }</pre>
 * 
 * @author Gogidix Dashboard Team
 * @since 1.0.0
 */
public final class DashboardRequestContext {

    private DashboardRequestContext() {
        // Utility class - prevent instantiation
    }

    /**
     * Gets the tenant ID for the current request context.
     * 
     * <p>This method first checks ThreadLocal storage, then falls back to
     * the security context if available.
     * 
     * @return Optional containing the tenant ID, or empty if not set
     */
    public static Optional<String> getTenantId() {
        // First check ThreadLocal (set by interceptors)
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId != null && !tenantId.isEmpty()) {
            return Optional.of(tenantId);
        }

        // Then check security context
        RequestContext context = RequestContext.get();
        if (context != null) {
            String contextTenantId = context.getTenantId();
            if (contextTenantId != null && !contextTenantId.isEmpty()) {
                return Optional.of(contextTenantId);
            }
        }

        return Optional.empty();
    }

    /**
     * Gets the user ID (subject) for the current request context.
     * 
     * @return Optional containing the user ID, or empty if not authenticated
     */
    public static Optional<String> getUserId() {
        RequestContext context = RequestContext.get();
        if (context != null) {
            String subject = context.getSubject();
            if (subject != null && !subject.isEmpty()) {
                return Optional.of(subject);
            }
        }
        return Optional.empty();
    }

    /**
     * Gets the correlation ID for distributed tracing.
     * 
     * @return Optional containing the correlation ID, or empty if not set
     */
    public static Optional<String> getCorrelationId() {
        String correlationId = RequestContext.getCorrelationIdFromThreadLocal();
        return Optional.ofNullable(correlationId);
    }

    /**
     * Sets the tenant ID for the current thread context.
     * 
     * <p>This should be called early in request processing, typically by
     * a filter or interceptor that extracts tenant information from the request.
     * 
     * @param tenantId the tenant ID to set
     */
    public static void setTenantId(String tenantId) {
        RequestContext.setTenantId(tenantId);
    }

    /**
     * Sets the correlation ID for distributed tracing.
     * 
     * @param correlationId the correlation ID to set
     */
    public static void setCorrelationId(String correlationId) {
        RequestContext.setCorrelationId(correlationId);
    }

    /**
     * Checks if a request context is available for the current thread.
     * 
     * @return true if context is set, false otherwise
     */
    public static boolean isContextAvailable() {
        return RequestContext.isSet();
    }

    /**
     * Clears all ThreadLocal context for the current thread.
     * 
     * <p><strong>Important:</strong> This should be called at the end of
     * request processing to prevent memory leaks in thread pool environments.
     */
    public static void clear() {
        RequestContext.clear();
    }

    /**
     * Gets the underlying RequestContext for advanced operations.
     * 
     * @return Optional containing the RequestContext, or empty if not set
     */
    public static Optional<RequestContext> getRawContext() {
        return Optional.ofNullable(RequestContext.get());
    }

    /**
     * Gets the client ID from the security context.
     * 
     * @return Optional containing the client ID, or empty if not available
     */
    public static Optional<String> getClientId() {
        RequestContext context = RequestContext.get();
        if (context != null) {
            return Optional.ofNullable(context.getClientId());
        }
        return Optional.empty();
    }

    /**
     * Gets the device ID from the security context.
     * 
     * @return Optional containing the device ID, or empty if not available
     */
    public static Optional<String> getDeviceId() {
        RequestContext context = RequestContext.get();
        if (context != null) {
            return Optional.ofNullable(context.getDeviceId());
        }
        return Optional.empty();
    }

    /**
     * Gets the IP address from the security context.
     * 
     * @return Optional containing the IP address, or empty if not available
     */
    public static Optional<String> getIpAddress() {
        RequestContext context = RequestContext.get();
        if (context != null) {
            return Optional.ofNullable(context.getIpAddress());
        }
        return Optional.empty();
    }

    /**
     * Checks if the current user has a specific role.
     * 
     * @param role the role to check
     * @return true if the user has the role, false otherwise
     */
    public static boolean hasRole(String role) {
        RequestContext context = RequestContext.get();
        if (context != null && context.getRoles() != null) {
            return context.getRoles().contains(role);
        }
        return false;
    }

    /**
     * Checks if the current user has a specific permission.
     * 
     * @param permission the permission to check
     * @return true if the user has the permission, false otherwise
     */
    public static boolean hasPermission(String permission) {
        RequestContext context = RequestContext.get();
        if (context != null && context.getPermissions() != null) {
            return context.getPermissions().contains(permission);
        }
        return false;
    }
}
