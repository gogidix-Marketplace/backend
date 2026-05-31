package com.gogidix.finance.cashflow.infrastructure.config;

import com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder;
import com.gogidix.finance.cashflow.shared.requestcontext.RequestContext;
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
 * Configures CORS and request context filter
 */
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("X-Tenant-ID", "X-Correlation-ID");
            }
        };
    }

    @Bean
    public OncePerRequestFilter RequestContextFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                           HttpServletResponse response,
                                           FilterChain filterChain)
                    throws ServletException, IOException {

                String tenantId = request.getHeader("X-Tenant-ID");
                String userId = request.getHeader("X-User-ID");
                String correlationId = request.getHeader("X-Correlation-ID");

                if (tenantId == null || tenantId.isBlank()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "X-Tenant-ID header is required");
                    return;
                }

                RequestContext context = RequestContext.builder()
                        .tenantId(tenantId)
                        .userId(userId != null ? userId : "anonymous")
                        .correlationId(correlationId != null ? correlationId : UUID.randomUUID().toString())
                        .build();

                RequestContextHolder.set(context);

                response.setHeader("X-Correlation-ID", context.correlationId());

                try {
                    filterChain.doFilter(request, response);
                } finally {
                    RequestContextHolder.clear();
                }
            }
        };
    }
}
