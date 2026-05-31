package com.gogidix.finance.tax.infrastructure.security;

import com.gogidix.finance.tax.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Tenant Interceptor
 * Validates tenant context for all requests
 */
@Component
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = RequestContextHolder.getTenantId();

        if (tenantId == null || tenantId.isBlank()) {
            log.warn("Missing tenant ID in request context for: {}", request.getRequestURI());
            return false;
        }

        log.debug("Request processed for tenant: {} on: {}", tenantId, request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                               Object handler, Exception ex) {
        if (ex != null) {
            log.error("Request completed with error for tenant: {} on: {}",
                RequestContextHolder.getTenantId(), request.getRequestURI(), ex);
        }
    }
}
