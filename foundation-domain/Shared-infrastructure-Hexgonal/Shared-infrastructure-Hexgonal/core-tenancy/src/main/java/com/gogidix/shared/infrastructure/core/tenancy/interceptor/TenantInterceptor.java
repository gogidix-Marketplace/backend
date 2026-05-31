package com.gogidix.shared.infrastructure.core.tenancy.interceptor;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to extract tenant ID from HTTP headers and set it in the tenant context.
 * <p>
 * This interceptor automatically extracts the tenant ID from incoming HTTP requests
 * and makes it available throughout the request processing via TenantContextHolder.
 * <p>
 * Header Priority:
 * <ol>
 *   <li>X-Tenant-ID (primary header)</li>
 *   <li>X-Tenant (fallback header)</li>
 * </ol>
 * <p>
 * The tenant context is automatically cleared after request completion to prevent
 * memory leaks in thread pools.
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final String HEADER_TENANT_ID = "X-Tenant-ID";
    private static final String HEADER_TENANT = "X-Tenant";

    private final TenantContextHolder tenantContextHolder;

    /**
     * Creates a new TenantInterceptor.
     *
     * @param tenantContextHolder the tenant context holder
     */
    public TenantInterceptor(TenantContextHolder tenantContextHolder) {
        this.tenantContextHolder = tenantContextHolder;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        String tenantId = request.getHeader(HEADER_TENANT_ID);

        // Fallback to secondary header
        if (tenantId == null || tenantId.trim().isEmpty()) {
            tenantId = request.getHeader(HEADER_TENANT);
        }

        // Set tenant context if a valid tenant ID is found
        if (tenantId != null && !tenantId.trim().isEmpty()) {
            tenantContextHolder.setTenantId(tenantId);
        }

        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        // Clear tenant context to prevent memory leaks
        tenantContextHolder.clear();
    }
}
