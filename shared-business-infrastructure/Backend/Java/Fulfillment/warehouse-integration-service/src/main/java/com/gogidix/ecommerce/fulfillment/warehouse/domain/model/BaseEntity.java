package com.gogidix.ecommerce.fulfillment.warehouse.domain.model;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "warehouse_fulfillment")
public abstract class BaseEntity {
    @Id protected String id;
    @Indexed protected String tenantId;
    protected Instant createdAt; protected Instant updatedAt;
    protected String createdBy; protected String updatedBy;
    public BaseEntity() { this.createdAt = Instant.now(); this.updatedAt = Instant.now(); }
    public String getId() { return id; } public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; } public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Instant getCreatedAt() { return createdAt; } public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; } public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public String getCreatedBy() { return createdBy; } public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getUpdatedBy() { return updatedBy; } public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public void markAsUpdated() { this.updatedAt = Instant.now(); }
    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof BaseEntity)) return false; BaseEntity that = (BaseEntity) o; return id != null && id.equals(that.id); }
    @Override public int hashCode() { return getClass().hashCode(); }
}