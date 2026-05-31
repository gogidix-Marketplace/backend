package com.gogidix.shared.warehousing.reorder.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Value("${multitenancy.header-name:X-Tenant-ID}")
    private String tenantHeaderName;

    @Value("${multitenancy.default-tenant:public}")
    private String defaultTenant;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(tenantHeaderName);
        if (tenantId == null || tenantId.trim().isEmpty()) {
            tenantId = defaultTenant;
        }
        TenantContext.setCurrentTenantId(tenantId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        TenantContext.clear();
    }
}
