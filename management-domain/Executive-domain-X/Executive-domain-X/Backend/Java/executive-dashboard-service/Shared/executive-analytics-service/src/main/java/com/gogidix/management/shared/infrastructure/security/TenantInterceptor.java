package com.gogidix.management.shared.infrastructure.security;

import com.gogidix.management.shared.requestcontext.RequestContext;
import com.gogidix.management.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.UUID;

public class TenantInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(TenantInterceptor.class);
    private static final String TENANT_ID_CLAIM = "tenant_id";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String TRACE_ID_HEADER = "X-Trace-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String correlationId = request.getHeader(CORRELATION_ID_HEADER);
            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }
            String traceId = request.getHeader(TRACE_ID_HEADER);

            RequestContext context = RequestContext.builder()
                .tenantId("default")
                .correlationId(correlationId)
                .traceId(traceId)
                .build();
            RequestContextHolder.set(context);

            MDC.put("correlationId", correlationId);
            response.setHeader(CORRELATION_ID_HEADER, correlationId);
            if (traceId != null) {
                response.setHeader(TRACE_ID_HEADER, traceId);
            }
            return true;
        } catch (Exception e) {
            log.error("Error in TenantInterceptor", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication failed");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        RequestContextHolder.clear();
        MDC.clear();
    }

    private String extractJwt(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
