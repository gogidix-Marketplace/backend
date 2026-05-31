package com.gogidix.sales.onboarding.domain.port.in;

import com.gogidix.sales.onboarding.domain.model.Onboarding;
import com.gogidix.sales.onboarding.domain.valueobject.CustomerType;
import com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Onboarding Query (Input Port)
 * Defines the query operations for onboarding
 */
public interface OnboardingQuery {

    Optional<Onboarding> findById(String id);

    Optional<Onboarding> findByOnboardingIdAndTenantId(String onboardingId, String tenantId);

    List<Onboarding> findByTenantId(String tenantId);

    List<Onboarding> findByTenantIdAndStatus(String tenantId, OnboardingStatus status);

    List<Onboarding> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<Onboarding> findByTenantIdAndAssignedTo(String tenantId, String assignedTo);

    List<Onboarding> findByTenantIdAndCustomerType(String tenantId, CustomerType customerType);

    List<Onboarding> findByTenantIdAndDateRange(String tenantId, LocalDate startDate, LocalDate endDate);

    Page<Onboarding> findByTenantIdPaged(String tenantId, Pageable pageable);

    List<Onboarding> findPendingReview(String tenantId);

    List<Onboarding> findInProgress(String tenantId);

    List<Onboarding> findOverdue(String tenantId);

    OnboardingSummary getSummary(String tenantId);

    record OnboardingSummary(
            Long totalOnboardings,
            Long notStarted,
            Long inProgress,
            Long pendingReview,
            Long completed,
            Long onHold,
            Long cancelled,
            Double averageCompletionTimeMinutes
    ) {}
}
