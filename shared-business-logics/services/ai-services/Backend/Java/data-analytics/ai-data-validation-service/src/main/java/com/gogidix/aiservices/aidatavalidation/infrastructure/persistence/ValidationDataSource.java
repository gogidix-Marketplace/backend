package com.gogidix.aiservices.aidatavalidation.infrastructure.persistence;

import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ValidationDataSource {
    ValidationEntity save(ValidationEntity entity);

    Optional<ValidationEntity> findById(String id);

    List<ValidationEntity> findPending();

    void delete(String id);

    ValidationRuleEntity saveRule(ValidationRuleEntity entity);

    Optional<ValidationRuleEntity> findRuleById(String id);

    List<ValidationRuleEntity> findRulesByType(ValidationType type);

    void deleteRule(String id);

    Map<String, Object> getStatistics();
}
