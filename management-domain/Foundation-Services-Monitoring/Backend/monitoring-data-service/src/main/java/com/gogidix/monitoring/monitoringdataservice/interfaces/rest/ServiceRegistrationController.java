package com.gogidix.monitoring.monitoringdataservice.interfaces.rest;

import com.gogidix.monitoring.monitoringdataservice.application.dto.RegisterServiceRequestDto;
import com.gogidix.monitoring.monitoringdataservice.application.dto.ServiceRegistrationResponseDto;
import com.gogidix.monitoring.monitoringdataservice.application.service.ServiceRegistrationApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * REST controller for service registration.
 */
@RestController
@RequestMapping("/services")
@Tag(name = "Service Registration", description = "APIs for managing service registrations")
public class ServiceRegistrationController {

    private final ServiceRegistrationApplicationService serviceRegistrationService;

    public ServiceRegistrationController(ServiceRegistrationApplicationService serviceRegistrationService) {
        this.serviceRegistrationService = serviceRegistrationService;
    }

    @PostMapping
    @Operation(summary = "Register a service", description = "Registers a new service for monitoring")
    @ApiResponse(
            responseCode = "201",
            description = "Service registered successfully",
            content = @Content(schema = @Schema(implementation = ServiceRegistrationResponseDto.class))
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ServiceRegistrationResponseDto> registerService(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Valid @RequestBody RegisterServiceRequestDto request
    ) {
        ServiceRegistrationResponseDto response = serviceRegistrationService.registerService(tenantId, request);

        return ResponseEntity
                .created(URI.create("/services/" + response.getServiceId()))
                .body(response);
    }

    @GetMapping("/{serviceId}")
    @Operation(summary = "Get a service registration", description = "Retrieves a service registration by ID")
    @ApiResponse(responseCode = "200", description = "Service registration retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Service not found")
    public ServiceRegistrationResponseDto getService(
            @Parameter(description = "Service ID", required = true)
            @PathVariable @NotBlank String serviceId,

            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId
    ) {
        return serviceRegistrationService.getService(serviceId, tenantId);
    }

    @GetMapping
    @Operation(summary = "List all services", description = "Retrieves all service registrations for a tenant")
    @ApiResponse(responseCode = "200", description = "Service registrations retrieved successfully")
    public List<ServiceRegistrationResponseDto> getServices(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Parameter(description = "Filter by active status")
            @RequestParam(required = false) Boolean activeOnly
    ) {
        if (Boolean.TRUE.equals(activeOnly)) {
            return serviceRegistrationService.getActiveServicesByTenant(tenantId);
        }
        return serviceRegistrationService.getServicesByTenant(tenantId);
    }

    @PutMapping("/{serviceId}/heartbeat")
    @Operation(summary = "Update service heartbeat", description = "Updates the heartbeat timestamp for a service")
    @ApiResponse(responseCode = "204", description = "Heartbeat updated successfully")
    @ApiResponse(responseCode = "404", description = "Service not found")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateHeartbeat(
            @Parameter(description = "Service ID", required = true)
            @PathVariable @NotBlank String serviceId,

            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId
    ) {
        serviceRegistrationService.updateHeartbeat(serviceId, tenantId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{serviceId}")
    @Operation(summary = "Unregister a service", description = "Unregisters a service from monitoring")
    @ApiResponse(responseCode = "204", description = "Service unregistered successfully")
    @ApiResponse(responseCode = "404", description = "Service not found")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> unregisterService(
            @Parameter(description = "Service ID", required = true)
            @PathVariable @NotBlank String serviceId,

            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId
    ) {
        serviceRegistrationService.unregisterService(serviceId, tenantId);
        return ResponseEntity.noContent().build();
    }
}
