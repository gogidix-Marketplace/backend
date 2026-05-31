package com.gogidix.shared.infrastructure.services.security.threat.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.request.CreateThreatIndicatorRequestDto;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.request.UpdateThreatIndicatorRequestDto;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.response.ThreatIndicatorResponseDto;
import com.gogidix.shared.infrastructure.services.security.threat.application.mapper.ThreatIndicatorMapper;
import com.gogidix.shared.infrastructure.services.security.threat.domain.exception.ThreatIndicatorNotFoundException;
import com.gogidix.shared.infrastructure.services.security.threat.domain.exception.ThreatIndicatorValidationException;
import com.gogidix.shared.infrastructure.services.security.threat.domain.model.ThreatIndicator;
import com.gogidix.shared.infrastructure.services.security.threat.domain.port.out.ThreatIndicatorRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ThreatIndicatorService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Threat Indicator Service Tests")
class ThreatIndicatorServiceTest {

    @Mock
    private ThreatIndicatorMapper mapper;

    @Mock
    private ThreatIndicatorRepositoryPort repository;

    @Mock
    private TenantContextHolder tenantContextHolder;

    @InjectMocks
    private ThreatIndicatorService threatIndicatorService;

    private static final String TEST_TENANT_ID = "test-tenant-123";
    private static final String TEST_INDICATOR_ID = "indicator-123";

    private ThreatIndicator testIndicator;
    private ThreatIndicatorResponseDto testResponseDto;

    @BeforeEach
    void setUp() {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TEST_TENANT_ID);

        testIndicator = new ThreatIndicator(TenantId.of(TEST_TENANT_ID), "IP_ADDRESS", "192.168.1.1");
        testIndicator.setId(TEST_INDICATOR_ID);
        testIndicator.setSeverity(ThreatIndicator.ThreatSeverity.HIGH);
        testIndicator.setDescription("Malicious IP address");
        testIndicator.setActive(true);
        testIndicator.setCreatedAt(LocalDateTime.now());
        testIndicator.setUpdatedAt(LocalDateTime.now());

