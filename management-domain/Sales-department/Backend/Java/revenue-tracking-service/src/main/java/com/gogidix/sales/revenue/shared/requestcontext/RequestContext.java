package com.gogidix.sales.revenue.shared.requestcontext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Request Context
 * Holds request-scoped information for multi-tenancy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestContext {

    private String tenantId;
    private String userId;
    private String correlationId;
    private String requestId;
    private String userAgent;
    private String ipAddress;

    private Map<String, Object> metadata;
}
