package com.gogidix.shared.servicediscovery.config.interceptor;

import com.gogidix.shared.servicediscovery.config.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Tenant Interceptor
 * <p>
 * MVC interceptor that extracts tenant context from HTTP headers
 * and populates the TenantContext for the current request.
 * </p>
 *
 * Expected headers:
 * - X-Tenant-ID: The tenant identifier
 * - X-Domain: Optional domain identifier
 * - X-Correlation-ID: Optional correlation ID for tracing
 *
 * @author Foundation Team
 * @version 1.0.0
 */
@Slf4j
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_ID_HEADER = "X-Tenant-ID";
    private static final String DOMAIN_HEADER = "X-Domain";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String USER_ID_HEADER = "X-User-ID";
    private static final String USER_ROLES_HEADER = "X-User-Roles";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Extract tenant ID from header
        String tenantId = request.getHeader(TENANT_ID_HEADER);
        if (tenantId != null && !tenantId.isEmpty()) {
            TenantContext.setTenantId(tenantId);
            log.trace("Tenant ID set from header: {}", tenantId);
        }

        // Extract domain from header
        String domain = request.getHeader(DOMAIN_HEADER);
        if (domain != null && !domain.isEmpty()) {
            TenantContext.setDomain(domain);
        }

        // Extract correlation ID from header
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId != null && !correlationId.isEmpty()) {
            TenantContext.setCorrelationId(correlationId);
        } else {
            // Generate correlation ID if not present
            TenantContext.setCorrelationId(generateCorrelationId());
        }

        // Extract user ID from header
        String userId = request.getHeader(USER_ID_HEADER);
        if (userId != null && !userId.isEmpty()) {
            TenantContext.setUserId(userId);
        }

        // Extract user roles from header
        String userRoles = request.getHeader(USER_ROLES_HEADER);
        if (userRoles != null && !userRoles.isEmpty()) {
            TenantContext.setUserRoles(userRoles);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                               Object handler, Exception ex) {
        // Clear tenant context after request completes
        TenantContext.clear();
        log.trace("Tenant context cleared after request");
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString().substring(0, 8);
    }
}
