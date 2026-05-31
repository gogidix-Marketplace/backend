package com.gogidix.aiservices.aigatewayservice.shared.requestcontext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

/**
 * Filter to extract and set request context from incoming HTTP requests.
 */
@Component("tenantRequestContextFilter")
public class RequestContextFilter extends OncePerRequestFilter {

    private static final String TENANT_ID_HEADER = "X-Tenant-Id";
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String tenantId = request.getHeader(TENANT_ID_HEADER);
        String userId = request.getHeader(USER_ID_HEADER);
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);

        if (tenantId == null || tenantId.isBlank()) {
            tenantId = "system";
        }
        if (userId == null || userId.isBlank()) {
            userId = "system";
        }

        RequestContext context = RequestContext.createWithCorrelationId(tenantId, userId, correlationId);
        RequestContextHolder.setContext(context);

        try {
            filterChain.doFilter(request, response);
        } finally {
            RequestContextHolder.clearContext();
        }
    }
}
