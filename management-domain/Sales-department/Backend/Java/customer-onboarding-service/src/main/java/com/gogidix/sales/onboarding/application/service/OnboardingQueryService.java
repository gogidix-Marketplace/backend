package com.gogidix.sales.onboarding.application.service;

import com.gogidix.sales.onboarding.domain.model.Onboarding;
import com.gogidix.sales.onboarding.domain.model.OnboardingTemplate;
import com.gogidix.sales.onboarding.domain.port.in.OnboardingQuery;
import com.gogidix.sales.onboarding.domain.repository.OnboardingRepository;
import com.gogidix.sales.onboarding.domain.repository.OnboardingTemplateRepository;
import com.gogidix.sales.onboarding.shared.exception.NotFoundException;
import com.gogidix.sales.onboarding.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Onboarding Query Service
 * Handles all read operations for onboarding
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OnboardingQueryService implements OnboardingQuery {

    private final OnboardingRepository onboardingRepository;
    private final OnboardingTemplateRepository templateRepository;

    @Override
    public java.util.Optional<Onboarding> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        return onboardingRepository.findByOnboardingIdAndTenantId(id, tenantId);
    }

    @Override
    public java.util.Optional<Onboarding> findByOnboardingIdAndTenantId(String onboardingId, String tenantId) {
        return onboardingRepository.findByOnboardingIdAndTenantId(onboardingId, tenantId);
    }

    @Override
    public List<Onboarding> findByTenantId(String tenantId) {
        return onboardingRepository.findByTenantId(tenantId);
    }

    @Override
    public List<Onboarding> findByTenantIdAndStatus(String tenantId,
                                                      com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus status) {
        return onboardingRepository.findByTenantIdAndStatus(tenantId, status);
    }

    @Override
    public List<Onboarding> findByTenantIdAndCustomerId(String tenantId, String customerId) {
        return onboardingRepository.findByTenantIdAndCustomerId(tenantId, customerId);
    }

    @Override
    public List<Onboarding> findByTenantIdAndAssignedTo(String tenantId, String assignedTo) {
        return onboardingRepository.findByTenantIdAndAssignedTo(tenantId, assignedTo);
    }

    @Override
    public List<Onboarding> findByTenantIdAndCustomerType(String tenantId,
                                                            com.gogidix.sales.onboarding.domain.valueobject.CustomerType customerType) {
        return onboardingRepository.findByTenantIdAndCustomerType(tenantId, customerType);
    }

    @Override
    public List<Onboarding> findByTenantIdAndDateRange(String tenantId, LocalDate startDate, LocalDate endDate) {
        return onboardingRepository.findByTenantIdAndDateRange(tenantId, startDate, endDate);
    }

    @Override
    public Page<Onboarding> findByTenantIdPaged(String tenantId, Pageable pageable) {
        List<Onboarding> allOnboardings = onboardingRepository.findByTenantId(tenantId);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allOnboardings.size());
        List<Onboarding> pagedOnboardings = allOnboardings.subList(start, end);
        return new PageImpl<>(pagedOnboardings, pageable, allOnboardings.size());
    }

    @Override
    public List<Onboarding> findPendingReview(String tenantId) {
        return onboardingRepository.findPendingReview(tenantId);
    }

    @Override
    public List<Onboarding> findInProgress(String tenantId) {
        return onboardingRepository.findInProgress(tenantId);
    }

    @Override
    public List<Onboarding> findOverdue(String tenantId) {
        return onboardingRepository.findOverdue(tenantId);
    }

    @Override
    public OnboardingSummary getSummary(String tenantId) {
        List<Onboarding> allOnboardings = onboardingRepository.findByTenantId(tenantId);

        long total = allOnboardings.size();
        long notStarted = allOnboardings.stream()
                .filter(o -> o.getStatus() == com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus.NOT_STARTED)
                .count();
        long inProgress = allOnboardings.stream()
                .filter(o -> o.getStatus() == com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus.IN_PROGRESS)
                .count();
        long pendingReview = allOnboardings.stream()
                .filter(o -> o.getStatus() == com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus.PENDING_REVIEW)
                .count();
        long completed = allOnboardings.stream()
                .filter(o -> o.getStatus() == com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus.COMPLETED)
                .count();
        long onHold = allOnboardings.stream()
                .filter(o -> o.getStatus() == com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus.ON_HOLD)
                .count();
        long cancelled = allOnboardings.stream()
                .filter(o -> o.getStatus() == com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus.CANCELLED)
                .count();

        double avgCompletionTime = allOnboardings.stream()
                .filter(o -> o.getActualDurationMinutes() != null)
                .mapToLong(Onboarding::getActualDurationMinutes)
                .average()
                .orElse(0.0);

        return new OnboardingSummary(total, notStarted, inProgress, pendingReview,
                completed, onHold, cancelled, avgCompletionTime);
    }

    // Additional query methods

    public Onboarding getById(String onboardingId) {
        String tenantId = RequestContextHolder.getTenantId();
        return onboardingRepository.findByOnboardingIdAndTenantId(onboardingId, tenantId)
                .orElseThrow(() -> new NotFoundException("Onboarding", onboardingId));
    }

    public List<Onboarding> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return onboardingRepository.findByTenantId(tenantId);
    }

    public List<Onboarding> getByCustomerId(String customerId) {
        String tenantId = RequestContextHolder.getTenantId();
        return onboardingRepository.findByTenantIdAndCustomerId(tenantId, customerId);
    }

    public List<Onboarding> getByAssignedUser() {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId();
        return onboardingRepository.findByTenantIdAndAssignedTo(tenantId, userId);
    }

    public List<OnboardingTemplate> getAllTemplates() {
        String tenantId = RequestContextHolder.getTenantId();
        return templateRepository.findByTenantId(tenantId);
    }

    public List<OnboardingTemplate> getActiveTemplates() {
        String tenantId = RequestContextHolder.getTenantId();
        return templateRepository.findByTenantIdAndActive(tenantId, true);
    }

    public OnboardingTemplate getTemplateById(String templateId) {
        String tenantId = RequestContextHolder.getTenantId();
        return templateRepository.findByTemplateIdAndTenantId(templateId, tenantId)
                .orElseThrow(() -> new NotFoundException("Template", templateId));
    }

    public List<OnboardingTemplate> getTemplatesByCustomerType(
            com.gogidix.sales.onboarding.domain.valueobject.CustomerType customerType) {
        String tenantId = RequestContextHolder.getTenantId();
        return templateRepository.findByTenantIdAndCustomerType(tenantId, customerType);
    }
}
