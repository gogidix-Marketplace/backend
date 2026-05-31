package com.gogidix.hr.benefitsadministration.interfaces.rest;

import com.gogidix.hr.benefitsadministration.application.dto.request.*;
import com.gogidix.hr.benefitsadministration.application.dto.response.*;
import com.gogidix.hr.benefitsadministration.application.service.BenefitPlanService;
import com.gogidix.hr.benefitsadministration.shared.requestcontext.RequestContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Benefit Plan operations
 * Provides endpoints for managing benefit plans
 */
@RestController
@RequestMapping("/api/benefit-plans")
@RequiredArgsConstructor
@Slf4j
public class BenefitPlanController {

    private final BenefitPlanService benefitPlanService;

    @PostMapping
    public ResponseEntity<BenefitPlanResponse> createBenefitPlan(
            @Valid @RequestBody CreateBenefitPlanRequest request) {
        log.info("POST /api/benefit-plans - Creating benefit plan: {}", request.getPlanCode());
        BenefitPlanResponse response = benefitPlanService.createBenefitPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<BenefitPlanResponse>> createBenefitPlans(
            @Valid @RequestBody List<CreateBenefitPlanRequest> requests) {
        log.info("POST /api/benefit-plans/batch - Creating {} benefit plans", requests.size());
        List<BenefitPlanResponse> responses = benefitPlanService.createBenefitPlans(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @PutMapping("/{planId}")
    public ResponseEntity<BenefitPlanResponse> updateBenefitPlan(
            @PathVariable String planId,
            @Valid @RequestBody UpdateBenefitPlanRequest request) {
        log.info("PUT /api/benefit-plans/{} - Updating benefit plan", planId);
        BenefitPlanResponse response = benefitPlanService.updateBenefitPlan(planId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{planId}/activate")
    public ResponseEntity<BenefitPlanResponse> activateBenefitPlan(@PathVariable String planId) {
        log.info("POST /api/benefit-plans/{}/activate - Activating benefit plan", planId);
        BenefitPlanResponse response = benefitPlanService.activateBenefitPlan(planId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{planId}/deactivate")
    public ResponseEntity<BenefitPlanResponse> deactivateBenefitPlan(@PathVariable String planId) {
        log.info("POST /api/benefit-plans/{}/deactivate - Deactivating benefit plan", planId);
        BenefitPlanResponse response = benefitPlanService.deactivateBenefitPlan(planId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{planId}")
    public ResponseEntity<BenefitPlanResponse> getBenefitPlan(@PathVariable String planId) {
        log.info("GET /api/benefit-plans/{} - Fetching benefit plan", planId);
        return benefitPlanService.getBenefitPlanById(planId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{planId}/details")
    public ResponseEntity<BenefitPlanDetailResponse> getBenefitPlanDetail(@PathVariable String planId) {
        log.info("GET /api/benefit-plans/{}/details - Fetching benefit plan details", planId);
        return benefitPlanService.getBenefitPlanDetail(planId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<BenefitPlanResponse>> getBenefitPlans(
            @RequestParam(required = false) String tenantId) {
        log.info("GET /api/benefit-plans - Fetching benefit plans for tenant: {}", tenantId);
        String resolvedTenantId = tenantId != null ? tenantId : RequestContextHolder.getTenantId();
        List<BenefitPlanResponse> plans = benefitPlanService.getActiveBenefitPlans(resolvedTenantId);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/country/{countryCode}")
    public ResponseEntity<List<BenefitPlanResponse>> getBenefitPlansByCountry(
            @PathVariable String countryCode,
            @RequestParam(required = false) String tenantId) {
        log.info("GET /api/benefit-plans/country/{} - Fetching benefit plans by country", countryCode);
        String resolvedTenantId = tenantId != null ? tenantId : RequestContextHolder.getTenantId();
        List<BenefitPlanResponse> plans = benefitPlanService.getBenefitPlansByCountry(resolvedTenantId, countryCode);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/type/{benefitType}")
    public ResponseEntity<List<BenefitPlanResponse>> getBenefitPlansByType(
            @PathVariable String benefitType,
            @RequestParam(required = false) String tenantId) {
        log.info("GET /api/benefit-plans/type/{} - Fetching benefit plans by type", benefitType);
        String resolvedTenantId = tenantId != null ? tenantId : RequestContextHolder.getTenantId();
        List<BenefitPlanResponse> plans = benefitPlanService.getBenefitPlansByType(resolvedTenantId, benefitType);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BenefitPlanResponse>> searchBenefitPlans(
            @ModelAttribute SearchBenefitPlansRequest request) {
        log.info("GET /api/benefit-plans/search - Searching benefit plans");
        List<BenefitPlanResponse> plans = benefitPlanService.searchBenefitPlans(request);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<BenefitPlanResponse>> getPlansExpiringSoon(
            @RequestParam(defaultValue = "30") int daysBeforeExpiry,
            @RequestParam(required = false) String tenantId) {
        log.info("GET /api/benefit-plans/expiring - Fetching plans expiring within {} days", daysBeforeExpiry);
        String resolvedTenantId = tenantId != null ? tenantId : RequestContextHolder.getTenantId();
        List<BenefitPlanResponse> plans = benefitPlanService.getPlansExpiringSoon(resolvedTenantId, daysBeforeExpiry);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/effective")
    public ResponseEntity<List<BenefitPlanResponse>> getPlansEffectiveOn(
            @RequestParam String effectiveDate,
            @RequestParam(required = false) String tenantId) {
        log.info("GET /api/benefit-plans/effective - Fetching plans effective on {}", effectiveDate);
        String resolvedTenantId = tenantId != null ? tenantId : RequestContextHolder.getTenantId();
        List<BenefitPlanResponse> plans = benefitPlanService.getPlansEffectiveOn(resolvedTenantId, java.time.LocalDate.parse(effectiveDate));
        return ResponseEntity.ok(plans);
    }

    @PostMapping("/calculate-premium")
    public ResponseEntity<PremiumCalculationResponse> calculatePremium(
            @Valid @RequestBody CalculatePremiumRequest request) {
        log.info("POST /api/benefit-plans/calculate-premium - Calculating premium");
        PremiumCalculationResponse response = benefitPlanService.calculatePremium(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate-eligibility")
    public ResponseEntity<EligibilityCheckResponse> validateEligibility(
            @Valid @RequestBody ValidateEligibilityRequest request) {
        log.info("POST /api/benefit-plans/validate-eligibility - Validating eligibility");
        EligibilityCheckResponse response = benefitPlanService.validateEligibility(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available-for-enrollment")
    public ResponseEntity<List<BenefitPlanResponse>> getAvailablePlansForEnrollment(
            @RequestParam String employeeId,
            @RequestParam(required = false) String tenantId) {
        log.info("GET /api/benefit-plans/available-for-enrollment - Fetching available plans for employee: {}", employeeId);
        String resolvedTenantId = tenantId != null ? tenantId : RequestContextHolder.getTenantId();
        List<BenefitPlanResponse> plans = benefitPlanService.getAvailablePlansForEnrollment(resolvedTenantId, employeeId);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/{planId}/eligibility-requirements")
    public ResponseEntity<List<String>> getEligibilityRequirements(@PathVariable String planId) {
        log.info("GET /api/benefit-plans/{}/eligibility-requirements - Fetching eligibility requirements", planId);
        List<String> requirements = benefitPlanService.getEligibilityRequirements(planId);
        return ResponseEntity.ok(requirements);
    }

    @GetMapping("/{planId}/dependent-coverage-allowed")
    public ResponseEntity<Boolean> isDependentCoverageAllowed(
            @PathVariable String planId,
            @RequestParam String employeeId) {
        log.info("GET /api/benefit-plans/{}/dependent-coverage-allowed - Checking dependent coverage", planId);
        boolean allowed = benefitPlanService.isDependentCoverageAllowed(employeeId, planId);
        return ResponseEntity.ok(allowed);
    }
}
