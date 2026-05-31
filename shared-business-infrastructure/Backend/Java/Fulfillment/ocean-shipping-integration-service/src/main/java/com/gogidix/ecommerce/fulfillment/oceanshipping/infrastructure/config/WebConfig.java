package com.gogidix.ecommerce.fulfillment.oceanshipping.infrastructure.config;

import com.gogidix.ecommerce.fulfillment.oceanshipping.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.fulfillment.oceanshipping.shared.requestcontext.RequestContextHolder;
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
public class WebConfig {
    @Bean @ConditionalOnWebApplication
    public RequestContextFilter requestContextFilter() { return new RequestContextFilter(); }

    public static class RequestContextFilter extends OncePerRequestFilter {
        private static final String TENANT_ID_HEADER = "X-Tenant-ID";
        private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
        private static final String CUSTOMER_ID_HEADER = "X-Customer-ID";
        private static final String USER_ID_HEADER = "X-User-ID";

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            String tenantId = request.getHeader(TENANT_ID_HEADER);
            String correlationId = request.getHeader(CORRELATION_ID_HEADER);
            String customerId = request.getHeader(CUSTOMER_ID_HEADER);
            String userId = request.getHeader(USER_ID_HEADER);

            if (tenantId == null || tenantId.isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing required header: " + TENANT_ID_HEADER);
                return;
            }
            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }

            RequestContext context = RequestContext.builder()
                    .tenantId(tenantId).customerId(customerId).userId(userId).correlationId(correlationId).build();
            RequestContextHolder.set(context);
            try {
                filterChain.doFilter(request, response);
            } finally {
                RequestContextHolder.clear();
            }
        }
    }
}