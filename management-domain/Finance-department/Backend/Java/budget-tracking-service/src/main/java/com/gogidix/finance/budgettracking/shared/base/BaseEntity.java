package com.gogidix.finance.budgettracking.shared.base;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
@NoArgsConstructor
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
