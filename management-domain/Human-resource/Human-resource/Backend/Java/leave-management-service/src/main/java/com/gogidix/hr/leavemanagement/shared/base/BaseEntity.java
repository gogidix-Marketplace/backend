package com.gogidix.hr.leavemanagement.shared.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    @Id
    protected String id;

    @CreatedDate
    protected Instant createdAt;

    @LastModifiedDate
    protected Instant updatedAt;

    @CreatedBy
    protected String createdBy;

    @org.springframework.data.annotation.LastModifiedBy
    protected String updatedBy;
}
