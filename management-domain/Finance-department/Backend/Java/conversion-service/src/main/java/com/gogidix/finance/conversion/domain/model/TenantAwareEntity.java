package com.gogidix.finance.conversion.domain.model;

import com.gogidix.finance.conversion.shared.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

/**
 * Base Entity for domain models
 * Extends shared base entity with tenant-specific fields
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class TenantAwareEntity extends BaseEntity {

    @Indexed
    @Field("tenant_id")
    protected String tenantId;

    protected TenantAwareEntity() {
        super();
    }

    protected TenantAwareEntity(String tenantId) {
        super();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
    }
}
