package com.gogidix.aiservices.aidatavalidation.infrastructure.persistence;

import com.gogidix.aiservices.aidatavalidation.domain.aggregate.ValidationExecution;
import com.gogidix.aiservices.aidatavalidation.domain.model.Severity;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationType;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationEntity {
    private String executionId;
    private String dataSource;
    private String schema;
    private ValidationExecution.Status status;
    private Instant createdAt;
    private Instant startedAt;
    private Instant completedAt;
    private List<ValidationRuleEntity> rules;
    private int timeout;
    private int progress;
    private String errorMessage;
}
