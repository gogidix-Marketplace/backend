package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.shared.requestcontext.TenantContextHolder;
import lombok.Getter;

import java.util.Map;

/**
 * Event published when a tenant is updated.
 */
@Getter
public class TenantUpdatedEvent extends DomainEvent {

    private final String tenantIdValue;
    private final String name;
    private final String domain;
    private final Tenant.TenantStatus status;
    private final Tenant.TenantPlan plan;
    private final Map<String, Object> settings;
    private final Map<String, Object> features;
    private final String updatedBy;

    public TenantUpdatedEvent(Tenant tenant) {
        super("TenantUpdated", "Tenant", tenant.getId());
        this.tenantIdValue = tenant.getTenantId();
        this.name = tenant.getName();
        this.domain = tenant.getDomain();
        this.status = tenant.getStatus();
        this.plan = tenant.getPlan();
        this.settings = tenant.getSettings();
        this.features = tenant.getFeatures();
        this.updatedBy = TenantContextHolder.getUserId();
    }
}
