package com.gogidix.shared.infrastructure.services.security.orchestration.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SecurityWorkflow domain model.
 */
@DisplayName("SecurityWorkflow Domain Model Tests")
class SecurityWorkflowTest {

    @Test
    @DisplayName("Should create SecurityWorkflow with default values")
    void shouldCreateSecurityWorkflowWithDefaults() {
        SecurityWorkflow workflow = new SecurityWorkflow();

        assertNull(workflow.getId());
        assertNull(workflow.getTenantId());
        assertNull(workflow.getWorkflowId());
        assertNull(workflow.getWorkflowName());
        assertNull(workflow.getDescription());
        assertEquals(SecurityWorkflow.WorkflowStatus.ACTIVE, workflow.getStatus());
        assertNull(workflow.getTriggerType());
        assertNull(workflow.getCreatedAt());
        assertNull(workflow.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create SecurityWorkflow with constructor")
    void shouldCreateSecurityWorkflowWithConstructor() {
        TenantId tenantId = TenantId.of("tenant-123");
        SecurityWorkflow workflow = new SecurityWorkflow(tenantId, "Incident Response Workflow");

        assertEquals(tenantId, workflow.getTenantId());
        assertEquals("Incident Response Workflow", workflow.getWorkflowName());
        assertNotNull(workflow.getWorkflowId());
        assertEquals(SecurityWorkflow.WorkflowStatus.ACTIVE, workflow.getStatus());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllProperties() {
        SecurityWorkflow workflow = new SecurityWorkflow();
        TenantId tenantId = TenantId.of("tenant-123");
        LocalDateTime now = LocalDateTime.now();

        workflow.setId("workflow-123");
        workflow.setTenantId(tenantId);
        workflow.setWorkflowId("workflow-id-456");
        workflow.setWorkflowName("Malware Response Workflow");
        workflow.setDescription("Automated response to malware detection");
        workflow.setStatus(SecurityWorkflow.WorkflowStatus.ACTIVE);
        workflow.setTriggerType("THREAT_DETECTED");
        workflow.setCreatedAt(now);
        workflow.setUpdatedAt(now);

        assertEquals("workflow-123", workflow.getId());
        assertEquals(tenantId, workflow.getTenantId());
        assertEquals("workflow-id-456", workflow.getWorkflowId());
        assertEquals("Malware Response Workflow", workflow.getWorkflowName());
        assertEquals("Automated response to malware detection", workflow.getDescription());
        assertEquals(SecurityWorkflow.WorkflowStatus.ACTIVE, workflow.getStatus());
        assertEquals("THREAT_DETECTED", workflow.getTriggerType());
        assertEquals(now, workflow.getCreatedAt());
        assertEquals(now, workflow.getUpdatedAt());
    }

    @Test
    @DisplayName("Should generate unique workflow IDs")
    void shouldGenerateUniqueWorkflowIds() {
        TenantId tenantId = TenantId.of("tenant-123");

        SecurityWorkflow workflow1 = new SecurityWorkflow(tenantId, "Workflow 1");
        SecurityWorkflow workflow2 = new SecurityWorkflow(tenantId, "Workflow 2");

        assertNotEquals(workflow1.getWorkflowId(), workflow2.getWorkflowId());
    }

    @Test
    @DisplayName("Should set status to ACTIVE by default")
    void shouldSetStatusToActiveByDefault() {
        TenantId tenantId = TenantId.of("tenant-123");
        SecurityWorkflow workflow = new SecurityWorkflow(tenantId, "Test Workflow");

        assertEquals(SecurityWorkflow.WorkflowStatus.ACTIVE, workflow.getStatus());
    }

    @Test
    @DisplayName("Should handle all WorkflowStatus enum values")
    void shouldHandleAllWorkflowStatusEnums() {
        assertEquals(3, SecurityWorkflow.WorkflowStatus.values().length);
        assertEquals(SecurityWorkflow.WorkflowStatus.ACTIVE, SecurityWorkflow.WorkflowStatus.valueOf("ACTIVE"));
        assertEquals(SecurityWorkflow.WorkflowStatus.INACTIVE, SecurityWorkflow.WorkflowStatus.valueOf("INACTIVE"));
        assertEquals(SecurityWorkflow.WorkflowStatus.DRAFT, SecurityWorkflow.WorkflowStatus.valueOf("DRAFT"));
    }

    @Test
    @DisplayName("Should handle different trigger types")
    void shouldHandleDifferentTriggerTypes() {
        SecurityWorkflow workflow = new SecurityWorkflow();

        workflow.setTriggerType("THREAT_DETECTED");
        assertEquals("THREAT_DETECTED", workflow.getTriggerType());

        workflow.setTriggerType("SCHEDULED");
        assertEquals("SCHEDULED", workflow.getTriggerType());

        workflow.setTriggerType("MANUAL");
        assertEquals("MANUAL", workflow.getTriggerType());

        workflow.setTriggerType("EVENT_BASED");
        assertEquals("EVENT_BASED", workflow.getTriggerType());
    }

    @Test
    @DisplayName("Should handle null description")
    void shouldHandleNullDescription() {
        SecurityWorkflow workflow = new SecurityWorkflow();
        workflow.setDescription(null);

        assertNull(workflow.getDescription());
    }

    @Test
    @DisplayName("Should handle non-null description")
    void shouldHandleNonNullDescription() {
        SecurityWorkflow workflow = new SecurityWorkflow();
        workflow.setDescription("Test description");

        assertEquals("Test description", workflow.getDescription());
    }

    @Test
    @DisplayName("Should handle different statuses")
    void shouldHandleDifferentStatuses() {
        SecurityWorkflow workflow = new SecurityWorkflow();

        workflow.setStatus(SecurityWorkflow.WorkflowStatus.ACTIVE);
        assertEquals(SecurityWorkflow.WorkflowStatus.ACTIVE, workflow.getStatus());

        workflow.setStatus(SecurityWorkflow.WorkflowStatus.INACTIVE);
        assertEquals(SecurityWorkflow.WorkflowStatus.INACTIVE, workflow.getStatus());

        workflow.setStatus(SecurityWorkflow.WorkflowStatus.DRAFT);
        assertEquals(SecurityWorkflow.WorkflowStatus.DRAFT, workflow.getStatus());
    }
}
