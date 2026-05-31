package com.gogidix.courier.routingservice.shared.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Holder for the current request context.
 * Provides thread-safe access to tenant and user information.
 */
public class RequestContextHolder {

    private static final Logger log = LoggerFactory.getLogger(RequestContextHolder.class);
    private static final String TENANT_ID_HEADER = "X-Tenant-Id";
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String DEFAULT_TENANT_ID = "default";
    private static final String DEFAULT_USER_ID = "anonymous";

    /**
     * Get the current request context.
     * Creates a default context if headers are not present.
     *
     * @return the request context
     */
    public static RequestContext getContext() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            log.debug("No request attributes found, returning anonymous context");
            return RequestContext.anonymous();
        }

        jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();

        String tenantId = request.getHeader(TENANT_ID_HEADER);
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = DEFAULT_TENANT_ID;
            log.debug("No tenant ID header found, using default: {}", DEFAULT_TENANT_ID);
        }

        String userId = request.getHeader(USER_ID_HEADER);
        if (userId == null || userId.isBlank()) {
            userId = DEFAULT_USER_ID;
        }

        String correlationId = request.getHeader(CORRELATION_ID_HEADER);

        return RequestContext.createWithCorrelationId(tenantId, userId, correlationId);
    }

    /**
     * Get the tenant ID from the current request.
     *
     * @return the tenant ID
     */
    public static String getTenantId() {
        return getContext().tenantId();
    }

    /**
     * Get the user ID from the current request.
     *
     * @return the user ID
     */
    public static String getUserId() {
        return getContext().userId();
    }

    /**
     * Get the correlation ID from the current request.
     *
     * @return the correlation ID
     */
    public static String getCorrelationId() {
        return getContext().correlationId();
    }

    /**
     * Set the current request context (for testing).
     *
     * @param context the context to set
     */
    public static void setContext(RequestContext context) {
        // This method is primarily for testing purposes
        log.debug("Setting request context: tenant={}, user={}", context.tenantId(), context.userId());
    }
}
