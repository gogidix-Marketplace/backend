package com.gogidix.ecommerce.loyalty.infrastructure.config;

import com.gogidix.ecommerce.loyalty.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.loyalty.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Configuration
@ConditionalOnWebApplication
public class WebConfig {

    @Bean
    public RequestContextFilter requestContextFilter() {
        return new RequestContextFilter();
    }

    public static class RequestContextFilter extends OncePerRequestFilter {

        private static final String TENANT_ID_HEADER = "X-Tenant-ID";
        private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            String tenantId = request.getHeader(TENANT_ID_HEADER);
            String correlationId = request.getHeader(CORRELATION_ID_HEADER);
            if (tenantId == null || tenantId.isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing required header: " + TENANT_ID_HEADER);
                return;
            }
            if (correlationId == null || correlationId.isBlank()) correlationId = UUID.randomUUID().toString();
            RequestContextHolder.set(new RequestContext(tenantId, null, null, correlationId, null));
            try { filterChain.doFilter(request, response); }
            finally { RequestContextHolder.clear(); }
        }
    }
}
