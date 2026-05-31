package com.gogidix.sales.leadmanagement.infrastructure.config;

import com.gogidix.sales.leadmanagement.shared.requestcontext.RequestContext;
import com.gogidix.sales.leadmanagement.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.UUID;

/**
 * Web Configuration
 * Configures request context interceptor and filters
 */
@Configuration
public class WebConfig {

    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);

    private static final String TENANT_HEADER = "X-Tenant-ID";
    private static final String USER_HEADER = "X-User-ID";
    private static final String CORRELATION_HEADER = "X-Correlation-ID";

    /**
     * Filter that extracts request context from headers
     */
    @Bean
    public OncePerRequestFilter requestContextFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                           jakarta.servlet.http.HttpServletResponse response,
                                           jakarta.servlet.FilterChain filterChain)
                    throws jakarta.servlet.ServletException, java.io.IOException {

                try {
                    String tenantId = request.getHeader(TENANT_HEADER);
                    String userId = request.getHeader(USER_HEADER);
                    String correlationId = request.getHeader(CORRELATION_HEADER);

                    if (correlationId == null || correlationId.isEmpty()) {
                        correlationId = UUID.randomUUID().toString();
                    }

                    if (tenantId != null && !tenantId.isEmpty()) {
                        RequestContext context = RequestContext.builder()
                                .tenantId(tenantId)
                                .userId(userId)
                                .correlationId(correlationId)
                                .build();

                        RequestContextHolder.set(context);

                        MDCUtils.putTenantId(tenantId);
                        MDCUtils.putCorrelationId(correlationId);
                    }

                    filterChain.doFilter(request, response);

                } finally {
                    RequestContextHolder.clear();
                    MDCUtils.clear();
                }
            }

            @Override
            protected boolean shouldNotFilter(jakarta.servlet.http.HttpServletRequest request) {
                String path = request.getRequestURI();
                return path.startsWith("/actuator") || path.startsWith("/swagger-ui") || path.startsWith("/api-docs");
            }
        };
    }

    /**
     * MDC Utilities for logging
     */
    private static class MDCUtils {
        static void putTenantId(String tenantId) {
            org.slf4j.MDC.put("tenantId", tenantId);
        }

        static void putCorrelationId(String correlationId) {
            org.slf4j.MDC.put("correlationId", correlationId);
        }

        static void clear() {
            org.slf4j.MDC.clear();
        }
    }
}
