package com.gogidix.aiservices.aimonitoringservice.domain.repository;

import com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRule;
import com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRuleStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AlertRule entities.
 */
public interface AlertRuleRepository {

    AlertRule save(AlertRule rule);

    Optional<AlertRule> findById(String id);

    Optional<AlertRule> findByAlertIdAndTenantId(String alertId, String tenantId);

    List<AlertRule> findByTenantId(String tenantId);

    List<AlertRule> findByTenantIdAndStatus(String tenantId, AlertRuleStatus status);

    void deleteById(String id);

    void deleteByAlertIdAndTenantId(String alertId, String tenantId);
}
