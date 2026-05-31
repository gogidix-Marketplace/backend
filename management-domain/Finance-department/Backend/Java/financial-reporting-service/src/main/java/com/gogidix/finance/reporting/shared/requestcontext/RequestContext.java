package com.gogidix.finance.reporting.shared.requestcontext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Request Context for multi-tenant SaaS
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
