package com.gogidix.corporatecms.application.dto;

import com.gogidix.corporatecms.application.dto.WorkflowDTO;
import com.gogidix.corporatecms.domain.enums.WorkflowStatus;
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
class WorkflowDTOTest {

        @Test
    void testBuilder() {
        WorkflowDTO dto = WorkflowDTO.builder()
                        .id("test-id")
            .contentId("test-contentId")
            .contentType("test-contentType")
            .contentTitle("test-contentTitle")
            .status(WorkflowStatus.PENDING)
            .requestedBy("test-requestedBy")
            .requestedByName("test-requestedByName")
            .requestComment("test-requestComment")
            .currentApproverId("test-currentApproverId")
            .currentApproverName("test-currentApproverName")
            .steps(Collections.emptyList())
            .actions(Collections.emptyList())
            .currentStepIndex(42)
            .metadata(Collections.emptyMap())
            .dueDate(LocalDateTime.of(2025,1,15,10,0))
            .startedAt(LocalDateTime.of(2025,1,15,10,0))
            .completedAt(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-contentId", dto.getContentId());
        assertEquals("test-contentType", dto.getContentType());
        assertEquals("test-contentTitle", dto.getContentTitle());
        assertEquals("test-requestedBy", dto.getRequestedBy());
        assertEquals("test-requestedByName", dto.getRequestedByName());
        assertEquals("test-requestComment", dto.getRequestComment());
        assertEquals("test-currentApproverId", dto.getCurrentApproverId());
        assertEquals("test-currentApproverName", dto.getCurrentApproverName());
        assertEquals(42, dto.getCurrentStepIndex());
    }

    @Test
    void testSettersAndGetters() {
        WorkflowDTO dto = new WorkflowDTO();
        dto.setId("val-id");
        dto.setContentId("val-contentId");
        dto.setContentType("val-contentType");
        dto.setContentTitle("val-contentTitle");
        dto.setRequestedBy("val-requestedBy");
        dto.setRequestedByName("val-requestedByName");
        dto.setRequestComment("val-requestComment");
        dto.setCurrentApproverId("val-currentApproverId");
        dto.setCurrentApproverName("val-currentApproverName");
        dto.setCurrentStepIndex(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-contentId", dto.getContentId());
        assertEquals("val-contentType", dto.getContentType());
        assertEquals("val-contentTitle", dto.getContentTitle());
        assertEquals("val-requestedBy", dto.getRequestedBy());
        assertEquals("val-requestedByName", dto.getRequestedByName());
        assertEquals("val-requestComment", dto.getRequestComment());
        assertEquals("val-currentApproverId", dto.getCurrentApproverId());
        assertEquals("val-currentApproverName", dto.getCurrentApproverName());
        assertEquals(99, dto.getCurrentStepIndex());
    }

    @Test
    void testEqualsAndHashCode() {
        WorkflowDTO dto1 = WorkflowDTO.builder()
                        .id("test-id")
            .contentId("test-contentId")
            .contentType("test-contentType")
            .contentTitle("test-contentTitle")
            .status(WorkflowStatus.PENDING)
            .requestedBy("test-requestedBy")
            .requestedByName("test-requestedByName")
            .requestComment("test-requestComment")
            .currentApproverId("test-currentApproverId")
            .currentApproverName("test-currentApproverName")
            .steps(Collections.emptyList())
            .actions(Collections.emptyList())
            .currentStepIndex(42)
            .metadata(Collections.emptyMap())
            .dueDate(LocalDateTime.of(2025,1,15,10,0))
            .startedAt(LocalDateTime.of(2025,1,15,10,0))
            .completedAt(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        WorkflowDTO dto2 = WorkflowDTO.builder()
                        .id("test-id")
            .contentId("test-contentId")
            .contentType("test-contentType")
            .contentTitle("test-contentTitle")
            .status(WorkflowStatus.PENDING)
            .requestedBy("test-requestedBy")
            .requestedByName("test-requestedByName")
            .requestComment("test-requestComment")
            .currentApproverId("test-currentApproverId")
            .currentApproverName("test-currentApproverName")
            .steps(Collections.emptyList())
            .actions(Collections.emptyList())
            .currentStepIndex(42)
            .metadata(Collections.emptyMap())
            .dueDate(LocalDateTime.of(2025,1,15,10,0))
            .startedAt(LocalDateTime.of(2025,1,15,10,0))
            .completedAt(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        WorkflowDTO dto = WorkflowDTO.builder()
                        .id("test-id")
            .contentId("test-contentId")
            .contentType("test-contentType")
            .contentTitle("test-contentTitle")
            .status(WorkflowStatus.PENDING)
            .requestedBy("test-requestedBy")
            .requestedByName("test-requestedByName")
            .requestComment("test-requestComment")
            .currentApproverId("test-currentApproverId")
            .currentApproverName("test-currentApproverName")
            .steps(Collections.emptyList())
            .actions(Collections.emptyList())
            .currentStepIndex(42)
            .metadata(Collections.emptyMap())
            .dueDate(LocalDateTime.of(2025,1,15,10,0))
            .startedAt(LocalDateTime.of(2025,1,15,10,0))
            .completedAt(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}