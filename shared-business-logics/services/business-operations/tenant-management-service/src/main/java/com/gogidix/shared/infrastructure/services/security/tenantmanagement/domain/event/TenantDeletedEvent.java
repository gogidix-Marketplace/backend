package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.shared.requestcontext.TenantContextHolder;
import lombok.Getter;

/**
 * Event published when a tenant is deleted.
 * Other services should clean up tenant-specific data when receiving this event.
 */
@Getter
public class TenantDeletedEvent extends DomainEvent {

    private final String tenantIdValue;
    private final String name;
    private final String deletedBy;

    public TenantDeletedEvent(String tenantId, String tenantName) {
        super("TenantDeleted", "Tenant", tenantId);
        this.tenantIdValue = tenantId;
        this.name = tenantName;
        this.deletedBy = TenantContextHolder.getUserId();
    }
}
