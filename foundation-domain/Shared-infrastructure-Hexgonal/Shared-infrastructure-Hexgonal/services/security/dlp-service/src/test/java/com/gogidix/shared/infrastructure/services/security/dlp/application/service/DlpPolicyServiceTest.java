package com.gogidix.shared.infrastructure.services.security.dlp.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request.CreateDlpPolicyRequestDto;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request.UpdateDlpPolicyRequestDto;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.response.DlpPolicyResponseDto;
import com.gogidix.shared.infrastructure.services.security.dlp.application.mapper.DlpPolicyMapper;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.exception.DlpPolicyNotFoundException;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.exception.DlpPolicyValidationException;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.model.DlpPolicy;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.port.out.DlpPolicyRepositoryPort;
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
 * Unit tests for DlpPolicyService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DLP Policy Service Tests")
class DlpPolicyServiceTest {

    @Mock
    private DlpPolicyMapper mapper;

    @Mock
    private DlpPolicyRepositoryPort repository;

    @Mock
    private TenantContextHolder tenantContextHolder;

    @InjectMocks
    private DlpPolicyService dlpPolicyService;

    private static final String TEST_TENANT_ID = "test-tenant-123";
    private static final String TEST_POLICY_ID = "policy-123";

    private DlpPolicy testPolicy;
    private DlpPolicyResponseDto testResponseDto;

    @BeforeEach
    void setUp() {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TEST_TENANT_ID);

        testPolicy = new DlpPolicy(TenantId.of(TEST_TENANT_ID), "Test Policy");
        testPolicy.setId(TEST_POLICY_ID);
        testPolicy.setDescription("Test Description");
        testPolicy.setSensitiveDataPatterns(List.of("\\d{3}-\\d{2}-\\d{4}"));
        testPolicy.setAction("BLOCK");
        testPolicy.setCreatedAt(LocalDateTime.now());
        testPolicy.setUpdatedAt(LocalDateTime.now());

