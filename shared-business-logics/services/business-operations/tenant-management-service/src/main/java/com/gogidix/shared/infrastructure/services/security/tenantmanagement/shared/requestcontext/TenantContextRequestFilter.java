package com.gogidix.shared.infrastructure.services.security.tenantmanagement.shared.requestcontext;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

/**
 * Servlet filter that extracts tenant and user information from HTTP headers
 * and stores it in a ThreadLocal TenantContext for use throughout the request.
 *
 * Required Headers:
 * - X-Tenant-ID: The tenant identifier
 * - X-User-ID: The user identifier (optional)
 * - X-Correlation-ID: Correlation ID for tracing (optional)
 * - X-Roles: Comma-separated list of roles (optional)
 * - X-Username: User's name (optional)
 * - X-Email: User's email (optional)
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantContextRequestFilter implements Filter {

    private static final String TENANT_HEADER = "X-Tenant-ID";
    private static final String USER_HEADER = "X-User-ID";
    private static final String CORRELATION_HEADER = "X-Correlation-ID";
    private static final String ROLES_HEADER = "X-Roles";
    private static final String USERNAME_HEADER = "X-Username";
    private static final String EMAIL_HEADER = "X-Email";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String tenantId = httpRequest.getHeader(TENANT_HEADER);
        String userId = httpRequest.getHeader(USER_HEADER);
        String correlationId = httpRequest.getHeader(CORRELATION_HEADER);
        String rolesStr = httpRequest.getHeader(ROLES_HEADER);
        String userName = httpRequest.getHeader(USERNAME_HEADER);
        String userEmail = httpRequest.getHeader(EMAIL_HEADER);

        // Only set context if tenant ID is present
        if (tenantId != null && !tenantId.isBlank()) {
            Set<String> roles = rolesStr != null && !rolesStr.isBlank()
                ? Set.of(rolesStr.split(","))
                : Set.of();

            TenantContext context = TenantContext.builder()
                .tenantId(tenantId)
                .userId(userId)
                .correlationId(correlationId)
                .roles(roles)
                .userName(userName)
                .userEmail(userEmail)
                .build();

            TenantContextHolder.setContext(context);
            log.debug("Tenant context set: tenantId={}, userId={}, correlationId={}, roles={}",
                tenantId, userId, correlationId, roles);
        } else {
            log.warn("Request missing X-Tenant-ID header");
        }

        try {
            chain.doFilter(request, response);
        } finally {
            // Always clear context to prevent thread pool contamination
            TenantContextHolder.clearContext();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("TenantContextRequestFilter initialized");
    }

    @Override
    public void destroy() {
        log.info("TenantContextRequestFilter destroyed");
    }
}
