package com.gogidix.sales.countrydashboard.infrastructure.config;

import com.gogidix.sales.countrydashboard.shared.requestcontext.RequestContext;
import com.gogidix.sales.countrydashboard.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

/**
 * Web Configuration
 * Configures web components including request context filter
 */
@Configuration
@Slf4j
public class WebConfig {

    @Bean
    @RequestScope
    public RequestContext requestContext(HttpServletRequest request) {
        return RequestContext.builder()
                .tenantId(extractHeader(request, "X-Tenant-Id"))
                .userId(extractHeader(request, "X-User-Id"))
                .countryCode(extractHeader(request, "X-Country-Code"))
                .correlationId(extractHeader(request, "X-Correlation-Id"))
                .region(extractHeader(request, "X-Region"))
                .build();
    }

    @Bean
    public OncePerRequestFilter requestContextFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                           jakarta.servlet.http.HttpServletResponse response,
                                           jakarta.servlet.FilterChain filterChain) throws IOException, jakarta.servlet.ServletException {

                RequestContext context = RequestContext.builder()
                        .tenantId(extractHeader(request, "X-Tenant-Id"))
                        .userId(extractHeader(request, "X-User-Id"))
                        .countryCode(extractHeader(request, "X-Country-Code"))
                        .correlationId(extractHeader(request, "X-Correlation-Id"))
                        .region(extractHeader(request, "X-Region"))
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

    private String extractHeader(HttpServletRequest request, String headerName) {
        Enumeration<String> headers = request.getHeaders(headerName);
        if (headers != null && headers.hasMoreElements()) {
            return headers.nextElement();
        }
        return null;
    }
}
