package com.gogidix.sales.countrydashboard.shared.requestcontext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Request Context
 * Holds tenant, user, country and correlation information for the current request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestContext {

    private String tenantId;
    private String userId;
    private String countryCode;
    private String correlationId;
    private String region;
    private Instant requestTime;
    private Map<String, Object> metadata;

    public static RequestContext of(String tenantId, String userId) {
        return RequestContext.builder()
                .tenantId(tenantId)
                .userId(userId)
                .requestTime(Instant.now())
                .build();
    }

    public static RequestContext of(String tenantId, String userId, String countryCode) {
        return RequestContext.builder()
                .tenantId(tenantId)
                .userId(userId)
                .countryCode(countryCode)
                .requestTime(Instant.now())
                .build();
    }

    public static RequestContext of(String tenantId, String userId, String countryCode, String correlationId) {
        return RequestContext.builder()
                .tenantId(tenantId)
                .userId(userId)
                .countryCode(countryCode)
                .correlationId(correlationId)
                .requestTime(Instant.now())
                .build();
    }
}
