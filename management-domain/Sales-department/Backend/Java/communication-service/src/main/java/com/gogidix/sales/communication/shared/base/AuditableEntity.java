package com.gogidix.sales.communication.shared.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Auditable Entity for multi-tenant MongoDB documents
 * Extends BaseEntity with tenant support
 */
@Data
@NoArgsConstructor
public abstract class AuditableEntity extends BaseEntity {

    @Indexed
    protected String tenantId;

    @CreatedBy
    protected String createdBy;

    @CreatedDate
    protected Instant createdAt;
}
