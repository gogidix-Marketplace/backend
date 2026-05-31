package com.gogidix.courier.assignmentservice.shared.requestcontext;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class RequestContextExtractor {

    private RequestContextExtractor() {
    }

    public static String extractTenantId() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) return "default";
        String tenantId = request.getHeader("X-Tenant-ID");
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "default";
    }

    public static String extractUserId() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) return "system";
        String userId = request.getHeader("X-User-ID");
        return (userId != null && !userId.isBlank()) ? userId : "anonymous";
    }

    public static String extractCorrelationId() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) return java.util.UUID.randomUUID().toString();
        String correlationId = request.getHeader("X-Correlation-ID");
        return (correlationId != null && !correlationId.isBlank()) ? correlationId : java.util.UUID.randomUUID().toString();
    }

    private static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attributes != null) ? attributes.getRequest() : null;
    }
}
