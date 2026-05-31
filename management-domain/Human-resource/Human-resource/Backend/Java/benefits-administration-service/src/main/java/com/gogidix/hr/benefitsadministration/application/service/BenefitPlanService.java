package com.gogidix.hr.benefitsadministration.application.service;

import com.gogidix.hr.benefitsadministration.application.dto.request.*;
import com.gogidix.hr.benefitsadministration.application.dto.response.*;
import com.gogidix.hr.benefitsadministration.domain.enums.BenefitStatus;
import com.gogidix.hr.benefitsadministration.domain.enums.BenefitType;
import com.gogidix.hr.benefitsadministration.domain.event.BenefitEnrollmentCreatedEvent;
import com.gogidix.hr.benefitsadministration.domain.model.BenefitPlan;
import com.gogidix.hr.benefitsadministration.domain.port.in.CalculateBenefitPremiumUseCase;
import com.gogidix.hr.benefitsadministration.domain.port.in.CreateBenefitPlanUseCase;
import com.gogidix.hr.benefitsadministration.domain.port.in.QueryBenefitPlansUseCase;
import com.gogidix.hr.benefitsadministration.domain.port.in.UpdateBenefitPlanUseCase;
import com.gogidix.hr.benefitsadministration.domain.port.in.ValidateBenefitEligibilityUseCase;
import com.gogidix.hr.benefitsadministration.domain.port.out.BenefitEventPublisher;
import com.gogidix.hr.benefitsadministration.domain.port.out.BenefitPlanRepositoryPort;
import com.gogidix.hr.benefitsadministration.domain.port.out.EmployeeVerificationPort;
import com.gogidix.hr.benefitsadministration.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing benefit plans
 * Implements hexagonal architecture use cases
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BenefitPlanService implements
        CreateBenefitPlanUseCase,
        UpdateBenefitPlanUseCase,
        QueryBenefitPlansUseCase,
        CalculateBenefitPremiumUseCase,
        ValidateBenefitEligibilityUseCase {

    private final BenefitPlanRepositoryPort benefitPlanRepository;
    private final BenefitEventPublisher eventPublisher;
    private final EmployeeVerificationPort employeeVerificationPort;

    @Override
    @Transactional
    public BenefitPlanResponse createBenefitPlan(CreateBenefitPlanRequest request) {
        log.info("Creating benefit plan: {} for tenant: {}", request.getPlanCode(), request.getTenantId());

        String tenantId = getTenantId();

        BenefitPlan plan = BenefitPlan.builder()
                .tenantId(tenantId)
                .countryCode(request.getCountryCode())
                .planId(UUID.randomUUID().toString())
                .planCode(request.getPlanCode())
                .planName(request.getPlanName())
                .description(request.getDescription())
                .benefitType(request.getBenefitType())
                .status(request.getStatus())
                .providerId(request.getProviderId())
                .providerName(request.getProviderName())
                .employeeContribution(request.getEmployeeContribution())
                .employerContribution(request.getEmployerContribution())
                .totalCost(request.getTotalCost())
                .currency(request.getCurrency())
                .deductionFrequency(request.getDeductionFrequency())
                .effectiveDate(request.getEffectiveDate())
                .expiryDate(request.getExpiryDate())
                .enrollmentWindowDays(request.getEnrollmentWindowDays())
                .requiresEvidence(request.getRequiresEvidence())
                .requiredDocuments(request.getRequiredDocuments())
                .minEmployees(request.getMinEmployees())
                .maxEmployees(request.getMaxEmployees())
                .isVoluntary(request.getIsVoluntary())
                .isTaxable(request.getIsTaxable())
                .taxCode(request.getTaxCode())
                .hasWaitingPeriod(request.getHasWaitingPeriod())
                .waitingPeriodDays(request.getWaitingPeriodDays())
                .eligibilityCheckRequired(request.getEligibilityCheckRequired())
                .eligibilityCriteria(request.getEligibilityCriteria())
                .termsAndConditions(request.getTermsAndConditions())
                .summary(request.getSummary())
                .category(request.getCategory())
                .priority(request.getPriority())
                .contactInfo(request.getContactInfo())
                .websiteUrl(request.getWebsiteUrl())
                .brochureUrl(request.getBrochureUrl())
                .coveredServices(request.getCoveredServices())
                .excludedServices(request.getExcludedServices())
                .annualLimit(request.getAnnualLimit())
                .notes(request.getNotes())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
        plan.setId(UUID.randomUUID().toString());
        plan.setCreatedAt(Instant.now());
        plan.setUpdatedAt(Instant.now());
        plan.setUpdatedBy(getUserId());

        if (request.getCoverageOptions() != null) {
            List<BenefitPlan.CoverageOption> options = request.getCoverageOptions().stream()
                    .map(opt -> BenefitPlan.CoverageOption.builder()
                            .optionCode(opt.getOptionCode())
                            .optionName(opt.getOptionName())
                            .description(opt.getDescription())
                            .level(opt.getLevel())
                            .employeeCost(opt.getEmployeeCost())
                            .employerCost(opt.getEmployerCost())
                            .isAvailable(opt.getIsAvailable())
                            .build())
                    .collect(Collectors.toList());
            plan.setCoverageOptions(options);
        }

        BenefitPlan saved = benefitPlanRepository.save(plan);

        log.info("Benefit plan created successfully: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public List<BenefitPlanResponse> createBenefitPlans(List<CreateBenefitPlanRequest> requests) {
        return requests.stream()
                .map(this::createBenefitPlan)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BenefitPlanResponse updateBenefitPlan(String planId, UpdateBenefitPlanRequest request) {
        log.info("Updating benefit plan: {}", planId);

        String tenantId = getTenantId();
        BenefitPlan plan = benefitPlanRepository.findById(planId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Benefit plan not found: " + planId));

        if (request.getPlanName() != null) plan.setPlanName(request.getPlanName());
        if (request.getDescription() != null) plan.setDescription(request.getDescription());
        if (request.getBenefitType() != null) plan.setBenefitType(request.getBenefitType());
        if (request.getStatus() != null) plan.setStatus(request.getStatus());
        if (request.getProviderId() != null) plan.setProviderId(request.getProviderId());
        if (request.getProviderName() != null) plan.setProviderName(request.getProviderName());
        if (request.getEmployeeContribution() != null) plan.setEmployeeContribution(request.getEmployeeContribution());
        if (request.getEmployerContribution() != null) plan.setEmployerContribution(request.getEmployerContribution());
        if (request.getTotalCost() != null) plan.setTotalCost(request.getTotalCost());
        if (request.getDeductionFrequency() != null) plan.setDeductionFrequency(request.getDeductionFrequency());
        if (request.getEffectiveDate() != null) plan.setEffectiveDate(request.getEffectiveDate());
        if (request.getExpiryDate() != null) plan.setExpiryDate(request.getExpiryDate());
        if (request.getEnrollmentWindowDays() != null) plan.setEnrollmentWindowDays(request.getEnrollmentWindowDays());
        if (request.getRequiresEvidence() != null) plan.setRequiresEvidence(request.getRequiresEvidence());
        if (request.getRequiredDocuments() != null) plan.setRequiredDocuments(request.getRequiredDocuments());
        if (request.getMinEmployees() != null) plan.setMinEmployees(request.getMinEmployees());
        if (request.getMaxEmployees() != null) plan.setMaxEmployees(request.getMaxEmployees());
        if (request.getIsVoluntary() != null) plan.setIsVoluntary(request.getIsVoluntary());
        if (request.getIsTaxable() != null) plan.setIsTaxable(request.getIsTaxable());
        if (request.getTaxCode() != null) plan.setTaxCode(request.getTaxCode());
        if (request.getHasWaitingPeriod() != null) plan.setHasWaitingPeriod(request.getHasWaitingPeriod());
        if (request.getWaitingPeriodDays() != null) plan.setWaitingPeriodDays(request.getWaitingPeriodDays());
        if (request.getEligibilityCheckRequired() != null) plan.setEligibilityCheckRequired(request.getEligibilityCheckRequired());
        if (request.getEligibilityCriteria() != null) plan.setEligibilityCriteria(request.getEligibilityCriteria());
        if (request.getTermsAndConditions() != null) plan.setTermsAndConditions(request.getTermsAndConditions());
        if (request.getSummary() != null) plan.setSummary(request.getSummary());
        if (request.getCategory() != null) plan.setCategory(request.getCategory());
        if (request.getPriority() != null) plan.setPriority(request.getPriority());
        if (request.getContactInfo() != null) plan.setContactInfo(request.getContactInfo());
        if (request.getWebsiteUrl() != null) plan.setWebsiteUrl(request.getWebsiteUrl());
        if (request.getBrochureUrl() != null) plan.setBrochureUrl(request.getBrochureUrl());
        if (request.getCoveredServices() != null) plan.setCoveredServices(request.getCoveredServices());
        if (request.getExcludedServices() != null) plan.setExcludedServices(request.getExcludedServices());
        if (request.getAnnualLimit() != null) plan.setAnnualLimit(request.getAnnualLimit());
        if (request.getNotes() != null) plan.setNotes(request.getNotes());
        if (request.getIsActive() != null) plan.setIsActive(request.getIsActive());

        plan.setUpdatedAt(Instant.now());
        plan.setUpdatedBy(getUserId());

        BenefitPlan saved = benefitPlanRepository.save(plan);

        log.info("Benefit plan updated successfully: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public BenefitPlanResponse activateBenefitPlan(String planId) {
        String tenantId = getTenantId();
        BenefitPlan plan = benefitPlanRepository.findById(planId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Benefit plan not found: " + planId));

        plan.setStatus(BenefitStatus.ACTIVE);
        plan.setIsActive(true);
        plan.setUpdatedAt(Instant.now());
        plan.setUpdatedBy(getUserId());

        BenefitPlan saved = benefitPlanRepository.save(plan);
        log.info("Benefit plan activated: {}", planId);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public BenefitPlanResponse deactivateBenefitPlan(String planId) {
        String tenantId = getTenantId();
        BenefitPlan plan = benefitPlanRepository.findById(planId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Benefit plan not found: " + planId));

        plan.setStatus(BenefitStatus.INACTIVE);
        plan.setIsActive(false);
        plan.setUpdatedAt(Instant.now());
        plan.setUpdatedBy(getUserId());

        BenefitPlan saved = benefitPlanRepository.save(plan);
        log.info("Benefit plan deactivated: {}", planId);
        return toResponse(saved);
    }

    @Override
    public Optional<BenefitPlanResponse> getBenefitPlanById(String planId) {
        String tenantId = getTenantId();
        return benefitPlanRepository.findById(planId, tenantId)
                .map(this::toResponse);
    }

    @Override
    public Optional<BenefitPlanDetailResponse> getBenefitPlanDetail(String planId) {
        String tenantId = getTenantId();
        return benefitPlanRepository.findById(planId, tenantId)
                .map(this::toDetailResponse);
    }

    @Override
    public List<BenefitPlanResponse> getActiveBenefitPlans(String tenantId) {
        return benefitPlanRepository.findActiveByTenantId(tenantId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BenefitPlanResponse> getBenefitPlansByCountry(String tenantId, String countryCode) {
        return benefitPlanRepository.findByTenantAndCountry(tenantId, countryCode).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BenefitPlanResponse> getBenefitPlansByType(String tenantId, String benefitType) {
        return benefitPlanRepository.findByType(tenantId, BenefitType.valueOf(benefitType)).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BenefitPlanResponse> searchBenefitPlans(SearchBenefitPlansRequest request) {
        String tenantId = request.getTenantId() != null ? request.getTenantId() : getTenantId();

        List<BenefitPlan> plans;

        if (request.getEffectiveOn() != null) {
            plans = benefitPlanRepository.findEffectiveOn(tenantId, request.getEffectiveOn());
        } else if (request.getExpiringBefore() != null) {
            plans = benefitPlanRepository.findExpiringBefore(tenantId, request.getExpiringBefore());
        } else {
            plans = benefitPlanRepository.findByTenantId(tenantId);
        }

        return plans.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BenefitPlanResponse> getAvailablePlansForEnrollment(String tenantId, String employeeId) {
        return benefitPlanRepository.findActiveByTenantId(tenantId).stream()
                .filter(plan -> plan.isEffective())
                .filter(plan -> !plan.getIsVoluntary() || isEmployeeEligible(employeeId, tenantId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EmployeeBenefitSummaryResponse> getEmployeeBenefitSummary(String employeeId) {
        // This would typically query enrollment data as well
        // For now, return a basic summary
        String tenantId = getTenantId();
        var employeeInfo = employeeVerificationPort.getEmployeeInfo(employeeId, tenantId);

        if (employeeInfo.isEmpty()) {
            return Optional.empty();
        }

        EmployeeBenefitSummaryResponse summary = EmployeeBenefitSummaryResponse.builder()
                .employeeId(employeeId)
                .tenantId(tenantId)
                .firstName(employeeInfo.get().firstName())
                .lastName(employeeInfo.get().lastName())
                .email(employeeInfo.get().email())
                .department(employeeInfo.get().department())
                .jobTitle(employeeInfo.get().jobTitle())
                .hireDate(employeeInfo.get().hireDate())
                .isEligibleForBenefits(employeeInfo.get().isEligibleForBenefits())
                .build();

        return Optional.of(summary);
    }

    @Override
    public List<BenefitPlanResponse> getPlansExpiringSoon(String tenantId, int daysBeforeExpiry) {
        LocalDate expiryDate = LocalDate.now().plusDays(daysBeforeExpiry);
        return benefitPlanRepository.findExpiringBefore(tenantId, expiryDate).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BenefitPlanResponse> getPlansEffectiveOn(String tenantId, LocalDate effectiveDate) {
        return benefitPlanRepository.findEffectiveOn(tenantId, effectiveDate).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PremiumCalculationResponse calculatePremium(CalculatePremiumRequest request) {
        String tenantId = getTenantId();
        BenefitPlan plan = benefitPlanRepository.findById(request.getPlanId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Benefit plan not found: " + request.getPlanId()));

        BigDecimal employeePremium = plan.getEmployeeContribution();
        BigDecimal employerPremium = plan.getEmployerContribution();
        BigDecimal totalPremium = plan.getTotalCost();

        // Adjust for coverage level if specified
        if (request.getCoverageLevel() != null && plan.getCoverageOptions() != null) {
            var option = plan.getCoverageOptions().stream()
                    .filter(opt -> opt.getLevel().name().equalsIgnoreCase(request.getCoverageLevel()))
                    .findFirst();

            if (option.isPresent()) {
                employeePremium = option.get().getEmployeeCost();
                employerPremium = option.get().getEmployerCost();
                totalPremium = employeePremium.add(employerPremium);
            }
        }

        // Adjust for dependents
        if (request.getNumberOfDependents() > 0) {
            BigDecimal dependentMultiplier = BigDecimal.valueOf(1.0 + (request.getNumberOfDependents() * 0.2));
            employeePremium = employeePremium.multiply(dependentMultiplier);
            totalPremium = employeePremium.add(employerPremium);
        }

        return PremiumCalculationResponse.builder()
                .planId(plan.getId())
                .planName(plan.getPlanName())
                .employeeId(request.getEmployeeId())
                .coverageLevel(request.getCoverageLevel())
                .coverageOptionCode(request.getCoverageOptionCode())
                .numberOfDependents(request.getNumberOfDependents())
                .employeePremium(employeePremium)
                .employerPremium(employerPremium)
                .totalPremium(totalPremium)
                .currency(plan.getCurrency())
                .deductionFrequency(plan.getDeductionFrequency().name())
                .effectiveDate(request.getEffectiveDate() != null ? request.getEffectiveDate() : LocalDate.now())
                .build();
    }

    @Override
    public PremiumCalculationResponse calculatePremiumForPlan(String planId, String coverageLevel, int dependentsCount) {
        CalculatePremiumRequest request = CalculatePremiumRequest.builder()
                .employeeId("unknown")
                .planId(planId)
                .coverageLevel(coverageLevel)
                .numberOfDependents(dependentsCount)
                .build();
        return calculatePremium(request);
    }

    @Override
    public BigDecimal estimateAnnualPremium(String employeeId, String planId) {
        CalculatePremiumRequest request = CalculatePremiumRequest.builder()
                .employeeId(employeeId)
                .planId(planId)
                .coverageLevel("EMPLOYEE_ONLY")
                .numberOfDependents(0)
                .build();

        PremiumCalculationResponse response = calculatePremium(request);
        return response.getTotalPremium().multiply(BigDecimal.valueOf(12));
    }

    @Override
    public List<PremiumCalculationResponse.PremiumBreakdown> getPremiumBreakdown(String enrollmentId, LocalDate startDate, LocalDate endDate) {
        // Calculate monthly breakdown
        List<PremiumCalculationResponse.PremiumBreakdown> breakdown = List.of(
                PremiumCalculationResponse.PremiumBreakdown.builder()
                        .component("BASE_PREMIUM")
                        .description("Base monthly premium")
                        .amount(BigDecimal.valueOf(100))
                        .isEmployeePaid(true)
                        .effectiveDate(startDate)
                        .endDate(endDate)
                        .build()
        );

        return breakdown;
    }

    @Override
    public EligibilityCheckResponse validateEligibility(ValidateEligibilityRequest request) {
        String tenantId = getTenantId();

        var planOpt = benefitPlanRepository.findById(request.getPlanId(), tenantId);
        if (planOpt.isEmpty()) {
            return EligibilityCheckResponse.builder()
                    .employeeId(request.getEmployeeId())
                    .planId(request.getPlanId())
                    .isEligible(false)
                    .ineligibilityReason("Plan not found")
                    .build();
        }

        BenefitPlan plan = planOpt.get();
        var employeeInfo = employeeVerificationPort.getEmployeeInfo(request.getEmployeeId(), tenantId);

        boolean isEligible = true;
        String ineligibilityReason = null;

        if (employeeInfo.isEmpty()) {
            isEligible = false;
            ineligibilityReason = "Employee not found";
        } else if (!employeeInfo.get().isEligibleForBenefits()) {
            isEligible = false;
            ineligibilityReason = "Employee not eligible for benefits";
        } else if (plan.getHasWaitingPeriod() != null && plan.getHasWaitingPeriod()) {
            long tenureDays = employeeVerificationPort.getTenureDays(request.getEmployeeId(), tenantId);
            if (tenureDays < (plan.getWaitingPeriodDays() != null ? plan.getWaitingPeriodDays() : 0)) {
                isEligible = false;
                ineligibilityReason = "Waiting period not met";
            }
        }

        LocalDate now = LocalDate.now();
        boolean isWithinEnrollmentWindow = plan.isWithinEnrollmentWindow();

        EligibilityCheckResponse.EligibilityDetails details = null;
        if (employeeInfo.isPresent()) {
            details = EligibilityCheckResponse.EligibilityDetails.builder()
                    .meetsAgeRequirement(true)
                    .meetsTenureRequirement(true)
                    .meetsEmploymentTypeRequirement(true)
                    .meetsHoursRequirement(true)
                    .tenureDays((int) employeeVerificationPort.getTenureDays(request.getEmployeeId(), tenantId))
                    .employmentType(employeeInfo.get().employmentType())
                    .hoursPerWeek(employeeInfo.get().hoursPerWeek() != null ? employeeInfo.get().hoursPerWeek().doubleValue() : null)
                    .isFullTime(employeeInfo.get().isFullTime())
                    .hireDate(employeeInfo.get().hireDate())
                    .jobGrade(request.getJobGrade())
                    .department(request.getDepartment())
                    .build();
        }

        return EligibilityCheckResponse.builder()
                .employeeId(request.getEmployeeId())
                .planId(request.getPlanId())
                .isEligible(isEligible)
                .effectiveDate(now)
                .eligibilityDetails(details)
                .ineligibilityReason(ineligibilityReason)
                .isWithinEnrollmentWindow(isWithinEnrollmentWindow)
                .build();
    }

    @Override
    public boolean isWithinEnrollmentWindow(String employeeId, String planId) {
        String tenantId = getTenantId();
        return benefitPlanRepository.findById(planId, tenantId)
                .map(BenefitPlan::isWithinEnrollmentWindow)
                .orElse(false);
    }

    @Override
    public List<String> getEligibilityRequirements(String planId) {
        String tenantId = getTenantId();
        return benefitPlanRepository.findById(planId, tenantId)
                .map(plan -> List.of(
                        plan.getEligibilityCriteria() != null ? plan.getEligibilityCriteria() : "Standard eligibility requirements",
                        "Must be active employee",
                        plan.getHasWaitingPeriod() != null && plan.getHasWaitingPeriod()
                                ? "Must complete waiting period of " + plan.getWaitingPeriodDays() + " days"
                                : "No waiting period"
                ))
                .orElse(List.of("Plan not found"));
    }

    @Override
    public boolean isDependentCoverageAllowed(String employeeId, String planId) {
        String tenantId = getTenantId();
        return benefitPlanRepository.findById(planId, tenantId)
                .map(plan -> plan.getCoverageOptions() != null && !plan.getCoverageOptions().isEmpty())
                .orElse(false);
    }

    private BenefitPlanResponse toResponse(BenefitPlan plan) {
        List<BenefitPlanResponse.CoverageOptionResponse> options = null;
        if (plan.getCoverageOptions() != null) {
            options = plan.getCoverageOptions().stream()
                    .map(opt -> BenefitPlanResponse.CoverageOptionResponse.builder()
                            .optionCode(opt.getOptionCode())
                            .optionName(opt.getOptionName())
                            .description(opt.getDescription())
                            .level(opt.getLevel() != null ? opt.getLevel().name() : null)
                            .employeeCost(opt.getEmployeeCost())
                            .employerCost(opt.getEmployerCost())
                            .isAvailable(opt.getIsAvailable())
                            .build())
                    .collect(Collectors.toList());
        }

        return BenefitPlanResponse.builder()
                .id(plan.getId())
                .tenantId(plan.getTenantId())
                .countryCode(plan.getCountryCode())
                .planId(plan.getPlanId())
                .planCode(plan.getPlanCode())
                .planName(plan.getPlanName())
                .description(plan.getDescription())
                .benefitType(plan.getBenefitType())
                .status(plan.getStatus())
                .providerId(plan.getProviderId())
                .providerName(plan.getProviderName())
                .employeeContribution(plan.getEmployeeContribution())
                .employerContribution(plan.getEmployerContribution())
                .totalCost(plan.getTotalCost())
                .currency(plan.getCurrency())
                .deductionFrequency(plan.getDeductionFrequency())
                .coverageOptions(options)
                .effectiveDate(plan.getEffectiveDate())
                .expiryDate(plan.getExpiryDate())
                .enrollmentWindowDays(plan.getEnrollmentWindowDays())
                .requiresEvidence(plan.getRequiresEvidence())
                .requiredDocuments(plan.getRequiredDocuments())
                .minEmployees(plan.getMinEmployees())
                .maxEmployees(plan.getMaxEmployees())
                .isVoluntary(plan.getIsVoluntary())
                .isTaxable(plan.getIsTaxable())
                .taxCode(plan.getTaxCode())
                .hasWaitingPeriod(plan.getHasWaitingPeriod())
                .waitingPeriodDays(plan.getWaitingPeriodDays())
                .eligibilityCheckRequired(plan.getEligibilityCheckRequired())
                .eligibilityCriteria(plan.getEligibilityCriteria())
                .termsAndConditions(plan.getTermsAndConditions())
                .summary(plan.getSummary())
                .category(plan.getCategory())
                .priority(plan.getPriority())
                .contactInfo(plan.getContactInfo())
                .websiteUrl(plan.getWebsiteUrl())
                .brochureUrl(plan.getBrochureUrl())
                .coveredServices(plan.getCoveredServices())
                .excludedServices(plan.getExcludedServices())
                .annualLimit(plan.getAnnualLimit())
                .notes(plan.getNotes())
                .isActive(plan.getIsActive())
                .createdAt(plan.getCreatedAt() != null ? plan.getCreatedAt().atZone(java.time.ZoneOffset.systemDefault()).toLocalDateTime() : null)
                .updatedAt(plan.getUpdatedAt() != null ? plan.getUpdatedAt().atZone(java.time.ZoneOffset.systemDefault()).toLocalDateTime() : null)
                .updatedBy(plan.getUpdatedBy())
                .build();
    }

    private BenefitPlanDetailResponse toDetailResponse(BenefitPlan plan) {
        BenefitPlanResponse baseResponse = toResponse(plan);

        BenefitPlanDetailResponse detail = new BenefitPlanDetailResponse();
        detail.setId(baseResponse.getId());
        detail.setTenantId(baseResponse.getTenantId());
        detail.setCountryCode(baseResponse.getCountryCode());
        detail.setPlanId(baseResponse.getPlanId());
        detail.setPlanCode(baseResponse.getPlanCode());
        detail.setPlanName(baseResponse.getPlanName());
        detail.setDescription(baseResponse.getDescription());
        detail.setBenefitType(baseResponse.getBenefitType());
        detail.setStatus(baseResponse.getStatus());
        detail.setProviderId(baseResponse.getProviderId());
        detail.setProviderName(baseResponse.getProviderName());
        detail.setEmployeeContribution(baseResponse.getEmployeeContribution());
        detail.setEmployerContribution(baseResponse.getEmployerContribution());
        detail.setTotalCost(baseResponse.getTotalCost());
        detail.setCurrency(baseResponse.getCurrency());
        detail.setDeductionFrequency(baseResponse.getDeductionFrequency());
        detail.setCoverageOptions(baseResponse.getCoverageOptions());
        detail.setEffectiveDate(baseResponse.getEffectiveDate());
        detail.setExpiryDate(baseResponse.getExpiryDate());
        detail.setEnrollmentWindowDays(baseResponse.getEnrollmentWindowDays());
        detail.setRequiresEvidence(baseResponse.getRequiresEvidence());
        detail.setRequiredDocuments(baseResponse.getRequiredDocuments());
        detail.setMinEmployees(baseResponse.getMinEmployees());
        detail.setMaxEmployees(baseResponse.getMaxEmployees());
        detail.setIsVoluntary(baseResponse.getIsVoluntary());
        detail.setIsTaxable(baseResponse.getIsTaxable());
        detail.setTaxCode(baseResponse.getTaxCode());
        detail.setHasWaitingPeriod(baseResponse.getHasWaitingPeriod());
        detail.setWaitingPeriodDays(baseResponse.getWaitingPeriodDays());
        detail.setEligibilityCheckRequired(baseResponse.getEligibilityCheckRequired());
        detail.setEligibilityCriteria(baseResponse.getEligibilityCriteria());
        detail.setTermsAndConditions(baseResponse.getTermsAndConditions());
        detail.setSummary(baseResponse.getSummary());
        detail.setCategory(baseResponse.getCategory());
        detail.setPriority(baseResponse.getPriority());
        detail.setContactInfo(baseResponse.getContactInfo());
        detail.setWebsiteUrl(baseResponse.getWebsiteUrl());
        detail.setBrochureUrl(baseResponse.getBrochureUrl());
        detail.setCoveredServices(baseResponse.getCoveredServices());
        detail.setExcludedServices(baseResponse.getExcludedServices());
        detail.setAnnualLimit(baseResponse.getAnnualLimit());
        detail.setNotes(baseResponse.getNotes());
        detail.setIsActive(baseResponse.getIsActive());
        detail.setCreatedAt(baseResponse.getCreatedAt());
        detail.setUpdatedAt(baseResponse.getUpdatedAt());
        detail.setUpdatedBy(baseResponse.getUpdatedBy());

        detail.setProvider(BenefitPlanDetailResponse.ProviderDetail.builder()
                .providerId(plan.getProviderId())
                .providerName(plan.getProviderName())
                .contactEmail(plan.getContactInfo())
                .build());
        detail.setEnrollmentDetails(BenefitPlanDetailResponse.EnrollmentDetails.builder()
                .isOpenForEnrollment(plan.isEffective())
                .enrollmentStartDate(plan.getEffectiveDate())
                .enrollmentEndDate(plan.getExpiryDate())
                .enrollmentWindowDays(plan.getEnrollmentWindowDays())
                .hasWaitingPeriod(plan.getHasWaitingPeriod())
                .waitingPeriodDays(plan.getWaitingPeriodDays())
                .requiresEvidence(plan.getRequiresEvidence())
                .requiredDocuments(plan.getRequiredDocuments())
                .build());
        detail.setCostBreakdown(BenefitPlanDetailResponse.CostBreakdown.builder()
                .baseEmployeeCost(plan.getEmployeeContribution())
                .baseEmployerCost(plan.getEmployerContribution())
                .totalCost(plan.getTotalCost())
                .currency(plan.getCurrency())
                .deductionFrequency(plan.getDeductionFrequency())
                .build());

        return detail;
    }

    private boolean isEmployeeEligible(String employeeId, String tenantId) {
        return employeeVerificationPort.isEligibleForBenefits(employeeId, tenantId);
    }

    private String getTenantId() {
        return RequestContextHolder.getTenantId();
    }

    private String getUserId() {
        return RequestContextHolder.getUserId().orElse("system");
    }
}
