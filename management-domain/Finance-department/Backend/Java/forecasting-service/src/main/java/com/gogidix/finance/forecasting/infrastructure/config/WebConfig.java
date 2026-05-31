package com.gogidix.finance.forecasting.infrastructure.config;

import com.gogidix.finance.forecasting.shared.requestcontext.RequestContext;
import com.gogidix.finance.forecasting.shared.requestcontext.RequestContextHolder;
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
 * Configures CORS and request context filter for multi-tenancy
 */
@Configuration
public class WebConfig {

    /**
     * Configures CORS mappings
     *
     * @return the WebMvcConfigurer with CORS configuration
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("X-Tenant-ID", "X-Correlation-ID", "X-Request-ID")
                        .maxAge(3600);
            }
        };
    }

    /**
     * Creates request context filter for multi-tenancy support
     *
     * @return the request context filter
     */
    @Bean
    public OncePerRequestFilter requestContextFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                           HttpServletResponse response,
                                           FilterChain filterChain)
                    throws ServletException, IOException {

                String tenantId = request.getHeader("X-Tenant-ID");
                String userId = request.getHeader("X-User-ID");
                String correlationId = request.getHeader("X-Correlation-ID");
                String requestId = request.getHeader("X-Request-ID");

                // Validate required tenant header
                if (tenantId == null || tenantId.isBlank()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"X-Tenant-ID header is required\"}");
                    return;
                }

                // Build request context
                RequestContext context = RequestContext.builder()
                        .tenantId(tenantId)
                        .userId(userId != null ? userId : "anonymous")
                        .correlationId(correlationId != null ? correlationId : UUID.randomUUID().toString())
                        .requestId(requestId != null ? requestId : UUID.randomUUID().toString())
                        .userAgent(request.getHeader("User-Agent"))
                        .ipAddress(getClientIpAddress(request))
                        .requestMethod(request.getMethod())
                        .requestUri(request.getRequestURI())
                        .build();

                // Set context for this request
                RequestContextHolder.set(context);

                // Add correlation ID to response
                response.setHeader("X-Correlation-ID", context.getCorrelationId());
                response.setHeader("X-Request-ID", context.getRequestId());

                try {
                    filterChain.doFilter(request, response);
                } finally {
                    // Clear context after request completes
                    RequestContextHolder.clear();
                }
            }
        };
    }

    /**
     * Extracts the client IP address from the request
     *
     * @param request the HTTP request
     * @return the client IP address
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isBlank()) {
            ipAddress = request.getHeader("X-Real-IP");
        }
        if (ipAddress == null || ipAddress.isBlank()) {
            ipAddress = request.getRemoteAddr();
        }
        // Handle multiple IPs in X-Forwarded-For
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }
}
