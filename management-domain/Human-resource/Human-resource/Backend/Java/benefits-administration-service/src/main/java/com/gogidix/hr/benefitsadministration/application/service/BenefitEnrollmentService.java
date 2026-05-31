package com.gogidix.hr.benefitsadministration.application.service;

import com.gogidix.hr.benefitsadministration.application.dto.request.*;
import com.gogidix.hr.benefitsadministration.application.dto.response.*;
import com.gogidix.hr.benefitsadministration.domain.enums.EnrollmentStatus;
import com.gogidix.hr.benefitsadministration.domain.event.BenefitEnrollmentCancelledEvent;
import com.gogidix.hr.benefitsadministration.domain.event.BenefitEnrollmentCreatedEvent;
import com.gogidix.hr.benefitsadministration.domain.event.BenefitEnrollmentUpdatedEvent;
import com.gogidix.hr.benefitsadministration.domain.model.BenefitEnrollment;
import com.gogidix.hr.benefitsadministration.domain.port.in.*;
import com.gogidix.hr.benefitsadministration.domain.port.out.BenefitEnrollmentRepositoryPort;
import com.gogidix.hr.benefitsadministration.domain.port.out.BenefitEventPublisher;
import com.gogidix.hr.benefitsadministration.domain.port.out.BenefitPlanRepositoryPort;
import com.gogidix.hr.benefitsadministration.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing benefit enrollments
 * Implements hexagonal architecture use cases
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BenefitEnrollmentService implements
        EnrollEmployeeInBenefitUseCase,
        CancelBenefitEnrollmentUseCase {

    private final BenefitEnrollmentRepositoryPort enrollmentRepository;
    private final BenefitPlanRepositoryPort planRepository;
    private final BenefitEventPublisher eventPublisher;
    private final BenefitPlanService planService;

    @Override
    @Transactional
    public EnrollmentConfirmationResponse enrollEmployee(CreateBenefitEnrollmentRequest request) {
        log.info("Enrolling employee {} in plan {}", request.getEmployeeId(), request.getPlanId());

        String tenantId = getTenantId();

        // Validate eligibility
        ValidateEligibilityRequest eligibilityRequest = ValidateEligibilityRequest.builder()
                .employeeId(request.getEmployeeId())
                .planId(request.getPlanId())
                .tenantId(tenantId)
                .effectiveDate(request.getEffectiveDate())
                .build();

        EligibilityCheckResponse eligibility = planService.validateEligibility(eligibilityRequest);

        if (!eligibility.getIsEligible()) {
            return EnrollmentConfirmationResponse.builder()
                    .employeeId(request.getEmployeeId())
                    .planId(request.getPlanId())
                    .status(EnrollmentStatus.REJECTED)
                    .isSuccessful(false)
                    .message("Employee not eligible: " + eligibility.getIneligibilityReason())
                    .build();
        }

        // Check if already enrolled
        boolean alreadyEnrolled = enrollmentRepository.isEmployeeEnrolled(
                request.getEmployeeId(),
                request.getPlanId(),
                tenantId
        );

        if (alreadyEnrolled) {
            return EnrollmentConfirmationResponse.builder()
                    .employeeId(request.getEmployeeId())
                    .planId(request.getPlanId())
                    .status(EnrollmentStatus.EXISTING)
                    .isSuccessful(false)
                    .message("Employee already enrolled in this plan")
                    .build();
        }

        // Calculate premium
        CalculatePremiumRequest premiumRequest = CalculatePremiumRequest.builder()
                .employeeId(request.getEmployeeId())
                .planId(request.getPlanId())
                .coverageLevel(request.getCoverageLevel())
                .coverageOptionCode(request.getCoverageOptionCode())
                .numberOfDependents(request.getDependents() != null ? request.getDependents().size() : 0)
                .effectiveDate(request.getEffectiveDate())
                .build();

        PremiumCalculationResponse premium = planService.calculatePremium(premiumRequest);

        // Create enrollment
        BenefitEnrollment enrollment = BenefitEnrollment.builder()
                .tenantId(tenantId)
                .employeeId(request.getEmployeeId())
                .planId(request.getPlanId())
                .coverageLevel(request.getCoverageLevel())
                .coverageOptionCode(request.getCoverageOptionCode())
                .effectiveDate(request.getEffectiveDate())
                .expiryDate(request.getExpiryDate())
                .status(EnrollmentStatus.PENDING)
                .employeePremium(premium.getEmployeePremium())
                .employerPremium(premium.getEmployerPremium())
                .totalPremium(premium.getTotalPremium())
                .currency(premium.getCurrency())
                .notes(request.getNotes())
                .waiveCoverage(request.getWaiveCoverage())
                .waiverReason(request.getWaiverReason())
                .evidenceDocuments(request.getEvidenceDocuments())
                .build();
        enrollment.setId(UUID.randomUUID().toString());
        enrollment.setCreatedAt(Instant.now());
        enrollment.setUpdatedAt(Instant.now());
        enrollment.setUpdatedBy(getUserId());

        BenefitEnrollment saved = enrollmentRepository.save(enrollment);

        // Publish event
        BenefitEnrollmentCreatedEvent event = BenefitEnrollmentCreatedEvent.builder()
                .enrollmentId(saved.getId())
                .employeeId(saved.getEmployeeId())
                .planId(saved.getPlanId())
                .coverageLevel(saved.getCoverageLevel())
                .effectiveDate(saved.getEffectiveDate() != null ? saved.getEffectiveDate().atStartOfDay() : null)
                .premium(saved.getTotalPremium())
                .tenantId(tenantId)
                .timestamp(LocalDateTime.now())
                .build();

        eventPublisher.publishEnrollmentCreated(event);

        String confirmationNumber = "BNF-" + saved.getId().substring(0, 8).toUpperCase();

        log.info("Employee enrolled successfully: {}", saved.getId());

        return EnrollmentConfirmationResponse.builder()
                .enrollmentId(saved.getId())
                .employeeId(request.getEmployeeId())
                .planId(request.getPlanId())
                .coverageLevel(request.getCoverageLevel())
                .status(EnrollmentStatus.PENDING)
                .effectiveDate(request.getEffectiveDate())
                .confirmationNumber(confirmationNumber)
                .confirmedAt(LocalDateTime.now())
                .requiresApproval(false)
                .requiresEvidence(eligibility.getEligibilityDetails() != null)
                .message("Enrollment submitted successfully")
                .isSuccessful(true)
                .build();
    }

    @Override
    @Transactional
    public List<EnrollmentConfirmationResponse> bulkEnrollEmployees(List<CreateBenefitEnrollmentRequest> requests) {
        return requests.stream()
                .map(this::enrollEmployee)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EnrollmentConfirmationResponse reEnrollEmployee(String enrollmentId) {
        log.info("Re-enrolling employee for enrollment: {}", enrollmentId);

        String tenantId = getTenantId();
        BenefitEnrollment existing = enrollmentRepository.findById(enrollmentId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));

        // Create new enrollment based on existing
        CreateBenefitEnrollmentRequest request = CreateBenefitEnrollmentRequest.builder()
                .employeeId(existing.getEmployeeId())
                .planId(existing.getPlanId())
                .tenantId(tenantId)
                .coverageLevel(existing.getCoverageLevel())
                .coverageOptionCode(existing.getCoverageOptionCode())
                .effectiveDate(LocalDate.now())
                .build();

        return enrollEmployee(request);
    }

    @Override
    @Transactional
    public BenefitEnrollmentResponse cancelEnrollment(String enrollmentId, String cancellationReason, LocalDate effectiveDate) {
        log.info("Canceling enrollment: {}", enrollmentId);

        String tenantId = getTenantId();
        BenefitEnrollment enrollment = enrollmentRepository.findById(enrollmentId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollment.setUpdatedAt(Instant.now());
        enrollment.setUpdatedBy(getUserId());

        BenefitEnrollment saved = enrollmentRepository.save(enrollment);

        // Publish cancellation event
        BenefitEnrollmentCancelledEvent event = BenefitEnrollmentCancelledEvent.builder()
                .enrollmentId(saved.getId())
                .employeeId(saved.getEmployeeId())
                .planId(saved.getPlanId())
                .cancellationReason(cancellationReason)
                .effectiveDate(effectiveDate)
                .tenantId(tenantId)
                .timestamp(LocalDateTime.now())
                .build();

        eventPublisher.publishEnrollmentCancelled(event);

        log.info("Enrollment cancelled successfully: {}", enrollmentId);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public List<BenefitEnrollmentResponse> terminateEmployeeEnrollments(String employeeId, LocalDate terminationDate) {
        log.info("Terminating all enrollments for employee: {}", employeeId);

        String tenantId = getTenantId();
        List<BenefitEnrollment> enrollments = enrollmentRepository.findActiveByEmployee(employeeId, tenantId);

        List<BenefitEnrollmentResponse> responses = enrollments.stream()
                .map(enrollment -> {
                    enrollment.setStatus(EnrollmentStatus.TERMINATED);
                    enrollment.setUpdatedAt(Instant.now());
                    enrollment.setUpdatedBy(getUserId());
                    BenefitEnrollment saved = enrollmentRepository.save(enrollment);
                    return toResponse(saved);
                })
                .collect(Collectors.toList());

        log.info("Terminated {} enrollments for employee: {}", responses.size(), employeeId);
        return responses;
    }

    public Optional<BenefitEnrollmentResponse> getEnrollmentById(String enrollmentId) {
        String tenantId = getTenantId();
        return enrollmentRepository.findById(enrollmentId, tenantId)
                .map(this::toResponse);
    }

    public List<BenefitEnrollmentResponse> getEnrollmentsByEmployee(String employeeId) {
        String tenantId = getTenantId();
        return enrollmentRepository.findByEmployeeId(employeeId, tenantId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BenefitEnrollmentResponse> getActiveEnrollmentsByEmployee(String employeeId) {
        String tenantId = getTenantId();
        return enrollmentRepository.findActiveByEmployee(employeeId, tenantId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BenefitEnrollmentResponse> getEnrollmentsByPlan(String planId) {
        String tenantId = getTenantId();
        return enrollmentRepository.findByPlanId(planId, tenantId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BenefitEnrollmentResponse updateEnrollment(String enrollmentId, UpdateBenefitEnrollmentRequest request) {
        log.info("Updating enrollment: {}", enrollmentId);

        String tenantId = getTenantId();
        BenefitEnrollment enrollment = enrollmentRepository.findById(enrollmentId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));

        if (request.getCoverageLevel() != null) {
            enrollment.setCoverageLevel(request.getCoverageLevel());
        }
        if (request.getEffectiveDate() != null) {
            enrollment.setEffectiveDate(request.getEffectiveDate());
        }
        if (request.getExpiryDate() != null) {
            enrollment.setExpiryDate(request.getExpiryDate());
        }
        if (request.getStatus() != null) {
            enrollment.setStatus(request.getStatus());
        }
        if (request.getCoverageOptionCode() != null) {
            enrollment.setCoverageOptionCode(request.getCoverageOptionCode());
        }
        if (request.getNotes() != null) {
            enrollment.setNotes(request.getNotes());
        }
        if (request.getEvidenceDocuments() != null) {
            enrollment.setEvidenceDocuments(request.getEvidenceDocuments());
        }

        enrollment.setUpdatedAt(Instant.now());
        enrollment.setUpdatedBy(getUserId());

        BenefitEnrollment saved = enrollmentRepository.save(enrollment);

        // Publish update event
        BenefitEnrollmentUpdatedEvent event = BenefitEnrollmentUpdatedEvent.builder()
                .enrollmentId(saved.getId())
                .employeeId(saved.getEmployeeId())
                .planId(saved.getPlanId())
                .coverageLevel(saved.getCoverageLevel())
                .status(saved.getStatus().name())
                .tenantId(tenantId)
                .timestamp(LocalDateTime.now())
                .build();

        eventPublisher.publishEnrollmentUpdated(event);

        log.info("Enrollment updated successfully: {}", enrollmentId);
        return toResponse(saved);
    }

    @Transactional
    public BenefitEnrollmentResponse approveEnrollment(String enrollmentId, ApproveEnrollmentRequest request) {
        log.info("Approving enrollment: {}", enrollmentId);

        String tenantId = getTenantId();
        BenefitEnrollment enrollment = enrollmentRepository.findById(enrollmentId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));

        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setUpdatedAt(Instant.now());
        enrollment.setUpdatedBy(request.getApprovedBy() != null ? request.getApprovedBy() : getUserId());

        BenefitEnrollment saved = enrollmentRepository.save(enrollment);

        log.info("Enrollment approved: {}", enrollmentId);
        return toResponse(saved);
    }

    @Transactional
    public BenefitEnrollmentResponse rejectEnrollment(String enrollmentId, RejectEnrollmentRequest request) {
        log.info("Rejecting enrollment: {}", enrollmentId);

        String tenantId = getTenantId();
        BenefitEnrollment enrollment = enrollmentRepository.findById(enrollmentId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));

        enrollment.setStatus(EnrollmentStatus.REJECTED);
        enrollment.setUpdatedAt(Instant.now());
        enrollment.setUpdatedBy(request.getRejectedBy() != null ? request.getRejectedBy() : getUserId());

        BenefitEnrollment saved = enrollmentRepository.save(enrollment);

        log.info("Enrollment rejected: {}", enrollmentId);
        return toResponse(saved);
    }

    private BenefitEnrollmentResponse toResponse(BenefitEnrollment enrollment) {
        return BenefitEnrollmentResponse.builder()
                .id(enrollment.getId())
                .tenantId(enrollment.getTenantId())
                .employeeId(enrollment.getEmployeeId())
                .planId(enrollment.getPlanId())
                .coverageLevel(enrollment.getCoverageLevel())
                .coverageOptionCode(enrollment.getCoverageOptionCode())
                .effectiveDate(enrollment.getEffectiveDate())
                .expiryDate(enrollment.getExpiryDate())
                .status(enrollment.getStatus())
                .employeePremium(enrollment.getEmployeePremium())
                .employerPremium(enrollment.getEmployerPremium())
                .totalPremium(enrollment.getTotalPremium())
                .currency(enrollment.getCurrency())
                .notes(enrollment.getNotes())
                .waiveCoverage(enrollment.getWaiveCoverage())
                .waiverReason(enrollment.getWaiverReason())
                .evidenceDocuments(enrollment.getEvidenceDocuments())
                .createdAt(enrollment.getCreatedAt() != null ? LocalDateTime.ofInstant(enrollment.getCreatedAt(), java.time.ZoneOffset.systemDefault()) : null)
                .updatedAt(enrollment.getUpdatedAt() != null ? LocalDateTime.ofInstant(enrollment.getUpdatedAt(), java.time.ZoneOffset.systemDefault()) : null)
                .updatedBy(enrollment.getUpdatedBy())
                .build();
    }

    private String getTenantId() {
        return RequestContextHolder.getTenantId();
    }

    private String getUserId() {
        return RequestContextHolder.getUserId().orElse("system");
    }
}
