package com.gogidix.courier.etaservice.infrastructure.config;

import com.gogidix.courier.etaservice.shared.context.RequestContext;
import com.gogidix.courier.etaservice.shared.context.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration for ETA service.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);

    private static final String TENANT_HEADER = "X-Tenant-Id";
    private static final String USER_HEADER = "X-User-Id";
    private static final String CORRELATION_HEADER = "X-Correlation-Id";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestContextInterceptor());
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setBeforeMessagePrefix(">> Incoming request: ");
        filter.setAfterMessagePrefix("<< Request processed: ");
        return filter;
    }

    /**
     * Interceptor to set up request context from headers.
     */
    public static class RequestContextInterceptor implements org.springframework.web.servlet.HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            String tenantId = request.getHeader(TENANT_HEADER);
            String userId = request.getHeader(USER_HEADER);
            String correlationId = request.getHeader(CORRELATION_HEADER);

            RequestContext context = RequestContext.builder()
                    .tenantId(tenantId)
                    .userId(userId)
                    .correlationId(correlationId)
                    .build();

            RequestContextHolder.setContext(context);

            log.debug("Request context set: tenantId={}, userId={}, correlationId={}",
                    tenantId, userId, correlationId);

            return true;
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
            RequestContextHolder.clearContext();
        }
    }
}