        testResponseDto = new ThreatIndicatorResponseDto(
            TEST_INDICATOR_ID,
            TEST_TENANT_ID,
            testIndicator.getIndicatorId(),
            "IP_ADDRESS",
            "192.168.1.1",
            "HIGH",
            "Malicious IP address",
            true,
            testIndicator.getCreatedAt(),
            testIndicator.getUpdatedAt()
        );
    }

    @Test
    @DisplayName("Should create indicator successfully")
    void shouldCreateIndicatorSuccessfully() {
        CreateThreatIndicatorRequestDto request = new CreateThreatIndicatorRequestDto(
            "DOMAIN",
            "malicious.example.com",
            "CRITICAL",
            "Known malicious domain"
        );

        when(mapper.toEntity(request, TEST_TENANT_ID)).thenReturn(testIndicator);
        when(repository.save(testIndicator)).thenReturn(testIndicator);
        when(mapper.toResponseDto(testIndicator)).thenReturn(testResponseDto);

        ThreatIndicatorResponseDto response = threatIndicatorService.create(request);

        assertNotNull(response);
        assertEquals(TEST_INDICATOR_ID, response.id());
        assertEquals("IP_ADDRESS", response.indicatorType());
        verify(repository).save(testIndicator);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should find indicator by id successfully")
    void shouldFindIndicatorByIdSuccessfully() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_INDICATOR_ID)).thenReturn(Optional.of(testIndicator));
        when(mapper.toResponseDto(testIndicator)).thenReturn(testResponseDto);

        ThreatIndicatorResponseDto response = threatIndicatorService.findById(TEST_INDICATOR_ID);

        assertNotNull(response);
        assertEquals(TEST_INDICATOR_ID, response.id());
        verify(repository).findByTenantIdAndId(TEST_TENANT_ID, TEST_INDICATOR_ID);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when indicator not found by id")
    void shouldThrowExceptionWhenIndicatorNotFoundById() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent-id")).thenReturn(Optional.empty());

        assertThrows(ThreatIndicatorNotFoundException.class, () -> threatIndicatorService.findById("non-existent-id"));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should find all indicators")
    void shouldFindAllIndicators() {
        ThreatIndicator indicator2 = new ThreatIndicator(TenantId.of(TEST_TENANT_ID), "HASH", "abc123");
        indicator2.setId("indicator-456");

        ThreatIndicatorResponseDto responseDto2 = new ThreatIndicatorResponseDto(
            "indicator-456",
            TEST_TENANT_ID,
            indicator2.getIndicatorId(),
            "HASH",
            "abc123",
            null,
            null,
            true,
            indicator2.getCreatedAt(),
            null
        );

        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of(testIndicator, indicator2));
        when(mapper.toResponseDto(testIndicator)).thenReturn(testResponseDto);
        when(mapper.toResponseDto(indicator2)).thenReturn(responseDto2);

        List<ThreatIndicatorResponseDto> responses = threatIndicatorService.findAll();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(TEST_INDICATOR_ID, responses.get(0).id());
        assertEquals("indicator-456", responses.get(1).id());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should return empty list when no indicators found")
    void shouldReturnEmptyListWhenNoIndicatorsFound() {
        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of());

        List<ThreatIndicatorResponseDto> responses = threatIndicatorService.findAll();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("Should find active indicators")
    void shouldFindActiveIndicators() {
        when(repository.findByTenantIdAndActiveTrue(TEST_TENANT_ID)).thenReturn(List.of(testIndicator));
        when(mapper.toResponseDto(testIndicator)).thenReturn(testResponseDto);

        List<ThreatIndicatorResponseDto> responses = threatIndicatorService.findActive();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertTrue(responses.get(0).active());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should find indicators by type")
    void shouldFindIndicatorsByType() {
        when(repository.findByTenantIdAndIndicatorType(TEST_TENANT_ID, "IP_ADDRESS")).thenReturn(List.of(testIndicator));
        when(mapper.toResponseDto(testIndicator)).thenReturn(testResponseDto);

        List<ThreatIndicatorResponseDto> responses = threatIndicatorService.findByType("IP_ADDRESS");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("IP_ADDRESS", responses.get(0).indicatorType());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should find indicators by severity")
    void shouldFindIndicatorsBySeverity() {
        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of(testIndicator));
        when(mapper.toResponseDto(testIndicator)).thenReturn(testResponseDto);

        List<ThreatIndicatorResponseDto> responses = threatIndicatorService.findBySeverity("HIGH");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("HIGH", responses.get(0).severity());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should find indicators by severity case insensitive")
    void shouldFindIndicatorsBySeverityCaseInsensitive() {
        testIndicator.setSeverity(ThreatIndicator.ThreatSeverity.CRITICAL);

        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of(testIndicator));
        when(mapper.toResponseDto(testIndicator)).thenReturn(testResponseDto);

        List<ThreatIndicatorResponseDto> responses = threatIndicatorService.findBySeverity("critical");

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    @DisplayName("Should return empty list when no indicators match severity")
    void shouldReturnEmptyListWhenNoIndicatorsMatchSeverity() {
        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of(testIndicator));

        List<ThreatIndicatorResponseDto> responses = threatIndicatorService.findBySeverity("LOW");

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("Should update indicator successfully")
    void shouldUpdateIndicatorSuccessfully() {
        UpdateThreatIndicatorRequestDto request = new UpdateThreatIndicatorRequestDto(
            null,
            "192.168.1.100",
            "CRITICAL",
            "Updated description"
        );

        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_INDICATOR_ID)).thenReturn(Optional.of(testIndicator));
        doNothing().when(mapper).updateEntity(request, testIndicator);
        when(repository.save(testIndicator)).thenReturn(testIndicator);
        when(mapper.toResponseDto(testIndicator)).thenReturn(testResponseDto);

        ThreatIndicatorResponseDto response = threatIndicatorService.update(TEST_INDICATOR_ID, request);

        assertNotNull(response);
        assertEquals(TEST_INDICATOR_ID, response.id());
        verify(repository).save(testIndicator);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent indicator")
    void shouldThrowExceptionWhenUpdatingNonExistentIndicator() {
        UpdateThreatIndicatorRequestDto request = new UpdateThreatIndicatorRequestDto(
            null, null, null, null
        );

        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent")).thenReturn(Optional.empty());

        assertThrows(ThreatIndicatorNotFoundException.class, () -> threatIndicatorService.update("non-existent", request));
        verify(mapper, never()).updateEntity(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete indicator successfully")
    void shouldDeleteIndicatorSuccessfully() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_INDICATOR_ID)).thenReturn(Optional.of(testIndicator));
        doNothing().when(repository).deleteByTenantIdAndId(TEST_TENANT_ID, TEST_INDICATOR_ID);

        assertDoesNotThrow(() -> threatIndicatorService.delete(TEST_INDICATOR_ID));
        verify(repository).deleteByTenantIdAndId(TEST_TENANT_ID, TEST_INDICATOR_ID);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent indicator")
    void shouldThrowExceptionWhenDeletingNonExistentIndicator() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent")).thenReturn(Optional.empty());

        assertThrows(ThreatIndicatorNotFoundException.class, () -> threatIndicatorService.delete("non-existent"));
        verify(repository, never()).deleteByTenantIdAndId(any(), any());
    }

    @Test
    @DisplayName("Should deactivate indicator successfully")
    void shouldDeactivateIndicatorSuccessfully() {
        ThreatIndicatorResponseDto inactiveResponseDto = new ThreatIndicatorResponseDto(
            TEST_INDICATOR_ID,
            TEST_TENANT_ID,
            testIndicator.getIndicatorId(),
            "IP_ADDRESS",
            "192.168.1.1",
            "HIGH",
            "Malicious IP address",
            false,
            testIndicator.getCreatedAt(),
            LocalDateTime.now()
        );

        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_INDICATOR_ID)).thenReturn(Optional.of(testIndicator));
        when(repository.save(testIndicator)).thenReturn(testIndicator);
        when(mapper.toResponseDto(testIndicator)).thenReturn(inactiveResponseDto);

        ThreatIndicatorResponseDto response = threatIndicatorService.deactivate(TEST_INDICATOR_ID);

        assertNotNull(response);
        assertFalse(response.active());
        assertFalse(testIndicator.getActive());
        verify(repository).save(testIndicator);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when deactivating non-existent indicator")
    void shouldThrowExceptionWhenDeactivatingNonExistentIndicator() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent")).thenReturn(Optional.empty());

        assertThrows(ThreatIndicatorNotFoundException.class, () -> threatIndicatorService.deactivate("non-existent"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should find indicators with CRITICAL severity")
    void shouldFindIndicatorsWithCriticalSeverity() {
        testIndicator.setSeverity(ThreatIndicator.ThreatSeverity.CRITICAL);
        ThreatIndicatorResponseDto criticalResponseDto = new ThreatIndicatorResponseDto(
            TEST_INDICATOR_ID,
            TEST_TENANT_ID,
            testIndicator.getIndicatorId(),
            "IP_ADDRESS",
            "192.168.1.1",
            "CRITICAL",
            "Malicious IP address",
            true,
            testIndicator.getCreatedAt(),
            testIndicator.getUpdatedAt()
        );

        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of(testIndicator));
        when(mapper.toResponseDto(testIndicator)).thenReturn(criticalResponseDto);

        List<ThreatIndicatorResponseDto> responses = threatIndicatorService.findBySeverity("CRITICAL");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("CRITICAL", responses.get(0).severity());
    }

    @Test
    @DisplayName("Should find indicators with MEDIUM severity")
    void shouldFindIndicatorsWithMediumSeverity() {
        testIndicator.setSeverity(ThreatIndicator.ThreatSeverity.MEDIUM);
        ThreatIndicatorResponseDto mediumResponseDto = new ThreatIndicatorResponseDto(
            TEST_INDICATOR_ID,
            TEST_TENANT_ID,
            testIndicator.getIndicatorId(),
            "IP_ADDRESS",
            "192.168.1.1",
            "MEDIUM",
            "Malicious IP address",
            true,
            testIndicator.getCreatedAt(),
            testIndicator.getUpdatedAt()
        );

        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of(testIndicator));
        when(mapper.toResponseDto(testIndicator)).thenReturn(mediumResponseDto);

        List<ThreatIndicatorResponseDto> responses = threatIndicatorService.findBySeverity("MEDIUM");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("MEDIUM", responses.get(0).severity());
    }

    @Test
    @DisplayName("Should find indicators with LOW severity")
    void shouldFindIndicatorsWithLowSeverity() {
        testIndicator.setSeverity(ThreatIndicator.ThreatSeverity.LOW);
        ThreatIndicatorResponseDto lowResponseDto = new ThreatIndicatorResponseDto(
            TEST_INDICATOR_ID,
            TEST_TENANT_ID,
            testIndicator.getIndicatorId(),
            "IP_ADDRESS",
            "192.168.1.1",
            "LOW",
            "Malicious IP address",
            true,
            testIndicator.getCreatedAt(),
            testIndicator.getUpdatedAt()
        );

        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of(testIndicator));
        when(mapper.toResponseDto(testIndicator)).thenReturn(lowResponseDto);

        List<ThreatIndicatorResponseDto> responses = threatIndicatorService.findBySeverity("LOW");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("LOW", responses.get(0).severity());
    }

    @Test
    @DisplayName("Should find indicators by different types")
    void shouldFindIndicatorsByDifferentTypes() {
        testIndicator.setIndicatorType("DOMAIN");
        ThreatIndicatorResponseDto domainResponseDto = new ThreatIndicatorResponseDto(
            TEST_INDICATOR_ID,
            TEST_TENANT_ID,
            testIndicator.getIndicatorId(),
            "DOMAIN",
            "malicious.example.com",
            "HIGH",
            "Malicious IP address",
            true,
            testIndicator.getCreatedAt(),
            testIndicator.getUpdatedAt()
        );

        when(repository.findByTenantIdAndIndicatorType(TEST_TENANT_ID, "DOMAIN")).thenReturn(List.of(testIndicator));
        when(mapper.toResponseDto(testIndicator)).thenReturn(domainResponseDto);

        List<ThreatIndicatorResponseDto> responses = threatIndicatorService.findByType("DOMAIN");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("DOMAIN", responses.get(0).indicatorType());
    }

    @Test
    @DisplayName("Should filter out inactive indicators when finding active")
    void shouldFilterOutInactiveIndicatorsWhenFindingActive() {
        ThreatIndicator inactiveIndicator = new ThreatIndicator(TenantId.of(TEST_TENANT_ID), "HASH", "def456");
        inactiveIndicator.setActive(false);

        when(repository.findByTenantIdAndActiveTrue(TEST_TENANT_ID)).thenReturn(List.of(testIndicator));
        when(mapper.toResponseDto(testIndicator)).thenReturn(testResponseDto);

        List<ThreatIndicatorResponseDto> responses = threatIndicatorService.findActive();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertTrue(responses.get(0).active());
    }
}
