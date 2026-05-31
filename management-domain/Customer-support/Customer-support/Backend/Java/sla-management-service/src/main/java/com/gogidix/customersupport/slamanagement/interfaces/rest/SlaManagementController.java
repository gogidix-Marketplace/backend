package com.gogidix.customersupport.slamanagement.interfaces.rest;

import com.gogidix.customersupport.slamanagement.application.dto.SLABreachResponseDto;
import com.gogidix.customersupport.slamanagement.application.dto.SLAPolicyRequestDto;
import com.gogidix.customersupport.slamanagement.application.dto.SLAPolicyResponseDto;
import com.gogidix.customersupport.slamanagement.application.service.SlaManagementService;
import com.gogidix.customersupport.slamanagement.domain.model.SLABreach;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/sla-management")
@RequiredArgsConstructor
@Tag(name = "SLA Management", description = "APIs for managing SLA policies and tracking breaches")
public class SlaManagementController {

    private final SlaManagementService slaManagementService;

    @GetMapping("/policies")
    @Operation(summary = "Get all SLA policies", description = "Retrieve all SLA policies")
    public ResponseEntity<List<SLAPolicyResponseDto>> getAllPolicies() {
        return ResponseEntity.ok(slaManagementService.getAllPolicies());
    }

    @GetMapping("/policies/active")
    @Operation(summary = "Get active SLA policies", description = "Retrieve all active SLA policies")
    public ResponseEntity<List<SLAPolicyResponseDto>> getActivePolicies() {
        return ResponseEntity.ok(slaManagementService.getActivePolicies());
    }

    @GetMapping("/policies/{id}")
    @Operation(summary = "Get policy by ID", description = "Retrieve a specific SLA policy by ID")
    public ResponseEntity<SLAPolicyResponseDto> getPolicyById(
            @Parameter(description = "Policy ID") @PathVariable String id) {
        return ResponseEntity.ok(slaManagementService.getPolicyById(id));
    }

    @GetMapping("/policies/code/{policyCode}")
    @Operation(summary = "Get policy by code", description = "Retrieve a specific SLA policy by code")
    public ResponseEntity<SLAPolicyResponseDto> getPolicyByCode(
            @Parameter(description = "Policy code") @PathVariable String policyCode) {
        return ResponseEntity.ok(slaManagementService.getPolicyByCode(policyCode));
    }

    @PostMapping("/policies")
    @Operation(summary = "Create SLA policy", description = "Create a new SLA policy")
    public ResponseEntity<SLAPolicyResponseDto> createPolicy(
            @Valid @RequestBody SLAPolicyRequestDto request) {
        SLAPolicyResponseDto created = slaManagementService.createPolicy(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/policies/{id}")
    @Operation(summary = "Update SLA policy", description = "Update an existing SLA policy")
    public ResponseEntity<SLAPolicyResponseDto> updatePolicy(
            @Parameter(description = "Policy ID") @PathVariable String id,
            @Valid @RequestBody SLAPolicyRequestDto request) {
        return ResponseEntity.ok(slaManagementService.updatePolicy(id, request));
    }

    @DeleteMapping("/policies/{id}")
    @Operation(summary = "Delete SLA policy", description = "Delete an SLA policy")
    public ResponseEntity<Void> deletePolicy(
            @Parameter(description = "Policy ID") @PathVariable String id) {
        slaManagementService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/breaches")
    @Operation(summary = "Get all SLA breaches", description = "Retrieve all SLA breaches")
    public ResponseEntity<List<SLABreachResponseDto>> getAllBreaches() {
        return ResponseEntity.ok(slaManagementService.getAllBreaches());
    }

    @GetMapping("/breaches/unresolved")
    @Operation(summary = "Get unresolved breaches", description = "Retrieve all unresolved SLA breaches")
    public ResponseEntity<List<SLABreachResponseDto>> getUnresolvedBreaches() {
        return ResponseEntity.ok(slaManagementService.getUnresolvedBreaches());
    }

    @GetMapping("/breaches/ticket/{ticketId}")
    @Operation(summary = "Get breaches by ticket", description = "Retrieve all breaches for a specific ticket")
    public ResponseEntity<List<SLABreachResponseDto>> getBreachesByTicketId(
            @Parameter(description = "Ticket ID") @PathVariable String ticketId) {
        return ResponseEntity.ok(slaManagementService.getBreachesByTicketId(ticketId));
    }

    @PostMapping("/breaches")
    @Operation(summary = "Record SLA breach", description = "Record a new SLA breach")
    public ResponseEntity<SLABreachResponseDto> recordBreach(
            @Parameter(description = "Ticket ID") @RequestParam String ticketId,
            @Parameter(description = "Ticket number") @RequestParam String ticketNumber,
            @Parameter(description = "SLA policy ID") @RequestParam String slaPolicyId,
            @Parameter(description = "SLA policy name") @RequestParam String slaPolicyName,
            @Parameter(description = "Breach type") @RequestParam SLABreach.BreachType breachType,
            @Parameter(description = "Due date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant dueDateTime,
            @Parameter(description = "Target time in minutes") @RequestParam Integer targetTimeMinutes) {
        SLABreachResponseDto breach = slaManagementService.recordBreach(
                ticketId, ticketNumber, slaPolicyId, slaPolicyName, breachType, dueDateTime, targetTimeMinutes);
        return ResponseEntity.status(HttpStatus.CREATED).body(breach);
    }

    @PutMapping("/breaches/{breachId}/notify")
    @Operation(summary = "Mark breach as notified", description = "Mark an SLA breach as notified")
    public ResponseEntity<SLABreachResponseDto> markBreachAsNotified(
            @Parameter(description = "Breach ID") @PathVariable String breachId) {
        return ResponseEntity.ok(slaManagementService.markBreachAsNotified(breachId));
    }

    @PutMapping("/breaches/{breachId}/escalate")
    @Operation(summary = "Trigger escalation", description = "Trigger escalation for an SLA breach")
    public ResponseEntity<SLABreachResponseDto> triggerEscalation(
            @Parameter(description = "Breach ID") @PathVariable String breachId,
            @Parameter(description = "Escalation level") @RequestParam Integer level) {
        return ResponseEntity.ok(slaManagementService.triggerEscalation(breachId, level));
    }

    @PutMapping("/breaches/{breachId}/resolve")
    @Operation(summary = "Resolve breach", description = "Resolve an SLA breach with notes")
    public ResponseEntity<SLABreachResponseDto> resolveBreach(
            @Parameter(description = "Breach ID") @PathVariable String breachId,
            @Parameter(description = "Resolution notes") @RequestParam String resolutionNotes) {
        return ResponseEntity.ok(slaManagementService.resolveBreach(breachId, resolutionNotes));
    }

    @GetMapping("/policies/{policyId}/compliance")
    @Operation(summary = "Calculate compliance rate", description = "Calculate SLA compliance rate for a policy")
    public ResponseEntity<Double> calculateComplianceRate(
            @Parameter(description = "Policy ID") @PathVariable String policyId) {
        return ResponseEntity.ok(slaManagementService.calculateComplianceRate(policyId));
    }

    @GetMapping("/stats/breaches/count/{breachType}")
    @Operation(summary = "Count breaches by type", description = "Get count of breaches by type")
    public ResponseEntity<Long> getBreachCountByType(
            @Parameter(description = "Breach type") @PathVariable SLABreach.BreachType breachType) {
        return ResponseEntity.ok(slaManagementService.getBreachCountByType(breachType));
    }
}
