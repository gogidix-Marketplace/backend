package com.gogidix.hr.benefitsadministration.application.service;

import com.gogidix.hr.benefitsadministration.application.dto.request.*;
import com.gogidix.hr.benefitsadministration.application.dto.response.*;
import com.gogidix.hr.benefitsadministration.domain.model.BenefitEnrollment;
import com.gogidix.hr.benefitsadministration.domain.port.out.BenefitEnrollmentRepositoryPort;
import com.gogidix.hr.benefitsadministration.domain.port.out.EmployeeVerificationPort;
import com.gogidix.hr.benefitsadministration.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing employee benefits
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeBenefitService {

    private final BenefitEnrollmentRepositoryPort enrollmentRepository;
    private final BenefitPlanService planService;
    private final BenefitEnrollmentService enrollmentService;
    private final EmployeeVerificationPort employeeVerificationPort;

    public Optional<EmployeeBenefitSummaryResponse> getEmployeeBenefitSummary(String employeeId) {
        log.info("Fetching benefit summary for employee: {}", employeeId);

        String tenantId = getTenantId();
        var employeeInfo = employeeVerificationPort.getEmployeeInfo(employeeId, tenantId);

        if (employeeInfo.isEmpty()) {
            log.warn("Employee not found: {}", employeeId);
            return Optional.empty();
        }

        var info = employeeInfo.get();
        List<BenefitEnrollment> enrollments = enrollmentRepository.findActiveByEmployee(employeeId, tenantId);

        List<EmployeeBenefitSummaryResponse.EnrollmentSummary> enrollmentSummaries = enrollments.stream()
                .map(this::toEnrollmentSummary)
                .collect(Collectors.toList());

        BigDecimal totalMonthlyPremium = enrollments.stream()
                .map(BenefitEnrollment::getTotalPremium)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalEmployerContribution = enrollments.stream()
                .map(BenefitEnrollment::getEmployerPremium)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalEmployeeContribution = enrollments.stream()
                .map(BenefitEnrollment::getEmployeePremium)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        EmployeeBenefitSummaryResponse summary = EmployeeBenefitSummaryResponse.builder()
                .employeeId(employeeId)
                .tenantId(tenantId)
                .firstName(info.firstName())
                .lastName(info.lastName())
                .email(info.email())
                .department(info.department())
                .jobTitle(info.jobTitle())
                .hireDate(info.hireDate())
                .isEligibleForBenefits(info.isEligibleForBenefits())
                .enrollments(enrollmentSummaries)
                .totalMonthlyPremium(totalMonthlyPremium)
                .totalEmployerContribution(totalEmployerContribution)
                .totalEmployeeContribution(totalEmployeeContribution)
                .totalDependents(0)
                .nextOpenEnrollmentDate(calculateNextOpenEnrollment())
                .build();

        return Optional.of(summary);
    }

    @Transactional
    public DependentCoverageResponse addDependent(AddDependentRequest request) {
        log.info("Adding dependent to enrollment: {}", request.getEnrollmentId());

        String tenantId = getTenantId();
        BenefitEnrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + request.getEnrollmentId()));

        CalculatePremiumRequest premiumRequest = CalculatePremiumRequest.builder()
                .employeeId(enrollment.getEmployeeId())
                .planId(enrollment.getPlanId())
                .coverageLevel(enrollment.getCoverageLevel())
                .numberOfDependents(1)
                .effectiveDate(enrollment.getEffectiveDate())
                .build();

        PremiumCalculationResponse premium = planService.calculatePremium(premiumRequest);

        enrollment.setEmployeePremium(premium.getEmployeePremium());
        enrollment.setEmployerPremium(premium.getEmployerPremium());
        enrollment.setTotalPremium(premium.getTotalPremium());
        enrollment.setUpdatedAt(Instant.now());
        enrollment.setUpdatedBy(getUserId());

        enrollmentRepository.save(enrollment);

        DependentCoverageResponse dependent = DependentCoverageResponse.builder()
                .id(UUID.randomUUID().toString())
                .enrollmentId(request.getEnrollmentId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .fullName(request.getFirstName() + " " + request.getLastName())
                .relationship(request.getRelationship())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .address(request.getAddress())
                .isStudent(request.getIsStudent())
                .isDisabled(request.getIsDisabled())
                .effectiveDate(LocalDate.now())
                .isActive(true)
                .status("ACTIVE")
                .notes(request.getNotes())
                .createdAt(LocalDate.now())
                .build();

        log.info("Dependent added successfully to enrollment: {}", request.getEnrollmentId());
        return dependent;
    }

    @Transactional
    public DependentCoverageResponse removeDependent(RemoveDependentRequest request) {
        log.info("Removing dependent {} from enrollment: {}", request.getDependentId(), request.getEnrollmentId());

        String tenantId = getTenantId();
        BenefitEnrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + request.getEnrollmentId()));

        CalculatePremiumRequest premiumRequest = CalculatePremiumRequest.builder()
                .employeeId(enrollment.getEmployeeId())
                .planId(enrollment.getPlanId())
                .coverageLevel(enrollment.getCoverageLevel())
                .numberOfDependents(0)
                .effectiveDate(request.getEffectiveDate())
                .build();

        PremiumCalculationResponse premium = planService.calculatePremium(premiumRequest);

        enrollment.setEmployeePremium(premium.getEmployeePremium());
        enrollment.setEmployerPremium(premium.getEmployerPremium());
        enrollment.setTotalPremium(premium.getTotalPremium());
        enrollment.setUpdatedAt(Instant.now());
        enrollment.setUpdatedBy(getUserId());

        enrollmentRepository.save(enrollment);

        DependentCoverageResponse dependent = DependentCoverageResponse.builder()
                .id(request.getDependentId())
                .enrollmentId(request.getEnrollmentId())
                .endDate(request.getEffectiveDate())
                .isActive(false)
                .status("TERMINATED")
                .build();

        log.info("Dependent removed successfully from enrollment: {}", request.getEnrollmentId());
        return dependent;
    }

    @Transactional
    public BenefitEnrollmentResponse updateCoverage(UpdateCoverageRequest request) {
        log.info("Updating coverage for enrollment: {}", request.getEnrollmentId());

        UpdateBenefitEnrollmentRequest updateRequest = UpdateBenefitEnrollmentRequest.builder()
                .coverageLevel(request.getNewCoverageLevel())
                .coverageOptionCode(request.getNewCoverageOptionCode())
                .effectiveDate(request.getEffectiveDate())
                .notes(request.getReason() + "\n" + request.getNotes())
                .build();

        return enrollmentService.updateEnrollment(request.getEnrollmentId(), updateRequest);
    }

    public List<BenefitPlanResponse> getAvailablePlansForEmployee(String employeeId) {
        String tenantId = getTenantId();
        return planService.getAvailablePlansForEnrollment(tenantId, employeeId);
    }

    public List<BenefitEnrollmentResponse> getEnrollmentHistory(String employeeId) {
        String tenantId = getTenantId();
        return enrollmentRepository.findByEmployeeId(employeeId, tenantId).stream()
                .map(enrollment -> BenefitEnrollmentResponse.builder()
                        .id(enrollment.getId())
                        .tenantId(enrollment.getTenantId())
                        .employeeId(enrollment.getEmployeeId())
                        .planId(enrollment.getPlanId())
                        .coverageLevel(enrollment.getCoverageLevel())
                        .effectiveDate(enrollment.getEffectiveDate())
                        .expiryDate(enrollment.getExpiryDate())
                        .status(enrollment.getStatus())
                        .totalPremium(enrollment.getTotalPremium())
                        .currency(enrollment.getCurrency())
                        .createdAt(enrollment.getCreatedAt() != null ? LocalDateTime.ofInstant(enrollment.getCreatedAt(), java.time.ZoneOffset.systemDefault()) : null)
                        .updatedAt(enrollment.getUpdatedAt() != null ? LocalDateTime.ofInstant(enrollment.getUpdatedAt(), java.time.ZoneOffset.systemDefault()) : null)
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentConfirmationResponse enrollEmployee(EnrollEmployeeRequest request) {
        CreateBenefitEnrollmentRequest enrollmentRequest = CreateBenefitEnrollmentRequest.builder()
                .employeeId(request.getEmployeeId())
                .planId(request.getPlanId())
                .tenantId(getTenantId())
                .coverageLevel(request.getCoverageLevel())
                .coverageOptionCode(request.getCoverageOptionCode())
                .effectiveDate(request.getEffectiveDate())
                .build();

        return enrollmentService.enrollEmployee(enrollmentRequest);
    }

    @Transactional
    public BenefitEnrollmentResponse cancelEnrollment(CancelEnrollmentRequest request) {
        return enrollmentService.cancelEnrollment(
                request.getEnrollmentId(),
                request.getCancellationReason(),
                request.getEffectiveDate()
        );
    }

    public boolean isEmployeeEligible(String employeeId) {
        String tenantId = getTenantId();
        return employeeVerificationPort.isEligibleForBenefits(employeeId, tenantId);
    }

    public EligibilityCheckResponse validateEligibility(String employeeId, String planId) {
        ValidateEligibilityRequest request = ValidateEligibilityRequest.builder()
                .employeeId(employeeId)
                .planId(planId)
                .tenantId(getTenantId())
                .build();

        return planService.validateEligibility(request);
    }

    private EmployeeBenefitSummaryResponse.EnrollmentSummary toEnrollmentSummary(BenefitEnrollment enrollment) {
        return EmployeeBenefitSummaryResponse.EnrollmentSummary.builder()
                .enrollmentId(enrollment.getId())
                .planName("Plan " + enrollment.getPlanId())
                .planType("BENEFIT")
                .coverageLevel(enrollment.getCoverageLevel())
                .effectiveDate(enrollment.getEffectiveDate())
                .expiryDate(enrollment.getExpiryDate())
                .status(enrollment.getStatus().name())
                .monthlyPremium(enrollment.getTotalPremium())
                .dependentCount(0)
                .isActive(enrollment.getStatus() == com.gogidix.hr.benefitsadministration.domain.enums.EnrollmentStatus.ACTIVE)
                .build();
    }

    private LocalDate calculateNextOpenEnrollment() {
        LocalDate now = LocalDate.now();
        LocalDate nextNovember = LocalDate.of(now.getYear(), 11, 1);
        if (now.isAfter(nextNovember)) {
            nextNovember = nextNovember.plusYears(1);
        }
        return nextNovember;
    }

    private String getTenantId() {
        return RequestContextHolder.getTenantId();
    }

    private String getUserId() {
        return RequestContextHolder.getUserId().orElse("system");
    }
}
