package com.gogidix.sales.onboarding.infrastructure.config;

import com.gogidix.sales.onboarding.shared.requestcontext.RequestContext;
import com.gogidix.sales.onboarding.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Web Configuration
 * Sets up request context filtering
 */
@Configuration
@Slf4j
public class WebConfig {

    @Bean
    public OncePerRequestFilter RequestContextFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                            jakarta.servlet.http.HttpServletResponse response,
                                            jakarta.servlet.FilterChain filterChain)
                    throws jakarta.servlet.ServletException, IOException {

                String tenantId = request.getHeader("X-Tenant-ID");
                String userId = request.getHeader("X-User-ID");
                String correlationId = request.getHeader("X-Correlation-ID");
                String organizationId = request.getHeader("X-Organization-ID");

                if (correlationId == null || correlationId.isEmpty()) {
                    correlationId = UUID.randomUUID().toString();
                }

                // Set default values if not provided (for development)
                if (tenantId == null || tenantId.isEmpty()) {
                    tenantId = "default-tenant";
                    log.warn("X-Tenant-ID header not provided, using default");
                }

                if (userId == null || userId.isEmpty()) {
                    userId = "system";
                }

                RequestContext context = RequestContext.builder()
                        .tenantId(tenantId)
                        .userId(userId)
                        .correlationId(correlationId)
                        .organizationId(organizationId)
                        .requestId(UUID.randomUUID().toString())
                        .build();

                RequestContextHolder.set(context);

                try {
                    filterChain.doFilter(request, response);
                } finally {
                    RequestContextHolder.clear();
                }
            }
        };
    }
}
