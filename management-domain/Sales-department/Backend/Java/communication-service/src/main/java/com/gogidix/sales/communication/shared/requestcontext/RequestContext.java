package com.gogidix.sales.communication.shared.requestcontext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Request Context
 * Holds tenant and user information for the current request
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
    private String channel;

    private Map<String, Object> metadata;
}
