package com.gogidix.management.shared.infrastructure.security;

import com.gogidix.management.shared.requestcontext.RequestContext;
import com.gogidix.management.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantContextFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(TenantContextFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String tenantId = request.getHeader("X-Tenant-ID");
            if (tenantId == null || tenantId.isBlank()) {
                tenantId = "default";
            }
            String correlationId = request.getHeader("X-Correlation-ID");
            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }
            RequestContext context = RequestContext.builder()
                .tenantId(tenantId)
                .correlationId(correlationId)
                .build();
            RequestContextHolder.set(context);
            response.setHeader("X-Correlation-ID", correlationId);
            filterChain.doFilter(request, response);
        } finally {
            RequestContextHolder.clear();
        }
    }
}
