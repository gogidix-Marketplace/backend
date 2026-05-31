package com.gogidix.courier.gpstrackingservice.infrastructure.config;

import com.gogidix.courier.gpstrackingservice.shared.context.RequestContext;
import com.gogidix.courier.gpstrackingservice.shared.context.RequestContextHolder;
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
import java.util.Enumeration;

/**
 * Web configuration for GPS Tracking Service.
 */
@Configuration
public class WebConfig {

    /**
     * CORS configuration.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("X-Correlation-Id", "X-Request-Id")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }

    /**
     * Request context filter.
     */
    @Bean
    public OncePerRequestFilter requestContextFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain filterChain) throws ServletException, IOException {

                String tenantId = request.getHeader("X-Tenant-Id");
                if (tenantId == null || tenantId.isEmpty()) {
                    tenantId = request.getParameter("tenantId");
                }
                if (tenantId == null || tenantId.isEmpty()) {
                    tenantId = "default";
                }

                String userId = request.getHeader("X-User-Id");
                if (userId == null || userId.isEmpty()) {
                    userId = request.getParameter("userId");
                }
                if (userId == null || userId.isEmpty()) {
                    userId = "system";
                }

                String correlationId = request.getHeader("X-Correlation-Id");
                if (correlationId == null || correlationId.isEmpty()) {
                    correlationId = request.getHeader("X-Request-Id");
                }

                RequestContext context = RequestContext.createWithCorrelationId(
                        tenantId,
                        userId,
                        correlationId
                );

                RequestContextHolder.setContext(context);

                response.setHeader("X-Correlation-Id", context.correlationId());
                response.setHeader("X-Tenant-Id", context.tenantId());

                try {
                    filterChain.doFilter(request, response);
                } finally {
                    RequestContextHolder.clearContext();
                }
            }
        };
    }
}
