package com.gogidix.finance.bankreconciliation.infrastructure.config;

import com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContext;
import com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.UUID;

/**
 * Web Configuration
 */
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("X-Correlation-ID", "X-Tenant-ID");
            }
        };
    }

    @Bean
    public RequestContextFilter requestContextFilter() {
        return new RequestContextFilter();
    }

    public static class RequestContextFilter extends OncePerRequestFilter {

        private static final String TENANT_HEADER = "X-Tenant-ID";
        private static final String USER_HEADER = "X-User-ID";
        private static final String CORRELATION_HEADER = "X-Correlation-ID";

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                       HttpServletResponse response,
                                       FilterChain filterChain) throws ServletException, IOException {

            String tenantId = request.getHeader(TENANT_HEADER);
            String userId = request.getHeader(USER_HEADER);
            String correlationId = request.getHeader(CORRELATION_HEADER);

            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }

            // Add correlation ID to response
            response.setHeader(CORRELATION_HEADER, correlationId);

            // Set request context if tenant is present
            if (tenantId != null && !tenantId.isBlank()) {
                RequestContext context = RequestContext.builder()
                        .tenantId(tenantId)
                        .userId(userId)
                        .correlationId(correlationId)
                        .build();
                RequestContextHolder.set(context);
            }

            try {
                filterChain.doFilter(request, response);
            } finally {
                RequestContextHolder.clear();
            }
        }
    }
}
