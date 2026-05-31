package com.gogidix.aiservices.aidatavalidation.domain.port.out;

import com.gogidix.aiservices.aidatavalidation.domain.aggregate.ValidationExecution;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationRule;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ValidationRepository {

    ValidationExecution save(ValidationExecution execution);

    Optional<ValidationExecution> findById(String executionId);

    List<ValidationExecution> findPending();

    void delete(String executionId);

    ValidationRule saveRule(ValidationRule rule);

    Optional<ValidationRule> findRuleById(String ruleId);

    List<ValidationRule> findRulesByType(ValidationType type);

    void deleteRule(String ruleId);

    Map<String, Object> getStatistics();
}
