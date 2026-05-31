package com.gogidix.monitoring.servicehealthservice.interfaces.rest;

import com.gogidix.monitoring.servicehealthservice.application.dto.ServiceHealthStatusResponseDto;
import com.gogidix.monitoring.servicehealthservice.application.dto.ServiceHealthSummaryDto;
import com.gogidix.monitoring.servicehealthservice.application.service.ServiceHealthApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for service health operations.
 */
@RestController
@RequestMapping("/health")
@Tag(name = "Service Health", description = "APIs for service health monitoring")
public class ServiceHealthController {

    private final ServiceHealthApplicationService serviceHealthApplicationService;

    public ServiceHealthController(ServiceHealthApplicationService serviceHealthApplicationService) {
        this.serviceHealthApplicationService = serviceHealthApplicationService;
    }

    @GetMapping("/summary")
    @Operation(summary = "Get health summary", description = "Returns overall health summary for all services")
    public ServiceHealthSummaryDto getHealthSummary(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId
    ) {
        return serviceHealthApplicationService.getHealthSummary(tenantId);
    }

    @GetMapping("/services/{serviceName}")
    @Operation(summary = "Get service health", description = "Returns health status for a specific service")
    public ServiceHealthStatusResponseDto getServiceHealth(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Parameter(description = "Service name", required = true)
            @PathVariable @NotBlank String serviceName
    ) {
        return serviceHealthApplicationService.getServiceHealth(tenantId, serviceName);
    }

    @GetMapping("/services")
    @Operation(summary = "List all service health", description = "Returns health status for all services")
    public List<ServiceHealthStatusResponseDto> getAllServiceHealth(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Parameter(description = "Filter by status")
            @RequestParam(required = false) String status,

            @Parameter(description = "Filter by service type")
            @RequestParam(required = false) String serviceType
    ) {
        if (status != null && !status.isBlank()) {
            return serviceHealthApplicationService.getServicesByStatus(
                    tenantId,
                    com.gogidix.monitoring.servicehealthservice.domain.model.ServiceHealthStatus.HealthStatus.valueOf(status.toUpperCase())
            );
        }

        if (serviceType != null && !serviceType.isBlank()) {
            return serviceHealthApplicationService.getServicesByType(
                    tenantId,
                    com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType.valueOf(serviceType.toUpperCase())
            );
        }

        return serviceHealthApplicationService.getAllServiceHealth(tenantId);
    }

    @GetMapping("/ai-services")
    @Operation(summary = "Get AI services health", description = "Returns health status for all AI services")
    public List<ServiceHealthStatusResponseDto> getAIServicesHealth(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId
    ) {
        return serviceHealthApplicationService.getServicesByType(
                tenantId,
                com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType.AI_SERVICE
        );
    }

    @GetMapping("/orchestration-services")
    @Operation(summary = "Get Orchestration services health", description = "Returns health status for all Orchestration services")
    public List<ServiceHealthStatusResponseDto> getOrchestrationServicesHealth(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId
    ) {
        return serviceHealthApplicationService.getServicesByType(
                tenantId,
                com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType.ORCHESTRATION_SERVICE
        );
    }
}
