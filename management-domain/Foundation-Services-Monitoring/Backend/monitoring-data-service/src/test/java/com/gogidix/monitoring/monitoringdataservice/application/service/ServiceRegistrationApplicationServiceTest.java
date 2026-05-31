package com.gogidix.monitoring.monitoringdataservice.application.service;

import com.gogidix.monitoring.monitoringdataservice.application.dto.RegisterServiceRequestDto;
import com.gogidix.monitoring.monitoringdataservice.application.dto.ServiceRegistrationResponseDto;
import com.gogidix.monitoring.monitoringdataservice.application.mapper.ServiceRegistrationMapper;
import com.gogidix.monitoring.monitoringdataservice.domain.model.ServiceRegistration;
import com.gogidix.monitoring.monitoringdataservice.domain.port.out.ServiceRegistrationRepositoryPort;
import com.gogidix.monitoring.monitoringdataservice.shared.exception.ConflictException;
import com.gogidix.monitoring.monitoringdataservice.shared.exception.NotFoundException;
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
 * Unit tests for ServiceRegistrationApplicationService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ServiceRegistrationApplicationService Tests")
class ServiceRegistrationApplicationServiceTest {

    @Mock
    private ServiceRegistrationRepositoryPort repository;

    @Mock
    private ServiceRegistrationMapper mapper;

    private ServiceRegistrationApplicationService service;

    private ServiceRegistrationResponseDto responseDto;

    @BeforeEach
    void setUp() {
        service = new ServiceRegistrationApplicationService(repository, mapper);

        responseDto = ServiceRegistrationResponseDto.builder()
                .serviceId("service-123")
                .serviceName("order-service")
                .serviceType("AI_SERVICE")
                .status("REGISTERED")
                .build();
    }

    @Test
    @DisplayName("Should register service successfully")
    void shouldRegisterServiceSuccessfully() {
        // Given
        String tenantId = "tenant-1";
        RegisterServiceRequestDto request = RegisterServiceRequestDto.builder()
                .serviceName("order-service")
                .serviceType("ai_service")
                .category("ecommerce")
                .version("1.0.0")
                .description("Order processing service")
                .baseUrl("http://localhost:8080")
                .healthEndpoint("/actuator/health")
                .metricsEndpoint("/actuator/metrics")
                .collectionInterval(60)
                .enabled(true)
                .tags(Map.of("team", "orders"))
                .build();

        ServiceRegistration saved = ServiceRegistration.builder()
                .serviceId("service-123")
                .serviceName("order-service")
                .build();

        when(repository.existsByTenantAndServiceName(tenantId, "order-service")).thenReturn(false);
        when(repository.save(any(ServiceRegistration.class))).thenReturn(saved);
        when(mapper.toResponseDto(saved)).thenReturn(responseDto);

        // When
        ServiceRegistrationResponseDto result = service.registerService(tenantId, request);

        // Then
        assertNotNull(result);
        assertEquals("service-123", result.getServiceId());
        verify(repository).existsByTenantAndServiceName(tenantId, "order-service");
        verify(repository).save(any(ServiceRegistration.class));
    }

