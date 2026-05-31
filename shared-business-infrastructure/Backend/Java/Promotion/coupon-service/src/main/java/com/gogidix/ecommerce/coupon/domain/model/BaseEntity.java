package com.gogidix.ecommerce.coupon.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.time.Instant;
import java.util.Objects;

public abstract class BaseEntity {

    @Id
    protected String id;

    @Transient
    protected String tenantId;

    protected Instant createdAt;
    protected Instant updatedAt;

    protected BaseEntity() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    protected BaseEntity(String tenantId) {
        this();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void updateTimestamp() { this.updatedAt = Instant.now(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
