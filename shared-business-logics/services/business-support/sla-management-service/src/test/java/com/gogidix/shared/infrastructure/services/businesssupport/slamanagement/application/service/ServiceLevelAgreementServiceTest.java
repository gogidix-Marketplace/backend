package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request.CreateServiceLevelAgreementRequestDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request.UpdateServiceLevelAgreementRequestDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.response.ServiceLevelAgreementResponseDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.mapper.ServiceLevelAgreementMapper;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.exception.ServiceLevelAgreementNotFoundException;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.model.ServiceLevelAgreement;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.port.out.IServiceLevelAgreementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ServiceLevelAgreementService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Service Level Agreement Service Tests")
class ServiceLevelAgreementServiceTest {

    @Mock
    private ServiceLevelAgreementMapper mapper;

    @Mock
    private IServiceLevelAgreementRepository repository;

    private TenantContextHolder tenantContextHolder;

    private ServiceLevelAgreementService service;

    private static final String TEST_TENANT_ID = "tenant-001";
    private ServiceLevelAgreement testSLA;
    private ServiceLevelAgreementResponseDto testResponseDto;

    @BeforeEach
    void setUp() {
        tenantContextHolder = new TenantContextHolder();
        service = new ServiceLevelAgreementService(mapper, repository, tenantContextHolder);
        tenantContextHolder.setTenantId(TEST_TENANT_ID);

        LocalDateTime now = LocalDateTime.now();
        testSLA = new ServiceLevelAgreement(
                TenantId.of(TEST_TENANT_ID),
                "Premium SLA",
                "Premium service level agreement",
                "API",
                500.0,
                99.9,
                10,
                now,
                now.plusYears(1)
        );
        testSLA.setId("sla-123");

        testResponseDto = new ServiceLevelAgreementResponseDto(
                "sla-123",
                TEST_TENANT_ID,
                "Premium SLA",
                "Premium service level agreement",
                "API",
                500.0,
                99.9,
                10,
                "ACTIVE",
                now,
                now.plusYears(1),
                now,
                now
        );
    }

    @AfterEach
    void tearDown() {
        tenantContextHolder.clear();
    }

    @Test
    @DisplayName("Should create SLA successfully")
    void shouldCreateSLASuccessfully() {
        CreateServiceLevelAgreementRequestDto dto = new CreateServiceLevelAgreementRequestDto(
                "New SLA",
                "New service level agreement",
                "API",
                200.0,
                99.5,
                5,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(6)
        );

        when(repository.existsByNameAndTenantId(anyString(), anyString())).thenReturn(false);
        when(mapper.toEntity(any(), anyString())).thenReturn(testSLA);
        when(repository.save(any(ServiceLevelAgreement.class))).thenReturn(testSLA);
        when(mapper.toResponseDto(any(ServiceLevelAgreement.class))).thenReturn(testResponseDto);

        ServiceLevelAgreementResponseDto result = service.create(dto);

        assertNotNull(result);
        assertEquals("sla-123", result.id());
        verify(repository).save(any(ServiceLevelAgreement.class));
    }

    @Test
    @DisplayName("Should throw exception when creating SLA with duplicate name")
    void shouldThrowExceptionWhenCreatingSLAWithDuplicateName() {
        CreateServiceLevelAgreementRequestDto dto = new CreateServiceLevelAgreementRequestDto(
                "Existing SLA",
                "Description",
                "API",
                100.0,
                99.0,
                5,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1)
        );

        when(repository.existsByNameAndTenantId(anyString(), anyString())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(dto)
        );

