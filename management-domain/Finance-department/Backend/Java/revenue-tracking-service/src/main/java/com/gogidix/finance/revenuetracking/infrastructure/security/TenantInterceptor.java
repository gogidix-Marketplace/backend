package com.gogidix.finance.revenuetracking.infrastructure.security;

import com.gogidix.finance.revenuetracking.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Tenant Interceptor
 * Validates and logs tenant context for each request
 */
@Component
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader("X-Tenant-ID");
        String userId = request.getHeader("X-User-ID");

        if (tenantId != null && !tenantId.isBlank()) {
            log.debug("Processing request for tenant: {} by user: {}", tenantId, userId);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        try {
            String tenantId = RequestContextHolder.getTenantId();
            String correlationId = RequestContextHolder.getCorrelationId();
            log.debug("Completed request for tenant: {} with correlation: {}", tenantId, correlationId);
        } catch (Exception e) {
            // Context already cleared
        }
    }
}
