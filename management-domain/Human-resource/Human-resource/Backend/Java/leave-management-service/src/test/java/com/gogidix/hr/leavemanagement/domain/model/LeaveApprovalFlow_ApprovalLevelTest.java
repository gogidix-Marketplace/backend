package com.gogidix.hr.leavemanagement.domain.model;

import com.gogidix.hr.leavemanagement.domain.enums.ApprovalStatus;
import com.gogidix.hr.leavemanagement.domain.model.LeaveApprovalFlow;
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
class LeaveApprovalFlow_ApprovalLevelTest {

        @Test
    void testBuilder() {
        LeaveApprovalFlow.ApprovalLevel dto = LeaveApprovalFlow.ApprovalLevel.builder()
                        .level(42)
            .approverId("test-approverId")
            .approverName("test-approverName")
            .approverRole("test-approverRole")
            .status(ApprovalStatus.PENDING)
            .required(true)
            .approvedAt(LocalDateTime.of(2025,1,15,10,0))
            .comments("test-comments")
            .skipIfUnavailable(true)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getLevel());
        assertEquals("test-approverId", dto.getApproverId());
        assertEquals("test-approverName", dto.getApproverName());
        assertEquals("test-approverRole", dto.getApproverRole());
        assertTrue(dto.getRequired());
        assertEquals("test-comments", dto.getComments());
        assertTrue(dto.getSkipIfUnavailable());
    }

    @Test
    void testSettersAndGetters() {
        LeaveApprovalFlow.ApprovalLevel dto = new LeaveApprovalFlow.ApprovalLevel();
        dto.setLevel(99);
        dto.setApproverId("val-approverId");
        dto.setApproverName("val-approverName");
        dto.setApproverRole("val-approverRole");
        dto.setRequired(true);
        dto.setComments("val-comments");
        dto.setSkipIfUnavailable(true);
        assertEquals(99, dto.getLevel());
        assertEquals("val-approverId", dto.getApproverId());
        assertEquals("val-approverName", dto.getApproverName());
        assertEquals("val-approverRole", dto.getApproverRole());
        assertTrue(dto.getRequired());
        assertEquals("val-comments", dto.getComments());
        assertTrue(dto.getSkipIfUnavailable());
    }

    @Test
    void testEqualsAndHashCode() {
        LeaveApprovalFlow.ApprovalLevel dto1 = LeaveApprovalFlow.ApprovalLevel.builder()
                        .level(42)
            .approverId("test-approverId")
            .approverName("test-approverName")
            .approverRole("test-approverRole")
            .status(ApprovalStatus.PENDING)
            .required(true)
            .approvedAt(LocalDateTime.of(2025,1,15,10,0))
            .comments("test-comments")
            .skipIfUnavailable(true)
            .build();
        LeaveApprovalFlow.ApprovalLevel dto2 = LeaveApprovalFlow.ApprovalLevel.builder()
                        .level(42)
            .approverId("test-approverId")
            .approverName("test-approverName")
            .approverRole("test-approverRole")
            .status(ApprovalStatus.PENDING)
            .required(true)
            .approvedAt(LocalDateTime.of(2025,1,15,10,0))
            .comments("test-comments")
            .skipIfUnavailable(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeaveApprovalFlow.ApprovalLevel dto = LeaveApprovalFlow.ApprovalLevel.builder()
                        .level(42)
            .approverId("test-approverId")
            .approverName("test-approverName")
            .approverRole("test-approverRole")
            .status(ApprovalStatus.PENDING)
            .required(true)
            .approvedAt(LocalDateTime.of(2025,1,15,10,0))
            .comments("test-comments")
            .skipIfUnavailable(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}