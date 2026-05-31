package com.gogidix.aiservices.aidatavalidation.infrastructure.persistence;

import com.gogidix.aiservices.aidatavalidation.domain.aggregate.ValidationExecution;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationType;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InMemoryValidationDataSource implements ValidationDataSource {

    private final Map<String, ValidationEntity> executions = new ConcurrentHashMap<>();
    private final Map<String, ValidationRuleEntity> rules = new ConcurrentHashMap<>();

    @Override
    public ValidationEntity save(ValidationEntity entity) {
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(Instant.now());
        }
        executions.put(entity.getExecutionId(), entity);
        return entity;
    }

    @Override
    public Optional<ValidationEntity> findById(String id) {
        return Optional.ofNullable(executions.get(id));
    }

    @Override
    public List<ValidationEntity> findPending() {
        return executions.values().stream()
                .filter(e -> e.getStatus() == ValidationExecution.Status.PENDING)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        executions.remove(id);
    }

    @Override
    public ValidationRuleEntity saveRule(ValidationRuleEntity entity) {
        rules.put(entity.getRuleId(), entity);
        return entity;
    }

    @Override
    public Optional<ValidationRuleEntity> findRuleById(String id) {
        return Optional.ofNullable(rules.get(id));
    }

    @Override
    public List<ValidationRuleEntity> findRulesByType(ValidationType type) {
        return rules.values().stream()
                .filter(r -> r.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRule(String id) {
        rules.remove(id);
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalValidations", executions.size());
        stats.put("completed", executions.values().stream()
                .filter(e -> e.getStatus() == ValidationExecution.Status.COMPLETED).count());
        stats.put("failed", executions.values().stream()
                .filter(e -> e.getStatus() == ValidationExecution.Status.FAILED).count());
        stats.put("pending", executions.values().stream()
                .filter(e -> e.getStatus() == ValidationExecution.Status.PENDING).count());
        stats.put("totalRules", rules.size());
        return stats;
    }
}
