package com.gogidix.sales.dealmanagement.shared.requestcontext;

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

    private String username;

    private String correlationId;

    private String userRole;

    private String organizationId;

    @Builder.Default
    private Map<String, String> metadata = Map.of();
}
