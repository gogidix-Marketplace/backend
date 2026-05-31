package com.gogidix.corporatecms.domain.model;

import com.gogidix.corporatecms.domain.enums.WorkflowStatus;
import com.gogidix.corporatecms.domain.model.Workflow;
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
class Workflow_WorkflowStepTest {

        @Test
    void testBuilder() {
        Workflow.WorkflowStep dto = Workflow.WorkflowStep.builder()
                        .stepNumber(42)
            .stepName("test-stepName")
            .approverRole("test-approverRole")
            .approverId("test-approverId")
            .approverName("test-approverName")
            .status(WorkflowStatus.PENDING)
            .startedAt(LocalDateTime.of(2025,1,15,10,0))
            .completedAt(LocalDateTime.of(2025,1,15,10,0))
            .comment("test-comment")
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getStepNumber());
        assertEquals("test-stepName", dto.getStepName());
        assertEquals("test-approverRole", dto.getApproverRole());
        assertEquals("test-approverId", dto.getApproverId());
        assertEquals("test-approverName", dto.getApproverName());
        assertEquals("test-comment", dto.getComment());
    }

    @Test
    void testSettersAndGetters() {
        Workflow.WorkflowStep dto = new Workflow.WorkflowStep();
        dto.setStepNumber(99);
        dto.setStepName("val-stepName");
        dto.setApproverRole("val-approverRole");
        dto.setApproverId("val-approverId");
        dto.setApproverName("val-approverName");
        dto.setComment("val-comment");
        assertEquals(99, dto.getStepNumber());
        assertEquals("val-stepName", dto.getStepName());
        assertEquals("val-approverRole", dto.getApproverRole());
        assertEquals("val-approverId", dto.getApproverId());
        assertEquals("val-approverName", dto.getApproverName());
        assertEquals("val-comment", dto.getComment());
    }

    @Test
    void testEqualsAndHashCode() {
        Workflow.WorkflowStep dto1 = Workflow.WorkflowStep.builder()
                        .stepNumber(42)
            .stepName("test-stepName")
            .approverRole("test-approverRole")
            .approverId("test-approverId")
            .approverName("test-approverName")
            .status(WorkflowStatus.PENDING)
            .startedAt(LocalDateTime.of(2025,1,15,10,0))
            .completedAt(LocalDateTime.of(2025,1,15,10,0))
            .comment("test-comment")
            .build();
        Workflow.WorkflowStep dto2 = Workflow.WorkflowStep.builder()
                        .stepNumber(42)
            .stepName("test-stepName")
            .approverRole("test-approverRole")
            .approverId("test-approverId")
            .approverName("test-approverName")
            .status(WorkflowStatus.PENDING)
            .startedAt(LocalDateTime.of(2025,1,15,10,0))
            .completedAt(LocalDateTime.of(2025,1,15,10,0))
            .comment("test-comment")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Workflow.WorkflowStep dto = Workflow.WorkflowStep.builder()
                        .stepNumber(42)
            .stepName("test-stepName")
            .approverRole("test-approverRole")
            .approverId("test-approverId")
            .approverName("test-approverName")
            .status(WorkflowStatus.PENDING)
            .startedAt(LocalDateTime.of(2025,1,15,10,0))
            .completedAt(LocalDateTime.of(2025,1,15,10,0))
            .comment("test-comment")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}