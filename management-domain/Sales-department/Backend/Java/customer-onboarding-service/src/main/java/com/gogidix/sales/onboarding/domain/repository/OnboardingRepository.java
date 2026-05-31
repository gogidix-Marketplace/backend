package com.gogidix.sales.onboarding.domain.repository;

import com.gogidix.sales.onboarding.domain.model.Onboarding;
import com.gogidix.sales.onboarding.domain.valueobject.CustomerType;
import com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Onboarding Repository Interface (Port)
 * Defines the contract for onboarding persistence operations
 */
public interface OnboardingRepository {

    Onboarding save(Onboarding onboarding);

    List<Onboarding> saveAll(List<Onboarding> onboardings);

    Optional<Onboarding> findById(String id);

    Optional<Onboarding> findByOnboardingIdAndTenantId(String onboardingId, String tenantId);

    List<Onboarding> findByTenantId(String tenantId);

    List<Onboarding> findByTenantIdAndStatus(String tenantId, OnboardingStatus status);

    List<Onboarding> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<Onboarding> findByTenantIdAndAssignedTo(String tenantId, String assignedTo);

    List<Onboarding> findByTenantIdAndCustomerType(String tenantId, CustomerType customerType);

    List<Onboarding> findByTenantIdAndDateRange(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Onboarding> findPendingReview(String tenantId);

    List<Onboarding> findInProgress(String tenantId);

    List<Onboarding> findOverdue(String tenantId);

    List<Onboarding> findByTenantIdAndPriority(String tenantId, String priority);

    List<Onboarding> findByTenantIdAndInitiatedBy(String tenantId, String initiatedBy);

    boolean existsByOnboardingIdAndTenantId(String onboardingId, String tenantId);

    void deleteById(String id);

    void deleteByOnboardingIdAndTenantId(String onboardingId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, OnboardingStatus status);

    List<Onboarding> findByTemplateId(String tenantId, String templateId);
}
