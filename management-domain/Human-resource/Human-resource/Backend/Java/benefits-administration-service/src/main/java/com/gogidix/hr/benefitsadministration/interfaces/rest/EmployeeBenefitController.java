package com.gogidix.hr.benefitsadministration.interfaces.rest;

import com.gogidix.hr.benefitsadministration.application.dto.request.*;
import com.gogidix.hr.benefitsadministration.application.dto.response.*;
import com.gogidix.hr.benefitsadministration.application.service.EmployeeBenefitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Employee Benefit operations
 */
@RestController
@RequestMapping("/api/employee-benefits")
@RequiredArgsConstructor
@Slf4j
public class EmployeeBenefitController {

    private final EmployeeBenefitService employeeBenefitService;

    @GetMapping("/employee/{employeeId}/summary")
    public ResponseEntity<EmployeeBenefitSummaryResponse> getEmployeeBenefitSummary(@PathVariable String employeeId) {
        log.info("GET /api/employee-benefits/employee/{}/summary - Fetching benefit summary", employeeId);
        return employeeBenefitService.getEmployeeBenefitSummary(employeeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}/available-plans")
    public ResponseEntity<List<BenefitPlanResponse>> getAvailablePlansForEmployee(@PathVariable String employeeId) {
        log.info("GET /api/employee-benefits/employee/{}/available-plans - Fetching available plans", employeeId);
        List<BenefitPlanResponse> plans = employeeBenefitService.getAvailablePlansForEmployee(employeeId);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/employee/{employeeId}/enrollment-history")
    public ResponseEntity<List<BenefitEnrollmentResponse>> getEnrollmentHistory(@PathVariable String employeeId) {
        log.info("GET /api/employee-benefits/employee/{}/enrollment-history - Fetching enrollment history", employeeId);
        List<BenefitEnrollmentResponse> history = employeeBenefitService.getEnrollmentHistory(employeeId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/enroll")
    public ResponseEntity<EnrollmentConfirmationResponse> enrollEmployee(
            @Valid @RequestBody EnrollEmployeeRequest request) {
        log.info("POST /api/employee-benefits/enroll - Enrolling employee {} in plan {}", request.getEmployeeId(), request.getPlanId());
        EnrollmentConfirmationResponse response = employeeBenefitService.enrollEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/cancel")
    public ResponseEntity<BenefitEnrollmentResponse> cancelEnrollment(
            @Valid @RequestBody CancelEnrollmentRequest request) {
        log.info("POST /api/employee-benefits/cancel - Canceling enrollment {}", request.getEnrollmentId());
        BenefitEnrollmentResponse response = employeeBenefitService.cancelEnrollment(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/dependents/add")
    public ResponseEntity<DependentCoverageResponse> addDependent(
            @Valid @RequestBody AddDependentRequest request) {
        log.info("POST /api/employee-benefits/dependents/add - Adding dependent to enrollment {}", request.getEnrollmentId());
        DependentCoverageResponse response = employeeBenefitService.addDependent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/dependents/remove")
    public ResponseEntity<DependentCoverageResponse> removeDependent(
            @Valid @RequestBody RemoveDependentRequest request) {
        log.info("POST /api/employee-benefits/dependents/remove - Removing dependent {} from enrollment {}",
                request.getDependentId(), request.getEnrollmentId());
        DependentCoverageResponse response = employeeBenefitService.removeDependent(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/coverage/update")
    public ResponseEntity<BenefitEnrollmentResponse> updateCoverage(
            @Valid @RequestBody UpdateCoverageRequest request) {
        log.info("POST /api/employee-benefits/coverage/update - Updating coverage for enrollment {}", request.getEnrollmentId());
        BenefitEnrollmentResponse response = employeeBenefitService.updateCoverage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeId}/eligible")
    public ResponseEntity<Boolean> isEmployeeEligible(@PathVariable String employeeId) {
        log.info("GET /api/employee-benefits/employee/{}/eligible - Checking eligibility", employeeId);
        boolean eligible = employeeBenefitService.isEmployeeEligible(employeeId);
        return ResponseEntity.ok(eligible);
    }

    @PostMapping("/validate-eligibility")
    public ResponseEntity<EligibilityCheckResponse> validateEligibility(
            @RequestParam String employeeId,
            @RequestParam String planId) {
        log.info("POST /api/employee-benefits/validate-eligibility - Validating eligibility for employee {} in plan {}",
                employeeId, planId);
        EligibilityCheckResponse response = employeeBenefitService.validateEligibility(employeeId, planId);
        return ResponseEntity.ok(response);
    }
}
