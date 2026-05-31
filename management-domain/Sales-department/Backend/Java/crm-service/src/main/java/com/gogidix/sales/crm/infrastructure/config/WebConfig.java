package com.gogidix.sales.crm.infrastructure.config;

import com.gogidix.sales.crm.shared.requestcontext.RequestContext;
import com.gogidix.sales.crm.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.UUID;

/**
 * Web Configuration
 * Configures filters and web-related settings
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    /**
     * Filter to set up request context from HTTP headers
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public Filter requestContextFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            jakarta.servlet.http.HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {

                try {
                    String tenantId = request.getHeader("X-Tenant-ID");
                    String userId = request.getHeader("X-User-ID");
                    String correlationId = request.getHeader("X-Correlation-ID");

                    if (correlationId == null || correlationId.isBlank()) {
                        correlationId = UUID.randomUUID().toString();
                    }

                    RequestContext context = RequestContext.builder()
                        .tenantId(tenantId != null ? tenantId : "default")
                        .userId(userId != null ? userId : "system")
                        .correlationId(correlationId)
                        .requestId(UUID.randomUUID().toString())
                        .userAgent(request.getHeader("User-Agent"))
                        .ipAddress(getClientIpAddress(request))
                        .build();

                    RequestContextHolder.set(context);

                    // Add correlation ID to response
                    response.setHeader("X-Correlation-ID", correlationId);

                    log.debug("Request context set - tenantId: {}, userId: {}, correlationId: {}",
                        tenantId, userId, correlationId);

                } catch (Exception e) {
                    log.warn("Failed to set request context: {}", e.getMessage());
                }

                try {
                    filterChain.doFilter(request, response);
                } finally {
                    RequestContextHolder.clear();
                }
            }

            private String getClientIpAddress(HttpServletRequest request) {
                String xForwardedFor = request.getHeader("X-Forwarded-For");
                if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                    return xForwardedFor.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }
        };
    }
}
