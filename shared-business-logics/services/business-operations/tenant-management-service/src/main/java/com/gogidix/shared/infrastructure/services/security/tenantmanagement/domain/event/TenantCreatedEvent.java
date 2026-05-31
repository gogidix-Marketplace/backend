package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.shared.requestcontext.TenantContextHolder;
import lombok.Getter;

/**
 * Event published when a new tenant is created.
 * Other services can subscribe to this event to initialize tenant-specific data.
 */
@Getter
public class TenantCreatedEvent extends DomainEvent {

    private final String tenantIdValue;
    private final String name;
    private final String domain;
    private final Tenant.TenantStatus status;
    private final Tenant.TenantPlan plan;
    private final Long maxUsers;
    private final Long maxStorageGB;
    private final String createdBy;

    public TenantCreatedEvent(Tenant tenant) {
        super("TenantCreated", "Tenant", tenant.getId());
        this.tenantIdValue = tenant.getTenantId();
        this.name = tenant.getName();
        this.domain = tenant.getDomain();
        this.status = tenant.getStatus();
        this.plan = tenant.getPlan();
        this.maxUsers = tenant.getMaxUsers();
        this.maxStorageGB = tenant.getMaxStorageGB();
        this.createdBy = TenantContextHolder.getUserId();
    }
}
