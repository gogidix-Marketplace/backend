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
class Workflow_WorkflowActionTest {

        @Test
    void testBuilder() {
        Workflow.WorkflowAction dto = Workflow.WorkflowAction.builder()
                        .actionType("test-actionType")
            .actorId("test-actorId")
            .actorName("test-actorName")
            .comment("test-comment")
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .fromStatus(WorkflowStatus.PENDING)
            .toStatus(WorkflowStatus.PENDING)
            .build();
        assertNotNull(dto);
        assertEquals("test-actionType", dto.getActionType());
        assertEquals("test-actorId", dto.getActorId());
        assertEquals("test-actorName", dto.getActorName());
        assertEquals("test-comment", dto.getComment());
    }

    @Test
    void testSettersAndGetters() {
        Workflow.WorkflowAction dto = new Workflow.WorkflowAction();
        dto.setActionType("val-actionType");
        dto.setActorId("val-actorId");
        dto.setActorName("val-actorName");
        dto.setComment("val-comment");
        assertEquals("val-actionType", dto.getActionType());
        assertEquals("val-actorId", dto.getActorId());
        assertEquals("val-actorName", dto.getActorName());
        assertEquals("val-comment", dto.getComment());
    }

    @Test
    void testEqualsAndHashCode() {
        Workflow.WorkflowAction dto1 = Workflow.WorkflowAction.builder()
                        .actionType("test-actionType")
            .actorId("test-actorId")
            .actorName("test-actorName")
            .comment("test-comment")
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .fromStatus(WorkflowStatus.PENDING)
            .toStatus(WorkflowStatus.PENDING)
            .build();
        Workflow.WorkflowAction dto2 = Workflow.WorkflowAction.builder()
                        .actionType("test-actionType")
            .actorId("test-actorId")
            .actorName("test-actorName")
            .comment("test-comment")
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .fromStatus(WorkflowStatus.PENDING)
            .toStatus(WorkflowStatus.PENDING)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Workflow.WorkflowAction dto = Workflow.WorkflowAction.builder()
                        .actionType("test-actionType")
            .actorId("test-actorId")
            .actorName("test-actorName")
            .comment("test-comment")
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .fromStatus(WorkflowStatus.PENDING)
            .toStatus(WorkflowStatus.PENDING)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}