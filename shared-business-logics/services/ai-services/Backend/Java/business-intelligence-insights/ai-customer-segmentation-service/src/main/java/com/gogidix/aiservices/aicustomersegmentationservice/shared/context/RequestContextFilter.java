package com.gogidix.aiservices.aicustomersegmentationservice.shared.context;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that sets up the request context and MDC for each request.
 */
@Component("tenantContextFilter")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestContextFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestContextFilter.class);

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

        try {
            if (tenantId != null && userId != null) {
                RequestContext context = RequestContext.createWithCorrelationId(
                        tenantId,
                        userId,
                        correlationId
                );
                RequestContextHolder.setContext(context);

                MDC.put("tenantId", tenantId);
                MDC.put("userId", userId);
                MDC.put("correlationId", context.correlationId());

                response.setHeader(CORRELATION_ID_HEADER, context.correlationId());
            }

            filterChain.doFilter(request, response);

        } finally {
            MDC.clear();
            RequestContextHolder.clear();
        }
    }
}
