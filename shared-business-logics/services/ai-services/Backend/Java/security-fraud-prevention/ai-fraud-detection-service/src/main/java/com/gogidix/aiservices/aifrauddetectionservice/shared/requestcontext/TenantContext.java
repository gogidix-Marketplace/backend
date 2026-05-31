package com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Context for tenant information in multi-tenant environments.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class TenantContext {

    private String tenantId;
    private String correlationId;
    private String userId;
    @Builder.Default
    private Set<String> roles = Set.of();

    public boolean isValid() {
        return tenantId != null && !tenantId.isEmpty();
    }
}