        assertTrue(exception.getMessage().contains("already exists"));
        verify(repository, never()).save(any(ServiceLevelAgreement.class));
    }

    @Test
    @DisplayName("Should throw exception when uptime percentage is invalid")
    void shouldThrowExceptionWhenUptimePercentageIsInvalid() {
        CreateServiceLevelAgreementRequestDto dto = new CreateServiceLevelAgreementRequestDto(
                "Invalid SLA",
                "Description",
                "API",
                100.0,
                101.0,
                5,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1)
        );

        ServiceLevelAgreement invalidSla = new ServiceLevelAgreement(
                TenantId.of(TEST_TENANT_ID), "Invalid SLA", "Description", "API",
                100.0, 101.0, 5, dto.validFrom(), dto.validUntil());

        when(repository.existsByNameAndTenantId(anyString(), anyString())).thenReturn(false);
        when(mapper.toEntity(any(), anyString())).thenReturn(invalidSla);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(dto)
        );

        assertTrue(exception.getMessage().contains("Uptime percentage must be between 0 and 100"));
    }

    @Test
    @DisplayName("Should throw exception when response time threshold is negative")
    void shouldThrowExceptionWhenResponseTimeThresholdIsNegative() {
        CreateServiceLevelAgreementRequestDto dto = new CreateServiceLevelAgreementRequestDto(
                "Invalid SLA",
                "Description",
                "API",
                -10.0,
                99.0,
                5,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1)
        );

        ServiceLevelAgreement invalidSla = new ServiceLevelAgreement(
                TenantId.of(TEST_TENANT_ID), "Invalid SLA", "Description", "API",
                -10.0, 99.0, 5, dto.validFrom(), dto.validUntil());

        when(repository.existsByNameAndTenantId(anyString(), anyString())).thenReturn(false);
        when(mapper.toEntity(any(), anyString())).thenReturn(invalidSla);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(dto)
        );

        assertTrue(exception.getMessage().contains("Response time threshold must be positive"));
    }

    @Test
    @DisplayName("Should throw exception when penalty percentage is invalid")
    void shouldThrowExceptionWhenPenaltyPercentageIsInvalid() {
        CreateServiceLevelAgreementRequestDto dto = new CreateServiceLevelAgreementRequestDto(
                "Invalid SLA",
                "Description",
                "API",
                100.0,
                99.0,
                150,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1)
        );

        ServiceLevelAgreement invalidSla = new ServiceLevelAgreement(
                TenantId.of(TEST_TENANT_ID), "Invalid SLA", "Description", "API",
                100.0, 99.0, 150, dto.validFrom(), dto.validUntil());

        when(repository.existsByNameAndTenantId(anyString(), anyString())).thenReturn(false);
        when(mapper.toEntity(any(), anyString())).thenReturn(invalidSla);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(dto)
        );

        assertTrue(exception.getMessage().contains("Penalty percentage must be between 0 and 100"));
    }

    @Test
    @DisplayName("Should throw exception when valid from date is after valid until")
    void shouldThrowExceptionWhenValidFromIsAfterValidUntil() {
        LocalDateTime now = LocalDateTime.now();
        CreateServiceLevelAgreementRequestDto dto = new CreateServiceLevelAgreementRequestDto(
                "Invalid SLA",
                "Description",
                "API",
                100.0,
                99.0,
                5,
                now.plusMonths(1),
                now
        );

        ServiceLevelAgreement invalidSla = new ServiceLevelAgreement(
                TenantId.of(TEST_TENANT_ID), "Invalid SLA", "Description", "API",
                100.0, 99.0, 5, now.plusMonths(1), now);

        when(repository.existsByNameAndTenantId(anyString(), anyString())).thenReturn(false);
        when(mapper.toEntity(any(), anyString())).thenReturn(invalidSla);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(dto)
        );

        assertTrue(exception.getMessage().contains("Valid from date must be before valid until date"));
    }

    @Test
    @DisplayName("Should find SLA by ID")
    void shouldFindSLAById() {
        when(repository.findByIdAndTenantId(anyString(), anyString())).thenReturn(Optional.of(testSLA));
        when(mapper.toResponseDto(any(ServiceLevelAgreement.class))).thenReturn(testResponseDto);

        ServiceLevelAgreementResponseDto result = service.findById("sla-123");

        assertNotNull(result);
        assertEquals("sla-123", result.id());
        verify(repository).findByIdAndTenantId("sla-123", TEST_TENANT_ID);
    }

    @Test
    @DisplayName("Should throw exception when finding non-existent SLA")
    void shouldThrowExceptionWhenFindingNonExistentSLA() {
        when(repository.findByIdAndTenantId(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(
                ServiceLevelAgreementNotFoundException.class,
                () -> service.findById("non-existent")
        );
    }

    @Test
    @DisplayName("Should find all SLAs for tenant")
    void shouldFindAllSLAsForTenant() {
        List<ServiceLevelAgreement> slas = Arrays.asList(testSLA);
        when(repository.findAllByTenantId(anyString())).thenReturn(slas);
        when(mapper.toResponseDto(any(ServiceLevelAgreement.class))).thenReturn(testResponseDto);

        List<ServiceLevelAgreementResponseDto> result = service.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findAllByTenantId(TEST_TENANT_ID);
    }

    @Test
    @DisplayName("Should find SLAs by status")
    void shouldFindSLAsByStatus() {
        List<ServiceLevelAgreement> slas = Arrays.asList(testSLA);
        when(repository.findByTenantIdAndStatus(anyString(), anyString())).thenReturn(slas);
        when(mapper.toResponseDto(any(ServiceLevelAgreement.class))).thenReturn(testResponseDto);

        List<ServiceLevelAgreementResponseDto> result = service.findByStatus("ACTIVE");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findByTenantIdAndStatus(TEST_TENANT_ID, "ACTIVE");
    }

    @Test
    @DisplayName("Should find SLAs by service type")
    void shouldFindSLAsByServiceType() {
        List<ServiceLevelAgreement> slas = Arrays.asList(testSLA);
        when(repository.findByTenantIdAndServiceType(anyString(), anyString())).thenReturn(slas);
        when(mapper.toResponseDto(any(ServiceLevelAgreement.class))).thenReturn(testResponseDto);

        List<ServiceLevelAgreementResponseDto> result = service.findByServiceType("API");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findByTenantIdAndServiceType(TEST_TENANT_ID, "API");
    }

    @Test
    @DisplayName("Should find active SLAs")
    void shouldFindActiveSLAs() {
        List<ServiceLevelAgreement> slas = Arrays.asList(testSLA);
        when(repository.findActiveByTenantAndDate(anyString(), any(LocalDateTime.class))).thenReturn(slas);
        when(mapper.toResponseDto(any(ServiceLevelAgreement.class))).thenReturn(testResponseDto);

        List<ServiceLevelAgreementResponseDto> result = service.findActive();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findActiveByTenantAndDate(eq(TEST_TENANT_ID), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Should update SLA successfully")
    void shouldUpdateSLASuccessfully() {
        UpdateServiceLevelAgreementRequestDto dto = new UpdateServiceLevelAgreementRequestDto(
                "Updated SLA",
                "Updated description",
                "WEB",
                300.0,
                99.8,
                8,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(3),
                "ACTIVE"
        );

        when(repository.findByIdAndTenantId(anyString(), anyString())).thenReturn(Optional.of(testSLA));
        when(repository.existsByNameAndTenantId(anyString(), anyString())).thenReturn(false);
        when(repository.save(any(ServiceLevelAgreement.class))).thenReturn(testSLA);
        when(mapper.toResponseDto(any(ServiceLevelAgreement.class))).thenReturn(testResponseDto);

        ServiceLevelAgreementResponseDto result = service.update("sla-123", dto);

        assertNotNull(result);
        verify(repository).save(any(ServiceLevelAgreement.class));
        verify(mapper).updateEntity(any(ServiceLevelAgreement.class), any(UpdateServiceLevelAgreementRequestDto.class));
    }

    @Test
    @DisplayName("Should throw exception when updating with duplicate name")
    void shouldThrowExceptionWhenUpdatingWithDuplicateName() {
        testSLA.setName("Original Name");
        UpdateServiceLevelAgreementRequestDto dto = new UpdateServiceLevelAgreementRequestDto(
                "Different Name",
                "Description",
                "API",
                100.0,
                99.0,
                5,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1),
                "ACTIVE"
        );

        when(repository.findByIdAndTenantId(anyString(), anyString())).thenReturn(Optional.of(testSLA));
        when(repository.existsByNameAndTenantId(anyString(), anyString())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.update("sla-123", dto)
        );

        assertTrue(exception.getMessage().contains("already exists"));
        verify(repository, never()).save(any(ServiceLevelAgreement.class));
    }

    @Test
    @DisplayName("Should delete SLA successfully")
    void shouldDeleteSLASuccessfully() {
        when(repository.findByIdAndTenantId(anyString(), anyString())).thenReturn(Optional.of(testSLA));

        service.delete("sla-123");

        verify(repository).deleteByTenantIdAndId(TEST_TENANT_ID, "sla-123");
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent SLA")
    void shouldThrowExceptionWhenDeletingNonExistentSLA() {
        when(repository.findByIdAndTenantId(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(
                ServiceLevelAgreementNotFoundException.class,
                () -> service.delete("non-existent")
        );

        verify(repository, never()).deleteByTenantIdAndId(anyString(), anyString());
    }

    @Test
    @DisplayName("Should deactivate SLA successfully")
    void shouldDeactivateSLASuccessfully() {
        when(repository.findByIdAndTenantId(anyString(), anyString())).thenReturn(Optional.of(testSLA));
        when(repository.save(any(ServiceLevelAgreement.class))).thenReturn(testSLA);

        service.deactivate("sla-123");

        verify(repository).save(argThat(sla -> "INACTIVE".equals(sla.getStatus())));
    }

    @Test
    @DisplayName("Should throw exception when deactivating non-existent SLA")
    void shouldThrowExceptionWhenDeactivatingNonExistentSLA() {
        when(repository.findByIdAndTenantId(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(
                ServiceLevelAgreementNotFoundException.class,
                () -> service.deactivate("non-existent")
        );

        verify(repository, never()).save(any(ServiceLevelAgreement.class));
    }

    @Test
    @DisplayName("Should validate SLA on update")
    void shouldValidateSLAOnUpdate() {
        UpdateServiceLevelAgreementRequestDto dto = new UpdateServiceLevelAgreementRequestDto(
                "Same Name",
                "Description",
                "API",
                100.0,
                150.0,
                5,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1),
                "ACTIVE"
        );

        ServiceLevelAgreement entity = new ServiceLevelAgreement(
                TenantId.of(TEST_TENANT_ID), "Same Name", "Description", "API",
                100.0, 99.0, 5, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
        entity.setId("sla-123");

        when(repository.findByIdAndTenantId(anyString(), anyString())).thenReturn(Optional.of(entity));
        doAnswer(invocation -> {
            ServiceLevelAgreement sla = invocation.getArgument(0);
            UpdateServiceLevelAgreementRequestDto d = invocation.getArgument(1);
            sla.setUptimePercentage(d.uptimePercentage());
            return null;
        }).when(mapper).updateEntity(any(ServiceLevelAgreement.class), any(UpdateServiceLevelAgreementRequestDto.class));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.update("sla-123", dto)
        );

        assertTrue(exception.getMessage().contains("Uptime percentage must be between 0 and 100"));
    }

    @Test
    @DisplayName("Should handle zero uptime percentage")
    void shouldHandleZeroUptimePercentage() {
        CreateServiceLevelAgreementRequestDto dto = new CreateServiceLevelAgreementRequestDto(
                "Zero Uptime SLA",
                "Description",
                "API",
                100.0,
                0.0,
                5,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1)
        );

        when(repository.existsByNameAndTenantId(anyString(), anyString())).thenReturn(false);
        when(mapper.toEntity(any(), anyString())).thenReturn(testSLA);
        when(repository.save(any(ServiceLevelAgreement.class))).thenReturn(testSLA);
        when(mapper.toResponseDto(any(ServiceLevelAgreement.class))).thenReturn(testResponseDto);

        // Should not throw exception for 0.0
        assertDoesNotThrow(() -> service.create(dto));
    }

    @Test
    @DisplayName("Should handle null valid dates")
    void shouldHandleNullValidDates() {
        CreateServiceLevelAgreementRequestDto dto = new CreateServiceLevelAgreementRequestDto(
                "SLA without dates",
                "Description",
                "API",
                100.0,
                99.0,
                5,
                null,
                null
        );

        when(repository.existsByNameAndTenantId(anyString(), anyString())).thenReturn(false);
        when(mapper.toEntity(any(), anyString())).thenReturn(testSLA);
        when(repository.save(any(ServiceLevelAgreement.class))).thenReturn(testSLA);
        when(mapper.toResponseDto(any(ServiceLevelAgreement.class))).thenReturn(testResponseDto);

        // Should not throw exception for null dates
        assertDoesNotThrow(() -> service.create(dto));
    }
}
