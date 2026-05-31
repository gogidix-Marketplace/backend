package com.gogidix.shared.security.context;

import com.gogidix.shared.security.domain.model.JwtSecurityContext;
import com.gogidix.shared.security.domain.model.SecurityLevel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Facade for JwtSecurityContext - provides simpler import path and more generic naming.
 * <p>
 * This class provides backward compatibility for services expecting RequestContext
 * with simpler package structure. All functionality is delegated to the canonical
 * {@link com.gogidix.shared.security.domain.model.JwtSecurityContext} class
 * in the domain.model package.
 * </p>
 * <p>
 * <strong>Note:</strong> For builder pattern access, use:
 * <pre>{@code
 * import com.gogidix.shared.security.domain.model.JwtSecurityContext;
 * JwtSecurityContext.Builder builder = JwtSecurityContext.builder();
 * }</pre>
 * </p>
 * <p>
 * <strong>ThreadLocal Context Management:</strong> This class provides static methods
 * for storing and retrieving request context in ThreadLocal storage, enabling
 * context propagation across service layers without explicit parameter passing.
 * </p>
 *
 * @deprecated Use {@link com.gogidix.shared.security.domain.model.JwtSecurityContext} instead.
 *             This facade exists for backward compatibility with existing service imports.
 */
@Deprecated
public class RequestContext extends JwtSecurityContext {

    /**
     * ThreadLocal storage for the current request context.
     * Each thread has its own isolated context instance.
     */
    private static final ThreadLocal<RequestContext> CONTEXTHolder = new ThreadLocal<>();

    /**
     * ThreadLocal storage for tenant ID.
     */
    private static final ThreadLocal<String> TENANT_ID_HOLDER = new ThreadLocal<>();

    /**
     * ThreadLocal storage for correlation ID.
     */
    private static final ThreadLocal<String> CORRELATION_ID_HOLDER = new ThreadLocal<>();

    /**
     * Creates a new RequestContext with basic parameters.
     *
     * @param subject the subject (user ID)
     * @param issuer the issuer
     * @param audience the audience set
     * @param issuedAt when it was issued
     * @param expiresAt when it expires
     * @param roles the roles
     * @param permissions the permissions
     * @param securityLevel the security level
     * @param sessionId the session ID
     */
    public RequestContext(String subject, String issuer, Set<String> audience,
                         LocalDateTime issuedAt, LocalDateTime expiresAt,
                         Set<String> roles, Set<String> permissions,
                         SecurityLevel securityLevel, String sessionId) {
        super(subject, issuer, audience, issuedAt, expiresAt,
              roles, permissions, securityLevel, sessionId);
    }

    /**
     * Creates a new RequestContext with full parameters.
     */
    public RequestContext(String subject, String issuer, Set<String> audience,
                         LocalDateTime issuedAt, LocalDateTime expiresAt, LocalDateTime notBefore, String jwtId,
                         Set<String> roles, Set<String> permissions, Set<String> authorities,
                         SecurityLevel securityLevel, String sessionId,
                         String authenticationMethod, LocalDateTime authenticationTime,
                         boolean multiFactorAuthenticated, String mfaMethod,
                         String clientId, String deviceId, String ipAddress, String userAgent, String geolocation,
                         Map<String, Object> customClaims) {
        super(subject, issuer, audience, issuedAt, expiresAt, notBefore, jwtId,
              roles, permissions, authorities, securityLevel, sessionId,
              authenticationMethod, authenticationTime,
              multiFactorAuthenticated, mfaMethod,
              clientId, deviceId, ipAddress, userAgent, geolocation,
              customClaims);
    }

    /**
     * Creates a RequestContext from an existing JwtSecurityContext.
     *
     * @param context the JWT security context
     * @return a new RequestContext
     */
    public static RequestContext from(JwtSecurityContext context) {
        return new RequestContext(
            context.getSubject(), context.getIssuer(), context.getAudience(),
            context.getIssuedAt(), context.getExpiresAt(),
            context.getNotBefore(), context.getJwtId(),
            context.getRoles(), context.getPermissions(), context.getAuthorities(),
            context.getSecurityLevel(), context.getSessionId(),
            context.getAuthenticationMethod(), context.getAuthenticationTime(),
            context.isMultiFactorAuthenticated(), context.getMfaMethod(),
            context.getClientId(), context.getDeviceId(), context.getIpAddress(),
            context.getUserAgent(), context.getGeolocation(),
            context.getCustomClaims()
        );
    }

    /**
     * Creates a standard request context.
     *
     * @param subject the subject (user ID)
     * @param issuer the issuer
     * @param roles the roles
     * @param expiration expiration duration
     * @return a new RequestContext
     */
    public static RequestContext createStandard(String subject, String issuer,
                                               Set<String> roles, Duration expiration) {
        return from(JwtSecurityContext.createStandardJwt(subject, issuer, roles, expiration));
    }

