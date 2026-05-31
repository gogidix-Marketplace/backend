package com.gogidix.management.infrastructure.security;

import com.gogidix.management.shared.requestcontext.RequestContext;
import com.gogidix.management.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * TenantInterceptor - Extracts tenant context from JWT and sets RequestContext
 *
 * <p>This interceptor MUST run FIRST (highest precedence) before any other filters or controllers.
 * It extracts the tenant ID from the JWT token, validates it, and sets up the RequestContext
 * for the current thread.</p>
 *
 * <p>Request flow:</p>
 * <ol>
 *   <li>Extract JWT from Authorization header</li>
 *   <li>Validate JWT and extract claims</li>
 *   <li>Extract tenantId from claims</li>
 *   <li>Validate tenant exists and is active</li>
 *   <li>Build and set RequestContext</li>
 *   <li>Add correlationId to response</li>
 * </ol>
 *
 * <p>After request completes, the context is automatically cleared to prevent memory leaks.</p>
 *
 * @see com.gogidix.management.shared.requestcontext.RequestContext
 * @see com.gogidix.management.shared.requestcontext.RequestContextHolder
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TenantInterceptor.class);

    private static final String TENANT_ID_CLAIM = "tenant_id";
    private static final String REGION_CLAIM = "region";
    private static final String COUNTRY_CLAIM = "country";
    private static final String TRACE_ID_HEADER = "X-Trace-ID";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    private final JwtTokenValidator jwtValidator;
    private final TenantService tenantService;

    public TenantInterceptor(JwtTokenValidator jwtValidator, TenantService tenantService) {
        this.jwtValidator = jwtValidator;
        this.tenantService = tenantService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) throws Exception {

        try {
            // 1. Extract JWT from Authorization header
            String token = extractJwt(request);
            if (token == null) {
                log.warn("Missing JWT token in request: {}", request.getRequestURI());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing JWT token");
                return false;
            }

            // 2. Validate JWT and extract claims
            JwtClaims claims = jwtValidator.validate(token);

            // 3. Extract tenantId from claims
            String tenantId = claims.getClaim(TENANT_ID_CLAIM);
            if (tenantId == null || tenantId.isBlank()) {
                log.warn("Missing tenant_id in JWT claims");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing tenant_id in JWT");
                return false;
            }

            // 4. Validate tenant exists and is active
            if (!tenantService.isActive(tenantId)) {
                log.warn("Tenant is not active: {}", tenantId);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Tenant is not active");
                return false;
            }

            // 5. Extract userId from JWT subject
            String userId = claims.getSubject();

            // 6. Extract or generate correlationId
            String correlationId = request.getHeader(CORRELATION_ID_HEADER);
            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }

            // 7. Extract trace ID if present
            String traceId = request.getHeader(TRACE_ID_HEADER);

            // 8. Extract region and country from claims
            String region = claims.getClaim(REGION_CLAIM);
            String country = claims.getClaim(COUNTRY_CLAIM);

            // 9. Build and set RequestContext
            RequestContext context = RequestContext.builder()
                .tenantId(tenantId)
                .userId(userId)
                .correlationId(correlationId)
                .region(region)
                .country(country)
                .traceId(traceId)
                .build();

            RequestContextHolder.set(context);

            // 10. Add correlationId to MDC for logging
            MDC.put("tenantId", tenantId);
            MDC.put("correlationId", correlationId);
            MDC.put("userId", userId != null ? userId : "anonymous");
            if (traceId != null) {
                MDC.put("traceId", traceId);
            }

            // 11. Add correlationId to response for tracing
            response.setHeader(CORRELATION_ID_HEADER, correlationId);
            if (traceId != null) {
                response.setHeader(TRACE_ID_HEADER, traceId);
            }

            log.debug("RequestContext set for tenant: {}, user: {}, correlationId: {}",
                tenantId, userId, correlationId);

            return true;

        } catch (Exception e) {
            log.error("Error in TenantInterceptor", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication failed");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                              HttpServletResponse response,
                              Object handler,
                              Exception ex) {

        // ALWAYS clear context and MDC to prevent memory leaks
        try {
            RequestContextHolder.clear();
            MDC.clear();
        } catch (Exception e) {
            log.error("Error clearing RequestContext", e);
        }
    }

    /**
     * Extract JWT from Authorization header.
     *
     * @param request the HTTP request
     * @return the JWT token, or null if not found
     */
    private String extractJwt(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

/**
 * JWT Token Validator interface.
 * Implementations should validate JWT tokens and return claims.
 */
interface JwtTokenValidator {
    JwtClaims validate(String token) throws JwtValidationException;
}

/**
 * JWT Claims holder.
 */
class JwtClaims {
    private final java.util.Map<String, Object> claims;

    public JwtClaims(java.util.Map<String, Object> claims) {
        this.claims = java.util.Map.copyOf(claims);
    }

    @SuppressWarnings("unchecked")
    public <T> T getClaim(String name) {
        return (T) claims.get(name);
    }

    public String getSubject() {
        return getClaim("sub");
    }
}

/**
 * JWT Validation Exception.
 */
class JwtValidationException extends Exception {
    public JwtValidationException(String message) {
        super(message);
    }

    public JwtValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * Tenant Service interface.
 * Implementations should validate tenant status.
 */
interface TenantService {
    boolean isActive(String tenantId);
}
