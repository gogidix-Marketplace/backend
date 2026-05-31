package com.gogidix.sales.territory.infrastructure.config;

import com.gogidix.sales.territory.shared.requestcontext.RequestContext;
import com.gogidix.sales.territory.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Web Configuration
 * Sets up request context filtering and MDC logging
 */
@Configuration
public class WebConfig {

    @Bean
    public OncePerRequestFilter requestContextFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain)
                    throws ServletException, IOException {

                try {
                    String tenantId = request.getHeader("X-Tenant-ID");
                    String userId = request.getHeader("X-User-ID");
                    String correlationId = request.getHeader("X-Correlation-ID");

                    if (correlationId == null || correlationId.isEmpty()) {
                        correlationId = UUID.randomUUID().toString();
                    }

                    RequestContext context = RequestContext.builder()
                        .tenantId(tenantId != null ? tenantId : "default")
                        .userId(userId != null ? userId : "system")
                        .correlationId(correlationId)
                        .organizationId(request.getHeader("X-Organization-ID"))
                        .userAgent(request.getHeader("User-Agent"))
                        .build();

                    RequestContextHolder.set(context);

                    MDC.put("tenantId", context.getTenantId());
                    MDC.put("correlationId", context.getCorrelationId());
                    MDC.put("traceId", UUID.randomUUID().toString());

                    response.setHeader("X-Correlation-ID", correlationId);

                    filterChain.doFilter(request, response);

                } finally {
                    RequestContextHolder.clear();
                    MDC.clear();
                }
            }
        };
    }
}