        testResponseDto = new DlpPolicyResponseDto(
            TEST_POLICY_ID,
            TEST_TENANT_ID,
            testPolicy.getPolicyId(),
            "Test Policy",
            "Test Description",
            "ACTIVE",
            List.of("\\d{3}-\\d{2}-\\d{4}"),
            "BLOCK",
            testPolicy.getCreatedAt(),
            testPolicy.getUpdatedAt()
        );
    }

    @Test
    @DisplayName("Should create policy successfully")
    void shouldCreatePolicySuccessfully() {
        CreateDlpPolicyRequestDto request = new CreateDlpPolicyRequestDto(
            "New Policy",
            "New Description",
            List.of("\\d{9}"),
            "ALERT"
        );

        when(repository.existsByTenantIdAndPolicyName(TEST_TENANT_ID, "New Policy")).thenReturn(false);
        when(mapper.toEntity(request, TEST_TENANT_ID)).thenReturn(testPolicy);
        when(repository.save(testPolicy)).thenReturn(testPolicy);
        when(mapper.toResponseDto(testPolicy)).thenReturn(testResponseDto);

        DlpPolicyResponseDto response = dlpPolicyService.create(request);

        assertNotNull(response);
        assertEquals(TEST_POLICY_ID, response.id());
        assertEquals("Test Policy", response.policyName());
        verify(repository).existsByTenantIdAndPolicyName(TEST_TENANT_ID, "New Policy");
        verify(repository).save(testPolicy);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when creating policy with duplicate name")
    void shouldThrowExceptionWhenCreatingPolicyWithDuplicateName() {
        CreateDlpPolicyRequestDto request = new CreateDlpPolicyRequestDto(
            "Existing Policy",
            "Description",
            List.of(),
            "BLOCK"
        );

        when(repository.existsByTenantIdAndPolicyName(TEST_TENANT_ID, "Existing Policy")).thenReturn(true);

        assertThrows(DlpPolicyValidationException.class, () -> dlpPolicyService.create(request));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should find policy by id successfully")
    void shouldFindPolicyByIdSuccessfully() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_POLICY_ID)).thenReturn(Optional.of(testPolicy));
        when(mapper.toResponseDto(testPolicy)).thenReturn(testResponseDto);

        DlpPolicyResponseDto response = dlpPolicyService.findById(TEST_POLICY_ID);

        assertNotNull(response);
        assertEquals(TEST_POLICY_ID, response.id());
        verify(repository).findByTenantIdAndId(TEST_TENANT_ID, TEST_POLICY_ID);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when policy not found by id")
    void shouldThrowExceptionWhenPolicyNotFoundById() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent-id")).thenReturn(Optional.empty());

        assertThrows(DlpPolicyNotFoundException.class, () -> dlpPolicyService.findById("non-existent-id"));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should find all policies")
    void shouldFindAllPolicies() {
        DlpPolicy policy2 = new DlpPolicy(TenantId.of(TEST_TENANT_ID), "Policy 2");
        policy2.setId("policy-456");

        DlpPolicyResponseDto responseDto2 = new DlpPolicyResponseDto(
            "policy-456",
            TEST_TENANT_ID,
            policy2.getPolicyId(),
            "Policy 2",
            null,
            "ACTIVE",
            null,
            null,
            null,
            null
        );

        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of(testPolicy, policy2));
        when(mapper.toResponseDto(testPolicy)).thenReturn(testResponseDto);
        when(mapper.toResponseDto(policy2)).thenReturn(responseDto2);

        List<DlpPolicyResponseDto> responses = dlpPolicyService.findAll();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(TEST_POLICY_ID, responses.get(0).id());
        assertEquals("policy-456", responses.get(1).id());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should return empty list when no policies found")
    void shouldReturnEmptyListWhenNoPoliciesFound() {
        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of());

        List<DlpPolicyResponseDto> responses = dlpPolicyService.findAll();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("Should find policies by status")
    void shouldFindPoliciesByStatus() {
        when(repository.findByTenantIdAndStatus(TEST_TENANT_ID, DlpPolicy.PolicyStatus.ACTIVE))
            .thenReturn(List.of(testPolicy));
        when(mapper.toResponseDto(testPolicy)).thenReturn(testResponseDto);

        List<DlpPolicyResponseDto> responses = dlpPolicyService.findByStatus("ACTIVE");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("ACTIVE", responses.get(0).status());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should find policies by status case insensitive")
    void shouldFindPoliciesByStatusCaseInsensitive() {
        when(repository.findByTenantIdAndStatus(TEST_TENANT_ID, DlpPolicy.PolicyStatus.INACTIVE))
            .thenReturn(List.of(testPolicy));
        when(mapper.toResponseDto(testPolicy)).thenReturn(testResponseDto);

        List<DlpPolicyResponseDto> responses = dlpPolicyService.findByStatus("inactive");

        assertNotNull(responses);
        verify(repository).findByTenantIdAndStatus(TEST_TENANT_ID, DlpPolicy.PolicyStatus.INACTIVE);
    }

    @Test
    @DisplayName("Should throw exception for invalid status")
    void shouldThrowExceptionForInvalidStatus() {
        assertThrows(DlpPolicyValidationException.class, () -> dlpPolicyService.findByStatus("INVALID_STATUS"));
        verify(repository, never()).findByTenantIdAndStatus(any(), any());
    }

    @Test
    @DisplayName("Should update policy successfully")
    void shouldUpdatePolicySuccessfully() {
        UpdateDlpPolicyRequestDto request = new UpdateDlpPolicyRequestDto(
            "Updated Policy",
            "Updated Description",
            List.of("\\d{9}"),
            "QUARANTINE"
        );

        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_POLICY_ID)).thenReturn(Optional.of(testPolicy));
        doNothing().when(mapper).updateEntity(request, testPolicy);
        when(repository.save(testPolicy)).thenReturn(testPolicy);
        when(mapper.toResponseDto(testPolicy)).thenReturn(testResponseDto);

        DlpPolicyResponseDto response = dlpPolicyService.update(TEST_POLICY_ID, request);

        assertNotNull(response);
        assertEquals(TEST_POLICY_ID, response.id());
        verify(repository).findByTenantIdAndId(TEST_TENANT_ID, TEST_POLICY_ID);
        verify(mapper).updateEntity(request, testPolicy);
        verify(repository).save(testPolicy);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent policy")
    void shouldThrowExceptionWhenUpdatingNonExistentPolicy() {
        UpdateDlpPolicyRequestDto request = new UpdateDlpPolicyRequestDto(
            "Updated",
            null,
            null,
            null
        );

        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent")).thenReturn(Optional.empty());

        assertThrows(DlpPolicyNotFoundException.class, () -> dlpPolicyService.update("non-existent", request));
        verify(mapper, never()).updateEntity(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete policy successfully")
    void shouldDeletePolicySuccessfully() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_POLICY_ID)).thenReturn(Optional.of(testPolicy));
        doNothing().when(repository).deleteByTenantIdAndId(TEST_TENANT_ID, TEST_POLICY_ID);

        assertDoesNotThrow(() -> dlpPolicyService.delete(TEST_POLICY_ID));
        verify(repository).findByTenantIdAndId(TEST_TENANT_ID, TEST_POLICY_ID);
        verify(repository).deleteByTenantIdAndId(TEST_TENANT_ID, TEST_POLICY_ID);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent policy")
    void shouldThrowExceptionWhenDeletingNonExistentPolicy() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent")).thenReturn(Optional.empty());

        assertThrows(DlpPolicyNotFoundException.class, () -> dlpPolicyService.delete("non-existent"));
        verify(repository, never()).deleteByTenantIdAndId(any(), any());
    }

    @Test
    @DisplayName("Should activate policy successfully")
    void shouldActivatePolicySuccessfully() {
        testPolicy.setStatus(DlpPolicy.PolicyStatus.INACTIVE);
        DlpPolicyResponseDto activeResponseDto = new DlpPolicyResponseDto(
            TEST_POLICY_ID,
            TEST_TENANT_ID,
            testPolicy.getPolicyId(),
            "Test Policy",
            "Test Description",
            "ACTIVE",
            List.of("\\d{3}-\\d{2}-\\d{4}"),
            "BLOCK",
            testPolicy.getCreatedAt(),
            LocalDateTime.now()
        );

        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_POLICY_ID)).thenReturn(Optional.of(testPolicy));
        when(repository.save(testPolicy)).thenReturn(testPolicy);
        when(mapper.toResponseDto(testPolicy)).thenReturn(activeResponseDto);

        DlpPolicyResponseDto response = dlpPolicyService.activate(TEST_POLICY_ID);

        assertNotNull(response);
        assertEquals("ACTIVE", response.status());
        assertEquals(DlpPolicy.PolicyStatus.ACTIVE, testPolicy.getStatus());
        verify(repository).save(testPolicy);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when activating non-existent policy")
    void shouldThrowExceptionWhenActivatingNonExistentPolicy() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent")).thenReturn(Optional.empty());

        assertThrows(DlpPolicyNotFoundException.class, () -> dlpPolicyService.activate("non-existent"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should deactivate policy successfully")
    void shouldDeactivatePolicySuccessfully() {
        DlpPolicyResponseDto inactiveResponseDto = new DlpPolicyResponseDto(
            TEST_POLICY_ID,
            TEST_TENANT_ID,
            testPolicy.getPolicyId(),
            "Test Policy",
            "Test Description",
            "INACTIVE",
            List.of("\\d{3}-\\d{2}-\\d{4}"),
            "BLOCK",
            testPolicy.getCreatedAt(),
            LocalDateTime.now()
        );

        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_POLICY_ID)).thenReturn(Optional.of(testPolicy));
        when(repository.save(testPolicy)).thenReturn(testPolicy);
        when(mapper.toResponseDto(testPolicy)).thenReturn(inactiveResponseDto);

        DlpPolicyResponseDto response = dlpPolicyService.deactivate(TEST_POLICY_ID);

        assertNotNull(response);
        assertEquals("INACTIVE", response.status());
        assertEquals(DlpPolicy.PolicyStatus.INACTIVE, testPolicy.getStatus());
        verify(repository).save(testPolicy);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when deactivating non-existent policy")
    void shouldThrowExceptionWhenDeactivatingNonExistentPolicy() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent")).thenReturn(Optional.empty());

        assertThrows(DlpPolicyNotFoundException.class, () -> dlpPolicyService.deactivate("non-existent"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should find policies by DRAFT status")
    void shouldFindPoliciesByDraftStatus() {
        testPolicy.setStatus(DlpPolicy.PolicyStatus.DRAFT);
        DlpPolicyResponseDto draftResponseDto = new DlpPolicyResponseDto(
            TEST_POLICY_ID,
            TEST_TENANT_ID,
            testPolicy.getPolicyId(),
            "Test Policy",
            "Test Description",
            "DRAFT",
            List.of("\\d{3}-\\d{2}-\\d{4}"),
            "BLOCK",
            testPolicy.getCreatedAt(),
            testPolicy.getUpdatedAt()
        );

        when(repository.findByTenantIdAndStatus(TEST_TENANT_ID, DlpPolicy.PolicyStatus.DRAFT))
            .thenReturn(List.of(testPolicy));
        when(mapper.toResponseDto(testPolicy)).thenReturn(draftResponseDto);

        List<DlpPolicyResponseDto> responses = dlpPolicyService.findByStatus("DRAFT");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("DRAFT", responses.get(0).status());
    }

    @Test
    @DisplayName("Should create policy with null optional fields")
    void shouldCreatePolicyWithNullOptionalFields() {
        CreateDlpPolicyRequestDto request = new CreateDlpPolicyRequestDto(
            "Minimal Policy",
            null,
            null,
            null
        );

        when(repository.existsByTenantIdAndPolicyName(TEST_TENANT_ID, "Minimal Policy")).thenReturn(false);
        when(mapper.toEntity(request, TEST_TENANT_ID)).thenReturn(testPolicy);
        when(repository.save(testPolicy)).thenReturn(testPolicy);
        when(mapper.toResponseDto(testPolicy)).thenReturn(testResponseDto);

        DlpPolicyResponseDto response = dlpPolicyService.create(request);

        assertNotNull(response);
        verify(repository).save(testPolicy);
    }
}
