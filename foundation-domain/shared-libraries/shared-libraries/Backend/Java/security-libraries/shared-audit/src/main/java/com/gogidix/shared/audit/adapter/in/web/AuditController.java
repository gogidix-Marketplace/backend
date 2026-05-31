package com.gogidix.shared.audit.adapter.in.web;

import com.gogidix.shared.audit.application.port.in.AuditEventUseCase;
import com.gogidix.shared.audit.api.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@Validated
public class AuditController {

    private final AuditEventUseCase auditEventUseCase;

    @PostMapping("/events")
    public ResponseEntity<AuditEventDTO> createAuditEvent(@Valid @RequestBody CreateAuditEventDTO request) {
        AuditEventDTO auditEvent = auditEventUseCase.createAuditEvent(request);
        return ResponseEntity.ok(auditEvent);
    }

    @GetMapping("/events")
    public ResponseEntity<List<AuditEventDTO>> searchAuditEvents(@Valid AuditSearchDTO searchCriteria) {
        List<AuditEventDTO> auditEvents = auditEventUseCase.searchAuditEvents(searchCriteria);
        return ResponseEntity.ok(auditEvents);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<AuditEventDTO> getAuditEvent(@PathVariable String eventId) {
        AuditEventDTO auditEvent = auditEventUseCase.getAuditEvent(eventId);
        return ResponseEntity.ok(auditEvent);
    }

    @GetMapping("/statistics")
    public ResponseEntity<AuditStatisticsDTO> getAuditStatistics() {
        AuditStatisticsDTO statistics = auditEventUseCase.getAuditStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/compliance/report")
    public ResponseEntity<ComplianceReportDTO> generateComplianceReport() {
        ComplianceReportDTO report = auditEventUseCase.generateComplianceReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/health")
    public ResponseEntity<HealthCheckDTO> healthCheck() {
        HealthCheckDTO health = auditEventUseCase.performHealthCheck();
        return ResponseEntity.ok(health);
    }
}