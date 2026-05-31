package com.gogidix.finance.conversion.shared.base;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

/**
 * Base Entity for all MongoDB documents
 * Provides common fields for auditing
 */
@Data
public abstract class BaseEntity {

    @Id
    protected String id;

    @CreatedDate
    protected Instant createdAt;

    @LastModifiedDate
    protected Instant updatedAt;

    @org.springframework.data.annotation.LastModifiedBy
    @CreatedBy
    protected String updatedBy;
}
