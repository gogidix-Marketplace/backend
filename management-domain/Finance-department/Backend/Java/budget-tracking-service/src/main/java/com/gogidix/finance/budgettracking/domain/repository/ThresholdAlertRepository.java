package com.gogidix.finance.budgettracking.domain.repository;

import com.gogidix.finance.budgettracking.domain.model.ThresholdAlert;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Threshold Alert Repository Interface (Port)
 * Defines the contract for threshold alert persistence operations
 */
public interface ThresholdAlertRepository {

    ThresholdAlert save(ThresholdAlert alert);

    List<ThresholdAlert> saveAll(List<ThresholdAlert> alerts);

    Optional<ThresholdAlert> findById(String id);

    Optional<ThresholdAlert> findByAlertIdAndTenantId(String alertId, String tenantId);

    List<ThresholdAlert> findByTenantId(String tenantId);

    List<ThresholdAlert> findByTenantIdAndBudgetId(String tenantId, String budgetId);

    List<ThresholdAlert> findByTenantIdAndBudgetCode(String tenantId, String budgetCode);

    List<ThresholdAlert> findByTenantIdAndEnabled(String tenantId, boolean enabled);

    List<ThresholdAlert> findByTenantIdAndStatus(String tenantId, ThresholdAlert.AlertStatus status);

    List<ThresholdAlert> findByTenantIdAndThresholdLevel(String tenantId, ThresholdAlert.ThresholdLevel level);

    List<ThresholdAlert> findByTenantIdAndAlertType(String tenantId, ThresholdAlert.AlertType alertType);

    List<ThresholdAlert> findActiveAlertsForBudget(String tenantId, String budgetId, LocalDate currentDate);

    List<ThresholdAlert> findByTenantIdAndDepartment(String tenantId, String department);

    List<ThresholdAlert> findByTenantIdAndCategory(String tenantId, String category);

    List<ThresholdAlert> findEffectiveAlerts(String tenantId, LocalDate currentDate);

    List<ThresholdAlert> findAlertsRequiringAcknowledgement(String tenantId);

    boolean existsByAlertIdAndTenantId(String alertId, String tenantId);

    void deleteById(String id);

    void deleteByAlertIdAndTenantId(String alertId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndEnabled(String tenantId, boolean enabled);

    long countByTenantIdAndStatus(String tenantId, ThresholdAlert.AlertStatus status);

    List<ThresholdAlert> findByTenantIdAndRecipientsContaining(String tenantId, String recipient);
}
