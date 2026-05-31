package com.gogidix.finance.tax.shared.base;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Base Entity for all MongoDB documents
 * Provides common fields for auditing
 */
@Data
public abstract class BaseEntity {

    @Id
    protected String id;

    @org.springframework.data.mongodb.core.mapping.Field("tenant_id")
    protected String tenantId;

    @CreatedDate
    @org.springframework.data.mongodb.core.mapping.Field("created_at")
    protected Instant createdAt;

    @LastModifiedDate
    @org.springframework.data.mongodb.core.mapping.Field("updated_at")
    protected Instant updatedAt;

    @org.springframework.data.mongodb.core.mapping.Field("updated_by")
    @CreatedBy
    protected String updatedBy;

    public void updateTimestamp() {
        this.updatedAt = Instant.now();
    }
}
