package com.gogidix.shared.infrastructure.services.security.orchestration.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request.CreateSecurityWorkflowRequestDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request.UpdateSecurityWorkflowRequestDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.response.SecurityWorkflowResponseDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.mapper.SecurityWorkflowMapper;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.exception.SecurityWorkflowNotFoundException;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.exception.SecurityWorkflowValidationException;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.model.SecurityWorkflow;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.port.out.SecurityWorkflowRepositoryPort;
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
 * Unit tests for SecurityWorkflowService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Security Workflow Service Tests")
class SecurityWorkflowServiceTest {

    @Mock
    private SecurityWorkflowMapper mapper;

    @Mock
    private SecurityWorkflowRepositoryPort repository;

    @Mock
    private TenantContextHolder tenantContextHolder;

    @InjectMocks
    private SecurityWorkflowService securityWorkflowService;

    private static final String TEST_TENANT_ID = "test-tenant-123";
    private static final String TEST_WORKFLOW_ID = "workflow-123";

    private SecurityWorkflow testWorkflow;
    private SecurityWorkflowResponseDto testResponseDto;

    @BeforeEach
    void setUp() {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TEST_TENANT_ID);

        testWorkflow = new SecurityWorkflow(TenantId.of(TEST_TENANT_ID), "Incident Response Workflow");
        testWorkflow.setId(TEST_WORKFLOW_ID);
        testWorkflow.setDescription("Automated incident response");
        testWorkflow.setTriggerType("THREAT_DETECTED");
        testWorkflow.setCreatedAt(LocalDateTime.now());
        testWorkflow.setUpdatedAt(LocalDateTime.now());

