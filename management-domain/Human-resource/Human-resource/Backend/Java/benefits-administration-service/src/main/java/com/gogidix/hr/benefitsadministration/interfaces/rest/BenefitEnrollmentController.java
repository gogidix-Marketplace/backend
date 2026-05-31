package com.gogidix.hr.benefitsadministration.interfaces.rest;

import com.gogidix.hr.benefitsadministration.application.dto.request.*;
import com.gogidix.hr.benefitsadministration.application.dto.response.*;
import com.gogidix.hr.benefitsadministration.application.service.BenefitEnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Benefit Enrollment operations
 */
@RestController
@RequestMapping("/api/benefit-enrollments")
@RequiredArgsConstructor
@Slf4j
public class BenefitEnrollmentController {

    private final BenefitEnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<EnrollmentConfirmationResponse> enrollEmployee(
            @Valid @RequestBody CreateBenefitEnrollmentRequest request) {
        log.info("POST /api/benefit-enrollments - Enrolling employee {} in plan {}", request.getEmployeeId(), request.getPlanId());
        EnrollmentConfirmationResponse response = enrollmentService.enrollEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<EnrollmentConfirmationResponse>> bulkEnrollEmployees(
            @Valid @RequestBody List<CreateBenefitEnrollmentRequest> requests) {
        log.info("POST /api/benefit-enrollments/batch - Bulk enrolling {} employees", requests.size());
        List<EnrollmentConfirmationResponse> responses = enrollmentService.bulkEnrollEmployees(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @PostMapping("/{enrollmentId}/re-enroll")
    public ResponseEntity<EnrollmentConfirmationResponse> reEnrollEmployee(@PathVariable String enrollmentId) {
        log.info("POST /api/benefit-enrollments/{}/re-enroll - Re-enrolling employee", enrollmentId);
        EnrollmentConfirmationResponse response = enrollmentService.reEnrollEmployee(enrollmentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{enrollmentId}/cancel")
    public ResponseEntity<BenefitEnrollmentResponse> cancelEnrollment(
            @PathVariable String enrollmentId,
            @Valid @RequestBody CancelEnrollmentRequest request) {
        log.info("POST /api/benefit-enrollments/{}/cancel - Canceling enrollment", enrollmentId);
        BenefitEnrollmentResponse response = enrollmentService.cancelEnrollment(
                enrollmentId,
                request.getCancellationReason(),
                request.getEffectiveDate()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{enrollmentId}")
    public ResponseEntity<BenefitEnrollmentResponse> updateEnrollment(
            @PathVariable String enrollmentId,
            @Valid @RequestBody UpdateBenefitEnrollmentRequest request) {
        log.info("PUT /api/benefit-enrollments/{} - Updating enrollment", enrollmentId);
        BenefitEnrollmentResponse response = enrollmentService.updateEnrollment(enrollmentId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{enrollmentId}/approve")
    public ResponseEntity<BenefitEnrollmentResponse> approveEnrollment(
            @PathVariable String enrollmentId,
            @Valid @RequestBody ApproveEnrollmentRequest request) {
        log.info("POST /api/benefit-enrollments/{}/approve - Approving enrollment", enrollmentId);
        BenefitEnrollmentResponse response = enrollmentService.approveEnrollment(enrollmentId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{enrollmentId}/reject")
    public ResponseEntity<BenefitEnrollmentResponse> rejectEnrollment(
            @PathVariable String enrollmentId,
            @Valid @RequestBody RejectEnrollmentRequest request) {
        log.info("POST /api/benefit-enrollments/{}/reject - Rejecting enrollment", enrollmentId);
        BenefitEnrollmentResponse response = enrollmentService.rejectEnrollment(enrollmentId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{enrollmentId}")
    public ResponseEntity<BenefitEnrollmentResponse> getEnrollment(@PathVariable String enrollmentId) {
        log.info("GET /api/benefit-enrollments/{} - Fetching enrollment", enrollmentId);
        return enrollmentService.getEnrollmentById(enrollmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<BenefitEnrollmentResponse>> getEnrollmentsByEmployee(@PathVariable String employeeId) {
        log.info("GET /api/benefit-enrollments/employee/{} - Fetching enrollments for employee", employeeId);
        List<BenefitEnrollmentResponse> enrollments = enrollmentService.getEnrollmentsByEmployee(employeeId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/employee/{employeeId}/active")
    public ResponseEntity<List<BenefitEnrollmentResponse>> getActiveEnrollmentsByEmployee(@PathVariable String employeeId) {
        log.info("GET /api/benefit-enrollments/employee/{}/active - Fetching active enrollments", employeeId);
        List<BenefitEnrollmentResponse> enrollments = enrollmentService.getActiveEnrollmentsByEmployee(employeeId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/plan/{planId}")
    public ResponseEntity<List<BenefitEnrollmentResponse>> getEnrollmentsByPlan(@PathVariable String planId) {
        log.info("GET /api/benefit-enrollments/plan/{} - Fetching enrollments for plan", planId);
        List<BenefitEnrollmentResponse> enrollments = enrollmentService.getEnrollmentsByPlan(planId);
        return ResponseEntity.ok(enrollments);
    }

    @PostMapping("/employee/{employeeId}/terminate")
    public ResponseEntity<List<BenefitEnrollmentResponse>> terminateEmployeeEnrollments(
            @PathVariable String employeeId,
            @RequestParam String terminationDate) {
        log.info("POST /api/benefit-enrollments/employee/{}/terminate - Terminating enrollments", employeeId);
        List<BenefitEnrollmentResponse> responses = enrollmentService.terminateEmployeeEnrollments(
                employeeId,
                java.time.LocalDate.parse(terminationDate)
        );
        return ResponseEntity.ok(responses);
    }
}
