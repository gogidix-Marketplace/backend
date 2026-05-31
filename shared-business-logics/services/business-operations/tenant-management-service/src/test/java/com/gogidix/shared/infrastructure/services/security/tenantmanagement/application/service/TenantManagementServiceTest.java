package com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.service;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request.CreateTenantRequestDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request.UpdateTenantRequestDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.response.TenantResponseDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.mapper.TenantMapper;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.exception.TenantNotFoundException;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.port.out.TenantRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TenantManagementService Tests")
class TenantManagementServiceTest {

    @Mock private TenantRepositoryPort tenantRepository;
    @Mock private TenantMapper tenantMapper;
    @InjectMocks private TenantManagementService service;

    private Tenant testTenant;
    private TenantResponseDto testDto;

    @BeforeEach
    void setUp() {
        testTenant = Tenant.builder()
            .id("1").tenantId("t1").name("Test").status(Tenant.TenantStatus.ACTIVE)
            .plan(Tenant.TenantPlan.FREE).maxUsers(10).maxStorageGB(5)
            .build();
        testDto = TenantResponseDto.builder().id("1").tenantId("t1").name("Test").build();
    }

    @Nested
    @DisplayName("Create Tenant")
    class CreateTests {

        @Test
        void shouldCreateTenant() {
            CreateTenantRequestDto request = CreateTenantRequestDto.builder()
                .tenantId("t1").name("Test").build();
            when(tenantRepository.existsByTenantId("t1")).thenReturn(false);
            when(tenantRepository.save(any())).thenReturn(testTenant);
            when(tenantMapper.toResponseDto(any())).thenReturn(testDto);

            TenantResponseDto result = service.createTenant(request);
            assertNotNull(result);
            assertEquals("t1", result.getTenantId());
        }

        @Test
        void shouldThrowWhenTenantIdExists() {
            CreateTenantRequestDto request = CreateTenantRequestDto.builder()
                .tenantId("t1").name("Test").build();
            when(tenantRepository.existsByTenantId("t1")).thenReturn(true);

            assertThrows(IllegalArgumentException.class, () -> service.createTenant(request));
        }

        @Test
        void shouldThrowWhenDomainExists() {
            CreateTenantRequestDto request = CreateTenantRequestDto.builder()
                .tenantId("t1").name("Test").domain("test.com").build();
            when(tenantRepository.existsByTenantId("t1")).thenReturn(false);
            when(tenantRepository.existsByDomain("test.com")).thenReturn(true);

            assertThrows(IllegalArgumentException.class, () -> service.createTenant(request));
        }

        @Test
        void shouldCreateWithDefaults() {
            CreateTenantRequestDto request = CreateTenantRequestDto.builder()
                .tenantId("t1").name("Test").build();
            when(tenantRepository.existsByTenantId("t1")).thenReturn(false);
            when(tenantRepository.save(any())).thenReturn(testTenant);
            when(tenantMapper.toResponseDto(any())).thenReturn(testDto);

            service.createTenant(request);
            verify(tenantRepository).save(argThat(t ->
                t.getStatus() == Tenant.TenantStatus.TRIAL &&
                t.getPlan() == Tenant.TenantPlan.FREE &&
                t.getMaxUsers() == 5 &&
                t.getMaxStorageGB() == 10
            ));
        }
    }

    @Nested
    @DisplayName("Update Tenant")
    class UpdateTests {

        @Test
        void shouldUpdateTenant() {
            UpdateTenantRequestDto request = UpdateTenantRequestDto.builder().name("New").build();
            when(tenantRepository.findByTenantId("t1")).thenReturn(Optional.of(testTenant));
            when(tenantRepository.save(any())).thenReturn(testTenant);
            when(tenantMapper.toResponseDto(any())).thenReturn(testDto);

            TenantResponseDto result = service.updateTenant("t1", request);
            assertNotNull(result);
        }

        @Test
        void shouldThrowWhenNotFound() {
            UpdateTenantRequestDto request = UpdateTenantRequestDto.builder().name("New").build();
            when(tenantRepository.findByTenantId("missing")).thenReturn(Optional.empty());

            assertThrows(TenantNotFoundException.class, () -> service.updateTenant("missing", request));
        }
    }

    @Nested
    @DisplayName("Get Tenant")
    class GetTests {

        @Test
        void shouldGetTenant() {
            when(tenantRepository.findByTenantId("t1")).thenReturn(Optional.of(testTenant));
            when(tenantMapper.toResponseDto(any())).thenReturn(testDto);

            Optional<TenantResponseDto> result = service.getTenant("t1");
            assertTrue(result.isPresent());
        }

        @Test
        void shouldReturnEmptyWhenNotFound() {
            when(tenantRepository.findByTenantId("missing")).thenReturn(Optional.empty());
            assertTrue(service.getTenant("missing").isEmpty());
        }

        @Test
        void shouldGetTenantByDomain() {
            when(tenantRepository.findByDomain("test.com")).thenReturn(Optional.of(testTenant));
            when(tenantMapper.toResponseDto(any())).thenReturn(testDto);

            Optional<TenantResponseDto> result = service.getTenantByDomain("test.com");
            assertTrue(result.isPresent());
        }
    }

    @Nested
    @DisplayName("List Tenants")
    class ListTests {

        @Test
        void shouldListTenants() {
            when(tenantRepository.findAll()).thenReturn(List.of(testTenant));
            when(tenantMapper.toResponseDto(any())).thenReturn(testDto);

            List<TenantResponseDto> result = service.listTenants();
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("Activate/Suspend/Delete")
    class LifecycleTests {

        @Test
        void shouldActivateTenant() {
            when(tenantRepository.findByTenantId("t1")).thenReturn(Optional.of(testTenant));
            service.activateTenant("t1");
            verify(tenantRepository).save(argThat(t -> t.getStatus() == Tenant.TenantStatus.ACTIVE));
        }

        @Test
        void shouldSuspendTenant() {
            when(tenantRepository.findByTenantId("t1")).thenReturn(Optional.of(testTenant));
            service.suspendTenant("t1");
            verify(tenantRepository).save(argThat(t -> t.getStatus() == Tenant.TenantStatus.SUSPENDED));
        }

        @Test
        void shouldDeleteTenant() {
            when(tenantRepository.findByTenantId("t1")).thenReturn(Optional.of(testTenant));
            service.deleteTenant("t1");
            verify(tenantRepository).delete(testTenant);
        }

        @Test
        void shouldThrowWhenActivatingMissingTenant() {
            when(tenantRepository.findByTenantId("missing")).thenReturn(Optional.empty());
            assertThrows(TenantNotFoundException.class, () -> service.activateTenant("missing"));
        }

        @Test
        void shouldThrowWhenSuspendingMissingTenant() {
            when(tenantRepository.findByTenantId("missing")).thenReturn(Optional.empty());
            assertThrows(TenantNotFoundException.class, () -> service.suspendTenant("missing"));
        }

        @Test
        void shouldThrowWhenDeletingMissingTenant() {
            when(tenantRepository.findByTenantId("missing")).thenReturn(Optional.empty());
            assertThrows(TenantNotFoundException.class, () -> service.deleteTenant("missing"));
        }
    }
}