        testResponseDto = new SecurityWorkflowResponseDto(
            TEST_WORKFLOW_ID,
            TEST_TENANT_ID,
            testWorkflow.getWorkflowId(),
            "Incident Response Workflow",
            "Automated incident response",
            "ACTIVE",
            "THREAT_DETECTED",
            testWorkflow.getCreatedAt(),
            testWorkflow.getUpdatedAt()
        );
    }

    @Test
    @DisplayName("Should create workflow successfully")
    void shouldCreateWorkflowSuccessfully() {
        CreateSecurityWorkflowRequestDto request = new CreateSecurityWorkflowRequestDto(
            "Malware Response Workflow",
            "Response to malware detection",
            "SCHEDULED"
        );

        when(mapper.toEntity(request, TEST_TENANT_ID)).thenReturn(testWorkflow);
        when(repository.save(testWorkflow)).thenReturn(testWorkflow);
        when(mapper.toResponseDto(testWorkflow)).thenReturn(testResponseDto);

        SecurityWorkflowResponseDto response = securityWorkflowService.create(request);

        assertNotNull(response);
        assertEquals(TEST_WORKFLOW_ID, response.id());
        assertEquals("Incident Response Workflow", response.workflowName());
        verify(repository).save(testWorkflow);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should find workflow by id successfully")
    void shouldFindWorkflowByIdSuccessfully() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_WORKFLOW_ID)).thenReturn(Optional.of(testWorkflow));
        when(mapper.toResponseDto(testWorkflow)).thenReturn(testResponseDto);

        SecurityWorkflowResponseDto response = securityWorkflowService.findById(TEST_WORKFLOW_ID);

        assertNotNull(response);
        assertEquals(TEST_WORKFLOW_ID, response.id());
        verify(repository).findByTenantIdAndId(TEST_TENANT_ID, TEST_WORKFLOW_ID);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when workflow not found by id")
    void shouldThrowExceptionWhenWorkflowNotFoundById() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent-id")).thenReturn(Optional.empty());

        assertThrows(SecurityWorkflowNotFoundException.class, () -> securityWorkflowService.findById("non-existent-id"));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should find all workflows")
    void shouldFindAllWorkflows() {
        SecurityWorkflow workflow2 = new SecurityWorkflow(TenantId.of(TEST_TENANT_ID), "Compliance Workflow");
        workflow2.setId("workflow-456");

        SecurityWorkflowResponseDto responseDto2 = new SecurityWorkflowResponseDto(
            "workflow-456",
            TEST_TENANT_ID,
            workflow2.getWorkflowId(),
            "Compliance Workflow",
            null,
            "ACTIVE",
            null,
            null,
            null
        );

        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of(testWorkflow, workflow2));
        when(mapper.toResponseDto(testWorkflow)).thenReturn(testResponseDto);
        when(mapper.toResponseDto(workflow2)).thenReturn(responseDto2);

        List<SecurityWorkflowResponseDto> responses = securityWorkflowService.findAll();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(TEST_WORKFLOW_ID, responses.get(0).id());
        assertEquals("workflow-456", responses.get(1).id());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should return empty list when no workflows found")
    void shouldReturnEmptyListWhenNoWorkflowsFound() {
        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of());

        List<SecurityWorkflowResponseDto> responses = securityWorkflowService.findAll();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("Should find workflows by status")
    void shouldFindWorkflowsByStatus() {
        when(repository.findByTenantIdAndStatus(TEST_TENANT_ID, SecurityWorkflow.WorkflowStatus.ACTIVE))
            .thenReturn(List.of(testWorkflow));
        when(mapper.toResponseDto(testWorkflow)).thenReturn(testResponseDto);

        List<SecurityWorkflowResponseDto> responses = securityWorkflowService.findByStatus("ACTIVE");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("ACTIVE", responses.get(0).status());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should find workflows by status case insensitive")
    void shouldFindWorkflowsByStatusCaseInsensitive() {
        when(repository.findByTenantIdAndStatus(TEST_TENANT_ID, SecurityWorkflow.WorkflowStatus.INACTIVE))
            .thenReturn(List.of(testWorkflow));
        when(mapper.toResponseDto(testWorkflow)).thenReturn(testResponseDto);

        List<SecurityWorkflowResponseDto> responses = securityWorkflowService.findByStatus("inactive");

        assertNotNull(responses);
        verify(repository).findByTenantIdAndStatus(TEST_TENANT_ID, SecurityWorkflow.WorkflowStatus.INACTIVE);
    }

    @Test
    @DisplayName("Should throw exception for invalid status")
    void shouldThrowExceptionForInvalidStatus() {
        assertThrows(SecurityWorkflowValidationException.class, () -> securityWorkflowService.findByStatus("INVALID_STATUS"));
        verify(repository, never()).findByTenantIdAndStatus(any(), any());
    }

    @Test
    @DisplayName("Should update workflow successfully")
    void shouldUpdateWorkflowSuccessfully() {
        UpdateSecurityWorkflowRequestDto request = new UpdateSecurityWorkflowRequestDto(
            "Updated Workflow",
            "Updated description",
            "MANUAL"
        );

        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_WORKFLOW_ID)).thenReturn(Optional.of(testWorkflow));
        doNothing().when(mapper).updateEntity(request, testWorkflow);
        when(repository.save(testWorkflow)).thenReturn(testWorkflow);
        when(mapper.toResponseDto(testWorkflow)).thenReturn(testResponseDto);

        SecurityWorkflowResponseDto response = securityWorkflowService.update(TEST_WORKFLOW_ID, request);

        assertNotNull(response);
        assertEquals(TEST_WORKFLOW_ID, response.id());
        verify(repository).save(testWorkflow);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent workflow")
    void shouldThrowExceptionWhenUpdatingNonExistentWorkflow() {
        UpdateSecurityWorkflowRequestDto request = new UpdateSecurityWorkflowRequestDto(
            "Updated", null, null
        );

        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent")).thenReturn(Optional.empty());

        assertThrows(SecurityWorkflowNotFoundException.class, () -> securityWorkflowService.update("non-existent", request));
        verify(mapper, never()).updateEntity(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete workflow successfully")
    void shouldDeleteWorkflowSuccessfully() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_WORKFLOW_ID)).thenReturn(Optional.of(testWorkflow));
        doNothing().when(repository).deleteByTenantIdAndId(TEST_TENANT_ID, TEST_WORKFLOW_ID);

        assertDoesNotThrow(() -> securityWorkflowService.delete(TEST_WORKFLOW_ID));
        verify(repository).deleteByTenantIdAndId(TEST_TENANT_ID, TEST_WORKFLOW_ID);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent workflow")
    void shouldThrowExceptionWhenDeletingNonExistentWorkflow() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent")).thenReturn(Optional.empty());

        assertThrows(SecurityWorkflowNotFoundException.class, () -> securityWorkflowService.delete("non-existent"));
        verify(repository, never()).deleteByTenantIdAndId(any(), any());
    }

    @Test
    @DisplayName("Should activate workflow successfully")
    void shouldActivateWorkflowSuccessfully() {
        testWorkflow.setStatus(SecurityWorkflow.WorkflowStatus.INACTIVE);
        SecurityWorkflowResponseDto activeResponseDto = new SecurityWorkflowResponseDto(
            TEST_WORKFLOW_ID,
            TEST_TENANT_ID,
            testWorkflow.getWorkflowId(),
            "Incident Response Workflow",
            "Automated incident response",
            "ACTIVE",
            "THREAT_DETECTED",
            testWorkflow.getCreatedAt(),
            LocalDateTime.now()
        );

        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_WORKFLOW_ID)).thenReturn(Optional.of(testWorkflow));
        when(repository.save(testWorkflow)).thenReturn(testWorkflow);
        when(mapper.toResponseDto(testWorkflow)).thenReturn(activeResponseDto);

        SecurityWorkflowResponseDto response = securityWorkflowService.activate(TEST_WORKFLOW_ID);

        assertNotNull(response);
        assertEquals("ACTIVE", response.status());
        assertEquals(SecurityWorkflow.WorkflowStatus.ACTIVE, testWorkflow.getStatus());
        verify(repository).save(testWorkflow);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when activating non-existent workflow")
    void shouldThrowExceptionWhenActivatingNonExistentWorkflow() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent")).thenReturn(Optional.empty());

        assertThrows(SecurityWorkflowNotFoundException.class, () -> securityWorkflowService.activate("non-existent"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should deactivate workflow successfully")
    void shouldDeactivateWorkflowSuccessfully() {
        SecurityWorkflowResponseDto inactiveResponseDto = new SecurityWorkflowResponseDto(
            TEST_WORKFLOW_ID,
            TEST_TENANT_ID,
            testWorkflow.getWorkflowId(),
            "Incident Response Workflow",
            "Automated incident response",
            "INACTIVE",
            "THREAT_DETECTED",
            testWorkflow.getCreatedAt(),
            LocalDateTime.now()
        );

        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_WORKFLOW_ID)).thenReturn(Optional.of(testWorkflow));
        when(repository.save(testWorkflow)).thenReturn(testWorkflow);
        when(mapper.toResponseDto(testWorkflow)).thenReturn(inactiveResponseDto);

        SecurityWorkflowResponseDto response = securityWorkflowService.deactivate(TEST_WORKFLOW_ID);

        assertNotNull(response);
        assertEquals("INACTIVE", response.status());
        assertEquals(SecurityWorkflow.WorkflowStatus.INACTIVE, testWorkflow.getStatus());
        verify(repository).save(testWorkflow);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should execute active workflow successfully")
    void shouldExecuteActiveWorkflowSuccessfully() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_WORKFLOW_ID)).thenReturn(Optional.of(testWorkflow));
        when(mapper.toResponseDto(testWorkflow)).thenReturn(testResponseDto);

        SecurityWorkflowResponseDto response = securityWorkflowService.execute(TEST_WORKFLOW_ID);

        assertNotNull(response);
        assertEquals(TEST_WORKFLOW_ID, response.id());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when executing inactive workflow")
    void shouldThrowExceptionWhenExecutingInactiveWorkflow() {
        testWorkflow.setStatus(SecurityWorkflow.WorkflowStatus.INACTIVE);
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_WORKFLOW_ID)).thenReturn(Optional.of(testWorkflow));

        assertThrows(SecurityWorkflowValidationException.class, () -> securityWorkflowService.execute(TEST_WORKFLOW_ID));
    }

    @Test
    @DisplayName("Should throw exception when executing non-existent workflow")
    void shouldThrowExceptionWhenExecutingNonExistentWorkflow() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent")).thenReturn(Optional.empty());

        assertThrows(SecurityWorkflowNotFoundException.class, () -> securityWorkflowService.execute("non-existent"));
    }

    @Test
    @DisplayName("Should find workflows by DRAFT status")
    void shouldFindWorkflowsByDraftStatus() {
        testWorkflow.setStatus(SecurityWorkflow.WorkflowStatus.DRAFT);
        SecurityWorkflowResponseDto draftResponseDto = new SecurityWorkflowResponseDto(
            TEST_WORKFLOW_ID,
            TEST_TENANT_ID,
            testWorkflow.getWorkflowId(),
            "Incident Response Workflow",
            "Automated incident response",
            "DRAFT",
            "THREAT_DETECTED",
            testWorkflow.getCreatedAt(),
            testWorkflow.getUpdatedAt()
        );

        when(repository.findByTenantIdAndStatus(TEST_TENANT_ID, SecurityWorkflow.WorkflowStatus.DRAFT))
            .thenReturn(List.of(testWorkflow));
        when(mapper.toResponseDto(testWorkflow)).thenReturn(draftResponseDto);

        List<SecurityWorkflowResponseDto> responses = securityWorkflowService.findByStatus("DRAFT");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("DRAFT", responses.get(0).status());
    }

    @Test
    @DisplayName("Should throw exception when executing DRAFT workflow")
    void shouldThrowExceptionWhenExecutingDraftWorkflow() {
        testWorkflow.setStatus(SecurityWorkflow.WorkflowStatus.DRAFT);
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_WORKFLOW_ID)).thenReturn(Optional.of(testWorkflow));

        assertThrows(SecurityWorkflowValidationException.class, () -> securityWorkflowService.execute(TEST_WORKFLOW_ID));
    }
}
