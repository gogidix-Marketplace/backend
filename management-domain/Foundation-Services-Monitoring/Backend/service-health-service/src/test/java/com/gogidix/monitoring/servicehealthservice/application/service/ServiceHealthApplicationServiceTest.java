package com.gogidix.monitoring.servicehealthservice.application.service;

import com.gogidix.monitoring.servicehealthservice.application.dto.ServiceHealthStatusResponseDto;
import com.gogidix.monitoring.servicehealthservice.application.dto.ServiceHealthSummaryDto;
import com.gogidix.monitoring.servicehealthservice.application.dto.ServiceDependencyResponseDto;
import com.gogidix.monitoring.servicehealthservice.application.mapper.ServiceHealthMapper;
import com.gogidix.monitoring.servicehealthservice.domain.model.ServiceDependency;
import com.gogidix.monitoring.servicehealthservice.domain.model.ServiceHealthStatus;
import com.gogidix.monitoring.servicehealthservice.domain.port.out.ServiceDependencyRepositoryPort;
import com.gogidix.monitoring.servicehealthservice.domain.port.out.ServiceHealthStatusRepositoryPort;
import com.gogidix.monitoring.servicehealthservice.domain.port.out.ServiceUptimeRepositoryPort;
import com.gogidix.monitoring.servicehealthservice.shared.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ServiceHealthApplicationService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ServiceHealthApplicationService Tests")
class ServiceHealthApplicationServiceTest {

    @Mock
    private ServiceHealthStatusRepositoryPort healthStatusRepository;

    @Mock
    private ServiceUptimeRepositoryPort uptimeRepository;

    @Mock
    private ServiceDependencyRepositoryPort dependencyRepository;

    @Mock
    private ServiceHealthMapper mapper;

    private ServiceHealthApplicationService service;

    private ServiceHealthStatusResponseDto responseDto;
    private ServiceHealthStatus healthStatus;

    @BeforeEach
    void setUp() {
        service = new ServiceHealthApplicationService(
                healthStatusRepository,
                uptimeRepository,
                dependencyRepository,
                mapper
        );

        responseDto = ServiceHealthStatusResponseDto.builder()
                .id("health-123")
                .serviceName("order-service")
                .status("HEALTHY")
                .healthScore(95)
                .build();

        healthStatus = ServiceHealthStatus.builder()
                .id("health-123")
                .tenantId("tenant-1")
                .serviceName("order-service")
                .status(ServiceHealthStatus.HealthStatus.HEALTHY)
                .healthScore(95)
                .lastCheckAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Should get service health successfully")
    void shouldGetServiceHealthSuccessfully() {
        // Given
        String tenantId = "tenant-1";
        String serviceName = "order-service";

        when(healthStatusRepository.findByTenantAndService(tenantId, serviceName))
                .thenReturn(Optional.of(healthStatus));
        when(mapper.toResponseDto(healthStatus)).thenReturn(responseDto);

        // When
        ServiceHealthStatusResponseDto result = service.getServiceHealth(tenantId, serviceName);

        // Then
        assertNotNull(result);
        assertEquals("health-123", result.getId());
        assertEquals("order-service", result.getServiceName());
        verify(healthStatusRepository).findByTenantAndService(tenantId, serviceName);
    }

    @Test
    @DisplayName("Should throw NotFoundException when service health not found")
    void shouldThrowNotFoundExceptionWhenServiceHealthNotFound() {
        // Given
        String tenantId = "tenant-1";
        String serviceName = "non-existent";

        when(healthStatusRepository.findByTenantAndService(tenantId, serviceName))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> service.getServiceHealth(tenantId, serviceName));
    }

    @Test
    @DisplayName("Should get all service health statuses")
    void shouldGetAllServiceHealthStatuses() {
        // Given
        String tenantId = "tenant-1";
        List<ServiceHealthStatus> statuses = List.of(
                ServiceHealthStatus.builder().id("health-1").serviceName("order-service").build(),
                ServiceHealthStatus.builder().id("health-2").serviceName("payment-service").build()
        );

        when(healthStatusRepository.findByTenantId(tenantId)).thenReturn(statuses);
        when(mapper.toResponseDto(any())).thenReturn(responseDto);

        // When
        List<ServiceHealthStatusResponseDto> result = service.getAllServiceHealth(tenantId);

        // Then
        assertEquals(2, result.size());
        verify(healthStatusRepository).findByTenantId(tenantId);
    }

