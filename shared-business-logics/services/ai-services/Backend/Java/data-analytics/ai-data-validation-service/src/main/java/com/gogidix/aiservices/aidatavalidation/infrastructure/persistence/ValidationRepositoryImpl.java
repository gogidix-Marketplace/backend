package com.gogidix.aiservices.aidatavalidation.infrastructure.persistence;

import com.gogidix.aiservices.aidatavalidation.domain.aggregate.ValidationExecution;
import com.gogidix.aiservices.aidatavalidation.domain.model.Severity;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationResult;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationRule;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationType;
import com.gogidix.aiservices.aidatavalidation.domain.port.out.ValidationRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ValidationRepositoryImpl implements ValidationRepository {

    private final ValidationDataSource dataSource;

    public ValidationRepositoryImpl(ValidationDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ValidationExecution save(ValidationExecution execution) {
        ValidationEntity entity = toEntity(execution);
        dataSource.save(entity);
        return execution;
    }

    @Override
    public Optional<ValidationExecution> findById(String executionId) {
        return dataSource.findById(executionId).map(this::toDomain);
    }

    @Override
    public List<ValidationExecution> findPending() {
        return dataSource.findPending().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void delete(String executionId) {
        dataSource.delete(executionId);
    }

    @Override
    public ValidationRule saveRule(ValidationRule rule) {
        ValidationRuleEntity entity = toRuleEntity(rule);
        dataSource.saveRule(entity);
        return rule;
    }

    @Override
    public Optional<ValidationRule> findRuleById(String ruleId) {
        return dataSource.findRuleById(ruleId).map(this::toRuleDomain);
    }

    @Override
    public List<ValidationRule> findRulesByType(ValidationType type) {
        return dataSource.findRulesByType(type).stream()
                .map(this::toRuleDomain)
                .toList();
    }

    @Override
    public void deleteRule(String ruleId) {
        dataSource.deleteRule(ruleId);
    }

    @Override
    public Map<String, Object> getStatistics() {
        return dataSource.getStatistics();
    }

    private ValidationEntity toEntity(ValidationExecution execution) {
        ValidationEntity entity = new ValidationEntity();
        entity.setExecutionId(execution.getExecutionId());
        entity.setDataSource(execution.getDataSource());
        entity.setSchema(execution.getSchema());
        entity.setStatus(execution.getStatus());
        entity.setCreatedAt(execution.getCreatedAt());
        entity.setStartedAt(execution.getStartedAt());
        entity.setCompletedAt(execution.getCompletedAt());
        entity.setTimeout(execution.getTimeout());
        entity.setProgress(execution.getProgress());
        entity.setErrorMessage(execution.getErrorMessage());
        return entity;
    }

    private ValidationExecution toDomain(ValidationEntity entity) {
        return ValidationExecution.restore(
                entity.getExecutionId(),
                entity.getDataSource(),
                entity.getSchema(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getStartedAt(),
                entity.getCompletedAt(),
                List.of(),
                entity.getTimeout(),
                entity.getProgress(),
                null,
                entity.getErrorMessage()
        );
    }

    private ValidationRuleEntity toRuleEntity(ValidationRule rule) {
        ValidationRuleEntity entity = new ValidationRuleEntity();
        entity.setRuleId(rule.getRuleId());
        entity.setName(rule.getName());
        entity.setType(rule.getType());
        entity.setConfiguration(rule.getConfiguration());
        entity.setSeverity(rule.getSeverity());
        entity.setPriority(rule.getPriority());
        entity.setEnabled(rule.isEnabled());
        return entity;
    }

    private ValidationRule toRuleDomain(ValidationRuleEntity entity) {
        return ValidationRule.restore(
                entity.getRuleId(),
                entity.getName(),
                entity.getType(),
                entity.getConfiguration(),
                entity.getSeverity(),
                entity.getPriority(),
                entity.isEnabled()
        );
    }
}
