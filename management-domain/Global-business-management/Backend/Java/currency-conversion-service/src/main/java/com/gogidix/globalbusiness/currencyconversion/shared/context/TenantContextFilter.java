package com.gogidix.globalbusiness.currencyconversion.shared.context;

import com.gogidix.globalbusinessmanagement.currencyconversion.shared.requestcontext.RequestContext;
import com.gogidix.globalbusinessmanagement.currencyconversion.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String tenantId = request.getHeader("X-Tenant-Id");
            if (tenantId != null && !tenantId.isBlank()) {
                RequestContext context = RequestContext.builder()
                        .tenantId(tenantId)
                        .userId(request.getHeader("X-User-Id"))
                        .correlationId(request.getHeader("X-Correlation-Id"))
                        .metadata(Map.of("requestUri", request.getRequestURI()))
                        .build();
                RequestContextHolder.set(context);
            }
            filterChain.doFilter(request, response);
        } finally {
            RequestContextHolder.clear();
        }
    }
}
