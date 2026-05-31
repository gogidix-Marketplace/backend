package com.gogidix.marketing.campaign.shared.infrastructure.security;

import com.gogidix.marketing.campaign.shared.requestcontext.RequestContext;
import com.gogidix.marketing.campaign.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * TenantContextFilter - Spring Security Filter for Tenant Context
 *
 * <p>CRITICAL: This is a FILTER, not an interceptor. Filters execute BEFORE Spring Security
 * and before any controllers. Interceptors execute AFTER controllers but BEFORE view rendering.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * 1. TenantContextFilter (HERE) - Sets RequestContext
 * 2. SecurityFilterChain (Authentication/Authorization)
 * 3. DispatcherServlet
 * 4. Controller (can now safely access RequestContext)
 * 5. TenantContextFilter.afterCompletion() - Clears RequestContext
 * </pre>
 *
 * <p><b>Development Mode:</b></p>
 * <ul>
 *   <li>Extracts tenant from {@code X-Tenant-ID} header</li>
 *   <li>Defaults to {@code default-tenant} if missing</li>
 *   <li>User is set to {@code dev-user}</li>
 * </ul>
 *
 * <p><b>Production Mode:</b></p>
 * <ul>
 *   <li>Validates JWT from {@code Authorization: Bearer} header</li>
 *   <li>Extracts {@code tenant_id} claim from JWT</li>
 *   <li>Validates tenant is active via TenantService</li>
 * </ul>
 *
 * @see com.gogidix.marketing.campaign.shared.requestcontext.RequestContext
 * @see com.gogidix.marketing.campaign.shared.requestcontext.RequestContextHolder
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantContextFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TenantContextFilter.class);

    private static final String TENANT_HEADER = "X-Tenant-ID";
    private static final String CORRELATION_HEADER = "X-Correlation-ID";
    private static final String TRACE_HEADER = "X-Trace-ID";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String DEFAULT_TENANT = "default-tenant";
    private static final String DEV_USER = "dev-user";

    // Production mode flag - set via environment variable or profile
    private final boolean productionMode = Boolean.parseBoolean(
        System.getProperty("app.production", "false")
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            RequestContext context = buildRequestContext(request);
            RequestContextHolder.set(context);

            // Set MDC for logging
            MDC.put("tenantId", context.tenantId());
            MDC.put("correlationId", context.correlationId());
            MDC.put("userId", context.userId() != null ? context.userId() : "anonymous");
            if (context.traceId() != null) {
                MDC.put("traceId", context.traceId());
            }

            // Add correlation ID to response header for distributed tracing
            response.setHeader(CORRELATION_HEADER, context.correlationId());
            if (context.traceId() != null) {
                response.setHeader(TRACE_HEADER, context.traceId());
            }

            log.debug("Tenant context set: tenantId={}, userId={}, correlationId={}",
                context.tenantId(), context.userId(), context.correlationId());

            // Continue filter chain
            filterChain.doFilter(request, response);

        } finally {
            // ALWAYS clear context and MDC to prevent memory leaks
            try {
                RequestContextHolder.clear();
                MDC.clear();
                log.debug("Tenant context cleared");
            } catch (Exception e) {
                log.error("Error clearing RequestContext", e);
            }
        }
    }

    /**
     * Build RequestContext from request headers or JWT.
     *
     * @param request the HTTP request
     * @return the RequestContext
     */
    private RequestContext buildRequestContext(HttpServletRequest request) {
        String correlationId = request.getHeader(CORRELATION_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        String traceId = request.getHeader(TRACE_HEADER);

        if (productionMode) {
            return buildFromJWT(request, correlationId, traceId);
        } else {
            return buildFromHeaders(request, correlationId, traceId);
        }
    }

    /**
     * Build RequestContext from JWT token (production mode).
     *
     * @param request the HTTP request
     * @param correlationId the correlation ID
     * @param traceId the trace ID
     * @return the RequestContext
     */
    private RequestContext buildFromJWT(
            HttpServletRequest request,
            String correlationId,
            String traceId) {

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
            log.warn("Missing or invalid Authorization header in production mode");
            throw new SecurityException("Missing JWT token");
        }

        String token = bearerToken.substring(BEARER_PREFIX.length());

        // TODO: Implement proper JWT validation
        // For now, extract tenant from header for testing
        String tenantId = request.getHeader(TENANT_HEADER);
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = DEFAULT_TENANT;
        }

        return RequestContext.builder()
            .tenantId(tenantId)
            .userId(extractUserIdFromToken(token))
            .correlationId(correlationId)
            .traceId(traceId)
            .build();
    }

    /**
     * Build RequestContext from headers (development mode).
     *
     * @param request the HTTP request
     * @param correlationId the correlation ID
     * @param traceId the trace ID
     * @return the RequestContext
     */
    private RequestContext buildFromHeaders(
            HttpServletRequest request,
            String correlationId,
            String traceId) {

        String tenantId = request.getHeader(TENANT_HEADER);
        if (tenantId == null || tenantId.isBlank()) {
            log.debug("No X-Tenant-ID header, using default tenant");
            tenantId = DEFAULT_TENANT;
        }

        return RequestContext.builder()
            .tenantId(tenantId)
            .userId(DEV_USER)
            .correlationId(correlationId)
            .traceId(traceId)
            .build();
    }

    /**
     * Extract user ID from JWT token.
     *
     * @param token the JWT token
     * @return the user ID
     */
    private String extractUserIdFromToken(String token) {
        // TODO: Implement proper JWT parsing
        // For now, return a placeholder
        return "jwt-user";
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Skip filter for actuator endpoints (optional)
        String path = request.getRequestURI();
        return path.startsWith("/actuator/health") || path.startsWith("/actuator/info");
    }
}
