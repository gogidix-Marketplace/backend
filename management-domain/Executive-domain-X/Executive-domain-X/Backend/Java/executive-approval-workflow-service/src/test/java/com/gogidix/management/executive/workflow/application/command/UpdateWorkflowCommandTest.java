package com.gogidix.management.executive.workflow.application.command;

import com.gogidix.management.executive.workflow.application.command.UpdateWorkflowCommand;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UpdateWorkflowCommandTest {

        @Test
    void testBuilder() {
        UpdateWorkflowCommand dto = UpdateWorkflowCommand.builder()
                        .workflowId("test-workflowId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateWorkflowCommand.WorkflowStatus.DRAFT)
            .build();
        assertNotNull(dto);
        assertEquals("test-workflowId", dto.getWorkflowId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-layout", dto.getLayout());
        assertEquals(UpdateWorkflowCommand.WorkflowStatus.DRAFT, dto.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        UpdateWorkflowCommand dto = new UpdateWorkflowCommand();
        dto.setWorkflowId("val-workflowId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setLayout("val-layout");
        dto.setStatus(UpdateWorkflowCommand.WorkflowStatus.DRAFT);
        assertEquals("val-workflowId", dto.getWorkflowId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-layout", dto.getLayout());
        assertEquals(UpdateWorkflowCommand.WorkflowStatus.DRAFT, dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdateWorkflowCommand dto1 = UpdateWorkflowCommand.builder()
                        .workflowId("test-workflowId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateWorkflowCommand.WorkflowStatus.DRAFT)
            .build();
        UpdateWorkflowCommand dto2 = UpdateWorkflowCommand.builder()
                        .workflowId("test-workflowId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateWorkflowCommand.WorkflowStatus.DRAFT)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpdateWorkflowCommand dto = UpdateWorkflowCommand.builder()
                        .workflowId("test-workflowId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .layout("test-layout")
            .status(UpdateWorkflowCommand.WorkflowStatus.DRAFT)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}