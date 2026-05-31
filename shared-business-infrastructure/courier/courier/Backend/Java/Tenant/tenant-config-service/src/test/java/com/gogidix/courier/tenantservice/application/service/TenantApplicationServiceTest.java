package com.gogidix.courier.tenantservice.application.service;

import com.gogidix.courier.tenantservice.application.dto.TenantConfigRequest;
import com.gogidix.courier.tenantservice.application.dto.TenantRequest;
import com.gogidix.courier.tenantservice.application.dto.TenantResponse;
import com.gogidix.courier.tenantservice.application.mapper.TenantMapper;
import com.gogidix.courier.tenantservice.domain.entity.Tenant;
import com.gogidix.courier.tenantservice.domain.repository.TenantRepository;
import com.gogidix.courier.tenantservice.shared.exception.NotFoundException;
import com.gogidix.courier.tenantservice.shared.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TenantApplicationService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TenantApplicationService Tests")
class TenantApplicationServiceTest {

    @Mock
    private TenantRepository repository;

    @Mock
    private TenantMapper mapper;

    @Mock
    private TenantEventPublisher eventPublisher;

    private TenantApplicationService service;

    @BeforeEach
    void setUp() {
        service = new TenantApplicationService(repository, mapper, eventPublisher);
    }

    @Test
    @DisplayName("Should create tenant successfully")
    void shouldCreateTenantSuccessfully() {
        // Given
        TenantRequest request = new TenantRequest("tenant-001", "Test Tenant", "Description");
        Tenant tenant = new Tenant("tenant-001", "Test Tenant", "Description");
        TenantResponse response = new TenantResponse(
                tenant.getId(),
                tenant.getTenantId(),
                tenant.getName(),
                tenant.getDescription(),
                tenant.getStatus(),
                null,
                tenant.getCreatedAt(),
                tenant.getUpdatedAt(),
                null,
                null
        );

        when(repository.existsByTenantId("tenant-001")).thenReturn(false);
        when(repository.existsByName("Test Tenant")).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(tenant);
        when(repository.save(any(Tenant.class))).thenReturn(tenant);
        when(mapper.toResponseDto(any(Tenant.class))).thenReturn(response);

        // When
        TenantResponse result = service.createTenant(request, "user-001");

        // Then
        assertNotNull(result);
        assertEquals("tenant-001", result.tenantId());
        verify(repository).save(any(Tenant.class));
        verify(eventPublisher).publish(any());
    }

    @Test
    @DisplayName("Should throw when creating duplicate tenant")
    void shouldThrowWhenCreatingDuplicateTenant() {
        // Given
        TenantRequest request = new TenantRequest("tenant-001", "Test Tenant", "Description");
        when(repository.existsByTenantId("tenant-001")).thenReturn(true);

        // When & Then
        assertThrows(ValidationException.class, () -> service.createTenant(request, "user-001"));
        verify(repository, never()).save(any(Tenant.class));
    }

    @Test
    @DisplayName("Should get tenant by id")
    void shouldGetTenantById() {
        // Given
        String id = "tenant-id";
        Tenant tenant = new Tenant("tenant-001", "Test Tenant", "Description");
        TenantResponse response = new TenantResponse(
                id, "tenant-001", "Test Tenant", "Description",
                Tenant.TenantStatus.PENDING, null, tenant.getCreatedAt(),
                tenant.getUpdatedAt(), null, null
        );

        when(repository.findById(id)).thenReturn(Optional.of(tenant));
        when(mapper.toResponseDto(tenant)).thenReturn(response);

        // When
        TenantResponse result = service.getTenantById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.id());
    }

    @Test
    @DisplayName("Should throw when tenant not found")
    void shouldThrowWhenTenantNotFound() {
        // Given
        String id = "non-existent";
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> service.getTenantById(id));
    }

    @Test
    @DisplayName("Should update tenant successfully")
    void shouldUpdateTenantSuccessfully() {
        // Given
        String id = "tenant-id";
        TenantRequest request = new TenantRequest("tenant-001", "Updated Name", "Description");
        Tenant tenant = new Tenant("tenant-001", "Old Name", "Description");
        TenantResponse response = new TenantResponse(
                id, "tenant-001", "Updated Name", "Description",
                Tenant.TenantStatus.PENDING, null, tenant.getCreatedAt(),
                tenant.getUpdatedAt(), null, null
        );

        when(repository.findById(id)).thenReturn(Optional.of(tenant));
        when(repository.existsByName("Updated Name")).thenReturn(false);
        when(repository.save(any(Tenant.class))).thenReturn(tenant);
        when(mapper.toResponseDto(any(Tenant.class))).thenReturn(response);

        // When
        TenantResponse result = service.updateTenant(id, request, "user-001");

        // Then
        assertNotNull(result);
        assertEquals("Updated Name", result.name());
        verify(repository).save(any(Tenant.class));
        verify(eventPublisher).publish(any());
    }

    @Test
    @DisplayName("Should activate tenant successfully")
    void shouldActivateTenantSuccessfully() {
        // Given
        String id = "tenant-id";
        Tenant tenant = new Tenant("tenant-001", "Test Tenant", "Description");
        TenantResponse response = new TenantResponse(
                id, "tenant-001", "Test Tenant", "Description",
                Tenant.TenantStatus.ACTIVE, null, tenant.getCreatedAt(),
                tenant.getUpdatedAt(), tenant.getActivatedAt(), null
        );

        when(repository.findById(id)).thenReturn(Optional.of(tenant));
        when(repository.save(any(Tenant.class))).thenReturn(tenant);
        when(mapper.toResponseDto(any(Tenant.class))).thenReturn(response);

        // When
        TenantResponse result = service.activateTenant(id);

        // Then
        assertNotNull(result);
        assertEquals(Tenant.TenantStatus.ACTIVE, result.status());
        verify(repository).save(any(Tenant.class));
    }

    @Test
    @DisplayName("Should delete tenant successfully")
    void shouldDeleteTenantSuccessfully() {
        // Given
        String id = "tenant-id";
        Tenant tenant = new Tenant("tenant-001", "Test Tenant", "Description");
        when(repository.findById(id)).thenReturn(Optional.of(tenant));
        when(repository.save(any(Tenant.class))).thenReturn(tenant);

        // When
        service.deleteTenant(id);

        // Then
        verify(repository).save(any(Tenant.class));
        assertEquals(Tenant.TenantStatus.TERMINATED, tenant.getStatus());
    }
}
