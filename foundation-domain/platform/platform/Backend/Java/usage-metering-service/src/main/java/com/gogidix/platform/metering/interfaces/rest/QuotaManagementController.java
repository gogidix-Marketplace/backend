package com.gogidix.platform.metering.interfaces.rest;

import com.gogidix.platform.metering.application.dto.QuotaDefinitionDto;
import com.gogidix.platform.metering.application.dto.QuotaAlertDto;
import com.gogidix.platform.metering.application.service.QuotaManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for quota management operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/quotas")
@RequiredArgsConstructor
@Tag(name = "Quota Management", description = "Quota definition and monitoring APIs")
public class QuotaManagementController {

    private final QuotaManagementService quotaManagementService;

    @PostMapping
    @Operation(summary = "Create quota definition", description = "Create a new quota definition")
    public ResponseEntity<QuotaDefinitionDto> createQuota(@Valid @RequestBody QuotaDefinitionDto dto) {
        log.info("Creating quota definition: name={}", dto.getQuotaName());
        QuotaDefinitionDto result = quotaManagementService.createQuotaDefinition(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    @Operation(summary = "Get all quotas", description = "Get all quota definitions for the current tenant")
    public ResponseEntity<List<QuotaDefinitionDto>> getQuotas() {
        return ResponseEntity.ok(quotaManagementService.getQuotasForTenant());
    }

    @GetMapping("/{quotaId}")
    @Operation(summary = "Get quota by ID", description = "Get a specific quota definition by ID")
    public ResponseEntity<QuotaDefinitionDto> getQuota(@PathVariable String quotaId) {
        return ResponseEntity.ok(quotaManagementService.getQuotaDefinition(quotaId));
    }

    @GetMapping("/alerts")
    @Operation(summary = "Get active alerts", description = "Get all active quota alerts")
    public ResponseEntity<List<QuotaAlertDto>> getAlerts() {
        return ResponseEntity.ok(quotaManagementService.getActiveAlerts());
    }

    @PostMapping("/alerts/{alertId}/acknowledge")
    @Operation(summary = "Acknowledge alert", description = "Acknowledge a quota alert")
    public ResponseEntity<Void> acknowledgeAlert(@PathVariable String alertId) {
        quotaManagementService.acknowledgeAlert(alertId);
        return ResponseEntity.noContent().build();
    }
}
