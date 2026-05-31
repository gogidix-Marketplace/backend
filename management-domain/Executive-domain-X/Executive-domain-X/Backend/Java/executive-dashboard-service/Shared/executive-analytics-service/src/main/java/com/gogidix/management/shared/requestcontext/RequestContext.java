package com.gogidix.management.shared.requestcontext;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestContext {
    private final String tenantId;
    private final String userId;
    private final String correlationId;
    private final String region;
    private final String country;
    private final String traceId;
}