    /**
     * Creates a high security request context.
     *
     * @param subject the subject (user ID)
     * @param issuer the issuer
     * @param roles the roles
     * @param permissions the permissions
     * @param expiration expiration duration
     * @param deviceId the device ID
     * @param ipAddress the IP address
     * @return a new RequestContext
     */
    public static RequestContext createHighSecurity(String subject, String issuer,
                                                    Set<String> roles, Set<String> permissions,
                                                    Duration expiration, String deviceId, String ipAddress) {
        return from(JwtSecurityContext.createHighSecurityJwt(subject, issuer, roles, permissions,
                                                             expiration, deviceId, ipAddress));
    }

    /**
     * Gets the current tenant ID from the security context.
     * <p>
     * This method extracts the tenant ID from the custom claims or generates it from the
     * client/device information. If no tenant ID is explicitly set, it derives one from
     * the client ID or device ID for multi-tenant isolation.
     * </p>
     *
     * @return the tenant ID, never null
     */
    public String getTenantId() {
        // Try to get tenantId from custom claims first
        Object tenantId = getCustomClaims() != null ? getCustomClaims().get("tenantId") : null;
        if (tenantId != null) {
            return tenantId.toString();
        }

        // Derive from clientId or deviceId
        String clientId = getClientId();
        if (clientId != null && !clientId.isEmpty()) {
            return clientId;
        }

        // Fallback to deviceId (remove dashes for UUID format)
        String deviceId = getDeviceId();
        if (deviceId != null && !deviceId.isEmpty()) {
            return deviceId;
        }

        // Final fallback - generate from subject
        String subject = getSubject();
        if (subject != null && subject.contains("|")) {
            // Subject format might be "tenantId|userId"
            return subject.split("\\|")[0];
        }

        return "default-tenant";
    }

    /**
     * Gets the tenant ID as a UUID for type safety.
     *
     * @return the tenant ID as UUID
     * @throws IllegalArgumentException if tenant ID is not a valid UUID
     */
    public UUID getTenantIdAsUUID() {
        String tenantId = getTenantId();
        try {
            return UUID.fromString(tenantId);
        } catch (IllegalArgumentException e) {
            // If not a valid UUID, return a default one
            return UUID.nameUUIDFromBytes(tenantId.getBytes());
        }
    }

    // ==================== ThreadLocal Context Management ====================

    /**
     * Sets the tenant ID for the current thread.
     * <p>
     * This method stores the tenant ID in ThreadLocal storage, making it available
     * to downstream service calls without explicit parameter passing.
     * </p>
     *
     * @param tenantId the tenant ID to set, may be null to clear
     */
    public static void setTenantId(String tenantId) {
        if (tenantId == null) {
            TENANT_ID_HOLDER.remove();
        } else {
            TENANT_ID_HOLDER.set(tenantId);
        }
    }

    /**
     * Gets the tenant ID for the current thread.
     *
     * @return the tenant ID, or null if not set
     */
    public static String getTenantIdFromThreadLocal() {
        return TENANT_ID_HOLDER.get();
    }

    /**
     * Sets the correlation ID for the current thread.
     * <p>
     * This method stores the correlation ID in ThreadLocal storage, enabling
     * distributed tracing and request correlation across service boundaries.
     * </p>
     *
     * @param correlationId the correlation ID to set, may be null to clear
     */
    public static void setCorrelationId(String correlationId) {
        if (correlationId == null) {
            CORRELATION_ID_HOLDER.remove();
        } else {
            CORRELATION_ID_HOLDER.set(correlationId);
        }
    }

    /**
     * Gets the correlation ID for the current thread.
     *
     * @return the correlation ID, or null if not set
     */
    public static String getCorrelationIdFromThreadLocal() {
        return CORRELATION_ID_HOLDER.get();
    }

    /**
     * Clears the ThreadLocal context for the current thread.
     * <p>
     * This method should be called at the end of request processing to prevent
     * memory leaks in thread pool environments. It removes all context values
     * from ThreadLocal storage.
     * </p>
     */
    public static void clear() {
        CONTEXTHolder.remove();
        TENANT_ID_HOLDER.remove();
        CORRELATION_ID_HOLDER.remove();
    }

    /**
     * Sets the current RequestContext for the current thread.
     *
     * @param context the context to set, may be null to clear
     */
    public static void set(RequestContext context) {
        if (context == null) {
            CONTEXTHolder.remove();
        } else {
            CONTEXTHolder.set(context);
        }
    }

    /**
     * Gets the current RequestContext for the current thread.
     *
     * @return the current context, or null if not set
     */
    public static RequestContext get() {
        return CONTEXTHolder.get();
    }

    /**
     * Checks if a context is set for the current thread.
     *
     * @return true if a context is set, false otherwise
     */
    public static boolean isSet() {
        return CONTEXTHolder.get() != null;
    }
}