    @Test
    @DisplayName("Should get health summary correctly")
    void shouldGetHealthSummaryCorrectly() {
        // Given
        String tenantId = "tenant-1";
        List<ServiceHealthStatus> statuses = List.of(
                ServiceHealthStatus.builder()
                        .serviceName("service-1")
                        .status(ServiceHealthStatus.HealthStatus.HEALTHY)
                        .healthScore(95)
                        .serviceType(com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType.AI_SERVICE)
                        .build(),
                ServiceHealthStatus.builder()
                        .serviceName("service-2")
                        .status(ServiceHealthStatus.HealthStatus.DEGRADED)
                        .healthScore(75)
                        .serviceType(com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType.ORCHESTRATION_SERVICE)
                        .build(),
                ServiceHealthStatus.builder()
                        .serviceName("service-3")
                        .status(ServiceHealthStatus.HealthStatus.UNHEALTHY)
                        .healthScore(60)
                        .serviceType(com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType.AI_SERVICE)
                        .build(),
                ServiceHealthStatus.builder()
                        .serviceName("service-4")
                        .status(ServiceHealthStatus.HealthStatus.DOWN)
                        .healthScore(30)
                        .serviceType(com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType.ORCHESTRATION_SERVICE)
                        .build(),
                ServiceHealthStatus.builder()
                        .serviceName("service-5")
                        .status(ServiceHealthStatus.HealthStatus.UNKNOWN)
                        .healthScore(null)
                        .serviceType(com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType.AI_SERVICE)
                        .build()
        );

        when(healthStatusRepository.findByTenantId(tenantId)).thenReturn(statuses);

        // When
        ServiceHealthSummaryDto result = service.getHealthSummary(tenantId);

        // Then
        assertEquals(5, result.getTotalServices());
        assertEquals(1, result.getHealthyServices());
        assertEquals(1, result.getDegradedServices());
        assertEquals(1, result.getUnhealthyServices());
        assertEquals(1, result.getDownServices());
        assertEquals(1, result.getUnknownServices());
        assertEquals(65.0, result.getAverageHealthScore(), 0.1);
        assertNotNull(result.getStatusBreakdown());
        assertNotNull(result.getServiceTypeBreakdown());
    }

    @Test
    @DisplayName("Should get services by health status")
    void shouldGetServicesByHealthStatus() {
        // Given
        String tenantId = "tenant-1";
        ServiceHealthStatus.HealthStatus status = ServiceHealthStatus.HealthStatus.HEALTHY;
        List<ServiceHealthStatus> statuses = List.of(
                ServiceHealthStatus.builder()
                        .id("health-1")
                        .serviceName("order-service")
                        .status(ServiceHealthStatus.HealthStatus.HEALTHY)
                        .build()
        );

        when(healthStatusRepository.findByTenantIdAndStatus(tenantId, status))
                .thenReturn(statuses);
        when(mapper.toResponseDto(any())).thenReturn(responseDto);

        // When
        List<ServiceHealthStatusResponseDto> result = service.getServicesByStatus(tenantId, status);

        // Then
        assertEquals(1, result.size());
        verify(healthStatusRepository).findByTenantIdAndStatus(tenantId, status);
    }

    @Test
    @DisplayName("Should get services by type")
    void shouldGetServicesByType() {
        // Given
        String tenantId = "tenant-1";
        com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType serviceType =
                com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType.AI_SERVICE;
        List<ServiceHealthStatus> statuses = List.of(
                ServiceHealthStatus.builder()
                        .id("health-1")
                        .serviceName("order-service")
                        .serviceType(serviceType)
                        .build()
        );

        when(healthStatusRepository.findByTenantIdAndServiceType(tenantId, serviceType))
                .thenReturn(statuses);
        when(mapper.toResponseDto(any())).thenReturn(responseDto);

        // When
        List<ServiceHealthStatusResponseDto> result = service.getServicesByType(tenantId, serviceType);

        // Then
        assertEquals(1, result.size());
        verify(healthStatusRepository).findByTenantIdAndServiceType(tenantId, serviceType);
    }

    @Test
    @DisplayName("Should update service health status successfully")
    void shouldUpdateServiceHealthStatusSuccessfully() {
        // Given
        String tenantId = "tenant-1";
        String serviceName = "order-service";
        ServiceHealthStatus.HealthStatus newStatus = ServiceHealthStatus.HealthStatus.HEALTHY;
        Integer healthScore = 95;

        when(healthStatusRepository.findByTenantAndService(tenantId, serviceName))
                .thenReturn(Optional.of(healthStatus));
        doNothing().when(healthStatusRepository).updateStatus(any(), any(), any());

        // When
        service.updateServiceHealth(tenantId, serviceName, newStatus, healthScore);

        // Then
        verify(healthStatusRepository).updateStatus(healthStatus.getId(), newStatus, healthScore);
    }