    @Test
    @DisplayName("Should throw ConflictException when service already registered")
    void shouldThrowConflictExceptionWhenServiceAlreadyRegistered() {
        // Given
        String tenantId = "tenant-1";
        RegisterServiceRequestDto request = RegisterServiceRequestDto.builder()
                .serviceName("order-service")
                .build();

        when(repository.existsByTenantAndServiceName(tenantId, "order-service")).thenReturn(true);

        // When & Then
        assertThrows(ConflictException.class, () -> service.registerService(tenantId, request));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should use default values when optional fields not provided")
    void shouldUseDefaultValuesWhenOptionalFieldsNotProvided() {
        // Given
        String tenantId = "tenant-1";
        RegisterServiceRequestDto request = RegisterServiceRequestDto.builder()
                .serviceName("order-service")
                .build();

        ServiceRegistration saved = ServiceRegistration.builder()
                .serviceId("service-123")
                .serviceName("order-service")
                .build();

        when(repository.existsByTenantAndServiceName(tenantId, "order-service")).thenReturn(false);
        when(repository.save(any(ServiceRegistration.class))).thenReturn(saved);
        when(mapper.toResponseDto(saved)).thenReturn(responseDto);

        // When
        service.registerService(tenantId, request);

        // Then
        verify(repository).save(argThat(reg ->
                "/actuator/health".equals(reg.getHealthEndpoint()) &&
                        "/actuator/metrics".equals(reg.getMetricsEndpoint()) &&
                        60 == reg.getCollectionInterval()));
    }

    @Test
    @DisplayName("Should get service by ID successfully")
    void shouldGetServiceByIdSuccessfully() {
        // Given
        String tenantId = "tenant-1";
        String serviceId = "service-123";

        ServiceRegistration registration = ServiceRegistration.builder()
                .serviceId(serviceId)
                .tenantId(tenantId)
                .serviceName("order-service")
                .build();

        when(repository.findById(serviceId)).thenReturn(Optional.of(registration));
        when(mapper.toResponseDto(registration)).thenReturn(responseDto);

        // When
        ServiceRegistrationResponseDto result = service.getService(serviceId, tenantId);

        // Then
        assertNotNull(result);
        assertEquals("service-123", result.getServiceId());
        verify(repository).findById(serviceId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when service not found")
    void shouldThrowNotFoundExceptionWhenServiceNotFound() {
        // Given
        String tenantId = "tenant-1";
        String serviceId = "non-existent";

        when(repository.findById(serviceId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> service.getService(serviceId, tenantId));
    }

    @Test
    @DisplayName("Should throw NotFoundException when service belongs to different tenant")
    void shouldThrowNotFoundExceptionWhenServiceBelongsToDifferentTenant() {
        // Given
        String tenantId = "tenant-1";
        String serviceId = "service-123";

        ServiceRegistration registration = ServiceRegistration.builder()
                .serviceId(serviceId)
                .tenantId("tenant-2")
                .build();

        when(repository.findById(serviceId)).thenReturn(Optional.of(registration));

        // When & Then
        assertThrows(NotFoundException.class, () -> service.getService(serviceId, tenantId));
    }

    @Test
    @DisplayName("Should get services by tenant")
    void shouldGetServicesByTenant() {
        // Given
        String tenantId = "tenant-1";
        List<ServiceRegistration> registrations = List.of(
                ServiceRegistration.builder().serviceId("service-1").serviceName("order-service").build(),
                ServiceRegistration.builder().serviceId("service-2").serviceName("payment-service").build()
        );

        when(repository.findByTenantId(tenantId)).thenReturn(registrations);
        when(mapper.toResponseDto(any())).thenReturn(responseDto);

        // When
        List<ServiceRegistrationResponseDto> result = service.getServicesByTenant(tenantId);

        // Then
        assertEquals(2, result.size());
        verify(repository).findByTenantId(tenantId);
    }

    @Test
    @DisplayName("Should get active services by tenant")
    void shouldGetActiveServicesByTenant() {
        // Given
        String tenantId = "tenant-1";
        List<ServiceRegistration> registrations = List.of(
                ServiceRegistration.builder().serviceId("service-1").serviceName("order-service").build()
        );

        when(repository.findActiveByTenantId(tenantId)).thenReturn(registrations);
        when(mapper.toResponseDto(any())).thenReturn(responseDto);

        // When
        List<ServiceRegistrationResponseDto> result = service.getActiveServicesByTenant(tenantId);

        // Then
        assertEquals(1, result.size());
        verify(repository).findActiveByTenantId(tenantId);
    }

    @Test
    @DisplayName("Should update heartbeat successfully")
    void shouldUpdateHeartbeatSuccessfully() {
        // Given
        String tenantId = "tenant-1";
        String serviceId = "service-123";

        ServiceRegistration registration = ServiceRegistration.builder()
                .serviceId(serviceId)
                .tenantId(tenantId)
                .build();

        when(repository.findById(serviceId)).thenReturn(Optional.of(registration));
        doNothing().when(repository).updateHeartbeat(serviceId);

        // When
        service.updateHeartbeat(serviceId, tenantId);

        // Then
        verify(repository).updateHeartbeat(serviceId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when updating heartbeat for non-existent service")
    void shouldThrowNotFoundExceptionWhenUpdatingHeartbeatForNonExistentService() {
        // Given
        String tenantId = "tenant-1";
        String serviceId = "non-existent";

        when(repository.findById(serviceId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> service.updateHeartbeat(serviceId, tenantId));
        verify(repository, never()).updateHeartbeat(any());
    }

    @Test
    @DisplayName("Should unregister service successfully")
    void shouldUnregisterServiceSuccessfully() {
        // Given
        String tenantId = "tenant-1";
        String serviceId = "service-123";

        ServiceRegistration registration = ServiceRegistration.builder()
                .serviceId(serviceId)
                .tenantId(tenantId)
                .build();

        when(repository.findById(serviceId)).thenReturn(Optional.of(registration));
        doNothing().when(repository).deleteById(serviceId);

        // When
        service.unregisterService(serviceId, tenantId);

        // Then
        verify(repository).deleteById(serviceId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when unregistering non-existent service")
    void shouldThrowNotFoundExceptionWhenUnregisteringNonExistentService() {
        // Given
        String tenantId = "tenant-1";
        String serviceId = "non-existent";

        when(repository.findById(serviceId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> service.unregisterService(serviceId, tenantId));
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should parse service type correctly")
    void shouldParseServiceTypeCorrectly() {
        // Given
        String tenantId = "tenant-1";
        RegisterServiceRequestDto request = RegisterServiceRequestDto.builder()
                .serviceName("order-service")
                .serviceType("AI_SERVICE")
                .build();

        ServiceRegistration saved = ServiceRegistration.builder()
                .serviceId("service-123")
                .serviceName("order-service")
                .serviceType(com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType.AI_SERVICE)
                .build();

        when(repository.existsByTenantAndServiceName(tenantId, "order-service")).thenReturn(false);
        when(repository.save(any(ServiceRegistration.class))).thenReturn(saved);
        when(mapper.toResponseDto(saved)).thenReturn(responseDto);

        // When
        service.registerService(tenantId, request);

        // Then
        verify(repository).save(argThat(reg ->
                reg.getServiceType() == com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType.AI_SERVICE));
    }

    @Test
    @DisplayName("Should handle invalid service type gracefully")
    void shouldHandleInvalidServiceTypeGracefully() {
        // Given
        String tenantId = "tenant-1";
        RegisterServiceRequestDto request = RegisterServiceRequestDto.builder()
                .serviceName("order-service")
                .serviceType("invalid_type")
                .build();

        ServiceRegistration saved = ServiceRegistration.builder()
                .serviceId("service-123")
                .serviceName("order-service")
                .build();

        when(repository.existsByTenantAndServiceName(tenantId, "order-service")).thenReturn(false);
        when(repository.save(any(ServiceRegistration.class))).thenReturn(saved);
        when(mapper.toResponseDto(saved)).thenReturn(responseDto);

        // When
        service.registerService(tenantId, request);

        // Then
        verify(repository).save(argThat(reg -> reg.getServiceType() == null));
    }
}
