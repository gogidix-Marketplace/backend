package com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Filter that extracts tenant context from HTTP requests.
 * Sets up the TenantContextHolder for multi-tenant request processing.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantContextRequestFilter extends OncePerRequestFilter {

    private static final String TENANT_HEADER = "X-Tenant-Id";
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String tenantId = extractTenantId(request);
            String userId = extractUserId(request);
            String correlationId = extractCorrelationId(request);

            TenantContext context = TenantContext.builder()
                .tenantId(tenantId)
                .userId(userId)
                .correlationId(correlationId)
                .roles(Set.of("ROLE_USER"))
                .build();

            TenantContextHolder.setContext(context);
            
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clearContext();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/health") 
            || path.contains("/readiness") 
            || path.contains("/liveness")
            || path.startsWith("/actuator");
    }

    private String extractTenantId(HttpServletRequest request) {
        String tenantId = request.getHeader(TENANT_HEADER);
        if (tenantId == null || tenantId.isEmpty()) {
            tenantId = "default";
        }
        return tenantId;
    }

    private String extractUserId(HttpServletRequest request) {
        String userId = request.getHeader(USER_ID_HEADER);
        if (userId == null || userId.isEmpty()) {
            userId = "anonymous";
        }
        return userId;
    }

    private String extractCorrelationId(HttpServletRequest request) {
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }
        return correlationId;
    }
}