    @Test
    @DisplayName("Should create new health status when not exists")
    void shouldCreateNewHealthStatusWhenNotExists() {
        // Given
        String tenantId = "tenant-1";
        String serviceName = "order-service";
        ServiceHealthStatus.HealthStatus newStatus = ServiceHealthStatus.HealthStatus.HEALTHY;
        Integer healthScore = 95;

        when(healthStatusRepository.findByTenantAndService(tenantId, serviceName))
                .thenReturn(Optional.empty());
        when(healthStatusRepository.save(any(ServiceHealthStatus.class)))
                .thenReturn(healthStatus);

        // When
        service.updateServiceHealth(tenantId, serviceName, newStatus, healthScore);

        // Then
        verify(healthStatusRepository).save(argThat(status ->
                status.getServiceName().equals(serviceName) &&
                        status.getStatus() == newStatus &&
                        status.getHealthScore() == healthScore));
    }

    @Test
    @DisplayName("Should get service dependencies")
    void shouldGetServiceDependencies() {
        // Given
        String tenantId = "tenant-1";
        String serviceName = "order-service";
        List<ServiceDependency> dependencies = List.of(
                ServiceDependency.builder()
                        .id("dep-1")
                        .serviceName(serviceName)
                        .dependsOnService("database-service")
                        .dependencyType(ServiceDependency.DependencyType.DATABASE)
                        .build()
        );

        ServiceDependencyResponseDto depResponseDto = ServiceDependencyResponseDto.builder()
                .id("dep-1")
                .serviceName(serviceName)
                .dependsOnService("database-service")
                .build();

        when(dependencyRepository.findByTenantAndService(tenantId, serviceName))
                .thenReturn(dependencies);
        when(mapper.toDependencyResponseDto(any())).thenReturn(depResponseDto);

        // When
        List<ServiceDependencyResponseDto> result = service.getServiceDependencies(tenantId, serviceName);

        // Then
        assertEquals(1, result.size());
        verify(dependencyRepository).findByTenantAndService(tenantId, serviceName);
    }

    @Test
    @DisplayName("Should get service dependents")
    void shouldGetServiceDependents() {
        // Given
        String tenantId = "tenant-1";
        String serviceName = "database-service";
        List<ServiceDependency> dependencies = List.of(
                ServiceDependency.builder()
                        .id("dep-1")
                        .serviceName("order-service")
                        .dependsOnService(serviceName)
                        .build()
        );

        ServiceDependencyResponseDto depResponseDto = ServiceDependencyResponseDto.builder()
                .id("dep-1")
                .serviceName("order-service")
                .dependsOnService(serviceName)
                .build();

        when(dependencyRepository.findDependents(tenantId, serviceName))
                .thenReturn(dependencies);
        when(mapper.toDependencyResponseDto(any())).thenReturn(depResponseDto);

        // When
        List<ServiceDependencyResponseDto> result = service.getServiceDependents(tenantId, serviceName);

        // Then
        assertEquals(1, result.size());
        verify(dependencyRepository).findDependents(tenantId, serviceName);
    }

    @Test
    @DisplayName("Should calculate average health score correctly")
    void shouldCalculateAverageHealthScoreCorrectly() {
        // Given
        String tenantId = "tenant-1";
        List<ServiceHealthStatus> statuses = List.of(
                ServiceHealthStatus.builder().healthScore(100).build(),
                ServiceHealthStatus.builder().healthScore(80).build(),
                ServiceHealthStatus.builder().healthScore(60).build(),
                ServiceHealthStatus.builder().healthScore(null).build()
        );

        when(healthStatusRepository.findByTenantId(tenantId)).thenReturn(statuses);

        // When
        ServiceHealthSummaryDto result = service.getHealthSummary(tenantId);

        // Then
        assertEquals(80.0, result.getAverageHealthScore(), 0.01);
    }

    @Test
    @DisplayName("Should handle empty list for health summary")
    void shouldHandleEmptyListForHealthSummary() {
        // Given
        String tenantId = "tenant-1";

        when(healthStatusRepository.findByTenantId(tenantId)).thenReturn(List.of());

        // When
        ServiceHealthSummaryDto result = service.getHealthSummary(tenantId);

        // Then
        assertEquals(0, result.getTotalServices());
        assertEquals(0.0, result.getAverageHealthScore(), 0.01);
    }
}
