package com.gogidix.finance.forecasting.infrastructure.security;

import com.gogidix.finance.forecasting.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Tenant Interceptor
 * Intercepts HTTP requests to enforce multi-tenant isolation and validate tenant context
 */
@Component
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_ID_HEADER = "X-Tenant-ID";
    private static final String USER_ID_HEADER = "X-User-ID";

    /**
     * Pre-handle method to validate tenant context before request processing
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param handler the handler
     * @return true if the request should proceed, false otherwise
     * @throws Exception if an error occurs
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {

        String tenantId = request.getHeader(TENANT_ID_HEADER);
        String userId = request.getHeader(USER_ID_HEADER);
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();

        // Log request with tenant context
        if (tenantId != null && !tenantId.isBlank()) {
            log.debug("Incoming request: {} {} for tenant: {}, user: {}",
                    requestMethod, requestUri, tenantId, userId);
        } else {
            log.warn("Request missing tenant header: {} {}", requestMethod, requestUri);
        }

        // Validate tenant context is set
        try {
            String contextTenantId = RequestContextHolder.getTenantId();
            if (contextTenantId == null || contextTenantId.isBlank()) {
                log.error("Tenant context not set for request: {} {}", requestMethod, requestUri);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Tenant context not initialized\"}");
                return false;
            }

            // Validate tenant ID matches header
            if (tenantId != null && !tenantId.equals(contextTenantId)) {
                log.error("Tenant ID mismatch: header={}, context={}", tenantId, contextTenantId);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Tenant ID mismatch\"}");
                return false;
            }

        } catch (IllegalStateException e) {
            log.error("Request context not available: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Request context not available\"}");
            return false;
        }

        return true;
    }

    /**
     * Post-handle method to add tenant-related headers to response
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param handler the handler
     * @param ex any exception thrown during handler execution
     */
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {

        try {
            String tenantId = RequestContextHolder.getTenantId();
            if (tenantId != null) {
                // Add tenant header to response for visibility
                if (!response.containsHeader(TENANT_ID_HEADER)) {
                    response.setHeader(TENANT_ID_HEADER, tenantId);
                }
            }
        } catch (IllegalStateException e) {
            // Context already cleared, ignore
        }
    }

    /**
     * Checks if the request path is exempt from tenant validation
     *
     * @param requestUri the request URI
     * @return true if exempt from validation
     */
    private boolean isExemptPath(String requestUri) {
        return requestUri.startsWith("/actuator")
                || requestUri.startsWith("/health")
                || requestUri.startsWith("/metrics")
                || requestUri.startsWith("/swagger")
                || requestUri.startsWith("/api-docs")
                || requestUri.startsWith("/error");
    }

    /**
     * Validates tenant ID format
     *
     * @param tenantId the tenant ID to validate
     * @return true if valid
     */
    private boolean isValidTenantId(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            return false;
        }
        // Tenant ID should be alphanumeric with possible hyphens/underscores
        return tenantId.matches("^[a-zA-Z0-9_-]+$");
    }

    /**
     * Extracts user ID from request
     *
     * @param request the HTTP request
     * @return the user ID or "system" if not available
     */
    public String extractUserId(HttpServletRequest request) {
        try {
            return RequestContextHolder.getUserId().orElse("system");
        } catch (IllegalStateException e) {
            return "system";
        }
    }

    /**
     * Gets the correlation ID for the current request
     *
     * @return the correlation ID
     */
    public String getCorrelationId() {
        try {
            return RequestContextHolder.getCorrelationId();
        } catch (IllegalStateException e) {
            return java.util.UUID.randomUUID().toString();
        }
    }
}
