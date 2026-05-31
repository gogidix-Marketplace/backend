package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.infrastructure.persistence.impl;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.model.ServiceLevelAgreement;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.infrastructure.persistence.ServiceLevelAgreementRepository;
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

@ExtendWith(MockitoExtension.class)
@DisplayName("ServiceLevelAgreementRepositoryImpl Tests")
class ServiceLevelAgreementRepositoryImplTest {

    @Mock
    private ServiceLevelAgreementRepository mongoRepository;

    private ServiceLevelAgreementRepositoryImpl repositoryImpl;

    private ServiceLevelAgreement testSLA;
    private static final String TENANT_ID = "tenant-1";

    @BeforeEach
    void setUp() {
        repositoryImpl = new ServiceLevelAgreementRepositoryImpl(mongoRepository);
        LocalDateTime now = LocalDateTime.now();
        testSLA = new ServiceLevelAgreement(
                TenantId.of(TENANT_ID), "SLA", "Desc", "API",
                500.0, 99.9, 10, now, now.plusYears(1)
        );
        testSLA.setId("sla-1");
    }

    @Test
    @DisplayName("Should save entity")
    void shouldSave() {
        when(mongoRepository.save(any())).thenReturn(testSLA);
        ServiceLevelAgreement result = repositoryImpl.save(testSLA);
        assertNotNull(result);
        assertEquals("sla-1", result.getId());
    }

    @Test
    @DisplayName("Should find by ID")
    void shouldFindById() {
        when(mongoRepository.findById("sla-1")).thenReturn(Optional.of(testSLA));
        Optional<ServiceLevelAgreement> result = repositoryImpl.findById("sla-1");
        assertTrue(result.isPresent());
        assertEquals("sla-1", result.get().getId());
    }

    @Test
    @DisplayName("Should find by ID returning empty")
    void shouldFindByIdEmpty() {
        when(mongoRepository.findById("nonexistent")).thenReturn(Optional.empty());
        Optional<ServiceLevelAgreement> result = repositoryImpl.findById("nonexistent");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should find all by tenant ID")
    void shouldFindAllByTenantId() {
        when(mongoRepository.findByTenantId_Value(TENANT_ID)).thenReturn(Arrays.asList(testSLA));
        List<ServiceLevelAgreement> result = repositoryImpl.findAllByTenantId(TENANT_ID);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should find by ID and tenant ID")
    void shouldFindByIdAndTenantId() {
        when(mongoRepository.findByTenantId_ValueAndId(TENANT_ID, "sla-1")).thenReturn(Optional.of(testSLA));
        Optional<ServiceLevelAgreement> result = repositoryImpl.findByIdAndTenantId("sla-1", TENANT_ID);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Should find by name and tenant ID")
    void shouldFindByNameAndTenantId() {
        when(mongoRepository.findByTenantId_ValueAndName(TENANT_ID, "SLA")).thenReturn(Optional.of(testSLA));
        Optional<ServiceLevelAgreement> result = repositoryImpl.findByNameAndTenantId("SLA", TENANT_ID);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Should find by tenant and status")
    void shouldFindByTenantIdAndStatus() {
        when(mongoRepository.findByTenantId_ValueAndStatus(TENANT_ID, "ACTIVE")).thenReturn(Arrays.asList(testSLA));
        List<ServiceLevelAgreement> result = repositoryImpl.findByTenantIdAndStatus(TENANT_ID, "ACTIVE");
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should find by tenant and service type")
    void shouldFindByTenantIdAndServiceType() {
        when(mongoRepository.findByTenantId_ValueAndServiceType(TENANT_ID, "API")).thenReturn(Arrays.asList(testSLA));
        List<ServiceLevelAgreement> result = repositoryImpl.findByTenantIdAndServiceType(TENANT_ID, "API");
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should find active by tenant and date")
    void shouldFindActiveByTenantAndDate() {
        LocalDateTime date = LocalDateTime.now();
        when(mongoRepository.findActiveByTenantAndDate(TENANT_ID, date)).thenReturn(Arrays.asList(testSLA));
        List<ServiceLevelAgreement> result = repositoryImpl.findActiveByTenantAndDate(TENANT_ID, date);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should count by tenant ID")
    void shouldCountByTenantId() {
        when(mongoRepository.countByTenantId_Value(TENANT_ID)).thenReturn(5L);
        long count = repositoryImpl.countByTenantId(TENANT_ID);
        assertEquals(5L, count);
    }

    @Test
    @DisplayName("Should delete by tenant and ID")
    void shouldDeleteByTenantIdAndId() {
        doNothing().when(mongoRepository).deleteByTenantId_ValueAndId(TENANT_ID, "sla-1");
        repositoryImpl.deleteByTenantIdAndId(TENANT_ID, "sla-1");
        verify(mongoRepository).deleteByTenantId_ValueAndId(TENANT_ID, "sla-1");
    }

    @Test
    @DisplayName("Should check exists by name and tenant ID")
    void shouldExistsByNameAndTenantId() {
        when(mongoRepository.existsByTenantId_ValueAndName(TENANT_ID, "SLA")).thenReturn(true);
        boolean exists = repositoryImpl.existsByNameAndTenantId("SLA", TENANT_ID);
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when name does not exist")
    void shouldNotExistByNameAndTenantId() {
        when(mongoRepository.existsByTenantId_ValueAndName(TENANT_ID, "NonExistent")).thenReturn(false);
        boolean exists = repositoryImpl.existsByNameAndTenantId("NonExistent", TENANT_ID);
        assertFalse(exists);
    }
}
