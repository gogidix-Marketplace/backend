package com.gogidix.sales.communication.infrastructure.config;

import com.gogidix.sales.communication.shared.requestcontext.RequestContext;
import com.gogidix.sales.communication.shared.requestcontext.RequestContextHolder;
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
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("X-Request-Id", "X-Correlation-Id")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public RequestContextFilter requestContextFilter() {
        return new RequestContextFilter();
    }

    public static class RequestContextFilter extends OncePerRequestFilter {

        private static final String TENANT_HEADER = "X-Tenant-Id";
        private static final String USER_HEADER = "X-User-Id";
        private static final String CORRELATION_HEADER = "X-Correlation-Id";
        private static final String REQUEST_ID_HEADER = "X-Request-Id";

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {

            try {
                String tenantId = request.getHeader(TENANT_HEADER);
                String userId = request.getHeader(USER_HEADER);
                String correlationId = request.getHeader(CORRELATION_HEADER);
                String requestId = request.getHeader(REQUEST_ID_HEADER);

                // Validate required headers
                if (tenantId == null || tenantId.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Missing X-Tenant-Id header");
                    return;
                }

                // Generate IDs if not provided
                if (correlationId == null || correlationId.isEmpty()) {
                    correlationId = UUID.randomUUID().toString();
                }
                if (requestId == null || requestId.isEmpty()) {
                    requestId = UUID.randomUUID().toString();
                }

                // Set response headers
                response.setHeader(CORRELATION_HEADER, correlationId);
                response.setHeader(REQUEST_ID_HEADER, requestId);

                // Build and set request context
                RequestContext context = RequestContext.builder()
                        .tenantId(tenantId)
                        .userId(userId != null ? userId : "system")
                        .correlationId(correlationId)
                        .requestId(requestId)
                        .channel("WEB")
                        .build();

                RequestContextHolder.set(context);

                filterChain.doFilter(request, response);

            } finally {
                RequestContextHolder.clear();
            }
        }
    }
}
