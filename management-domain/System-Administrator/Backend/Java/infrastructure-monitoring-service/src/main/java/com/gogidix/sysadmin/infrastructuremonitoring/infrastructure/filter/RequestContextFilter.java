package com.gogidix.sysadmin.infrastructuremonitoring.infrastructure.filter;

import com.gogidix.sysadmin.infrastructuremonitoring.shared.requestcontext.RequestContext;
import com.gogidix.sysadmin.infrastructuremonitoring.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestContextFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestContextFilter.class);
    private static final String TENANT_ID_HEADER = "X-Tenant-Id";
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {

        String tenantId = request.getHeader(TENANT_ID_HEADER);
        String userId = request.getHeader(USER_ID_HEADER);
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);

        if (tenantId == null || tenantId.isBlank()) {
            tenantId = "default-tenant";
            logger.warn("No tenant ID provided, using default: {}", tenantId);
        }

        if (userId == null || userId.isBlank()) {
            userId = "system";
        }

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        RequestContext context = RequestContext.builder()
                .tenantId(tenantId)
                .userId(userId)
                .correlationId(correlationId)
                .build();

        RequestContextHolder.set(context);

        response.setHeader(CORRELATION_ID_HEADER, correlationId);

        try {
            logger.debug("Request context set: tenant={}, user={}, correlation={}",
                    tenantId, userId, correlationId);
            filterChain.doFilter(request, response);
        } finally {
            RequestContextHolder.clear();
        }
    }
}
