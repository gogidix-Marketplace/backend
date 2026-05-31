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
class LeaveApprovalFlow_ApprovalHistoryTest {

        @Test
    void testBuilder() {
        LeaveApprovalFlow.ApprovalHistory dto = LeaveApprovalFlow.ApprovalHistory.builder()
                        .level(42)
            .approverId("test-approverId")
            .approverName("test-approverName")
            .status(ApprovalStatus.PENDING)
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .comments("test-comments")
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getLevel());
        assertEquals("test-approverId", dto.getApproverId());
        assertEquals("test-approverName", dto.getApproverName());
        assertEquals("test-comments", dto.getComments());
    }

    @Test
    void testSettersAndGetters() {
        LeaveApprovalFlow.ApprovalHistory dto = new LeaveApprovalFlow.ApprovalHistory();
        dto.setLevel(99);
        dto.setApproverId("val-approverId");
        dto.setApproverName("val-approverName");
        dto.setComments("val-comments");
        assertEquals(99, dto.getLevel());
        assertEquals("val-approverId", dto.getApproverId());
        assertEquals("val-approverName", dto.getApproverName());
        assertEquals("val-comments", dto.getComments());
    }

    @Test
    void testEqualsAndHashCode() {
        LeaveApprovalFlow.ApprovalHistory dto1 = LeaveApprovalFlow.ApprovalHistory.builder()
                        .level(42)
            .approverId("test-approverId")
            .approverName("test-approverName")
            .status(ApprovalStatus.PENDING)
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .comments("test-comments")
            .build();
        LeaveApprovalFlow.ApprovalHistory dto2 = LeaveApprovalFlow.ApprovalHistory.builder()
                        .level(42)
            .approverId("test-approverId")
            .approverName("test-approverName")
            .status(ApprovalStatus.PENDING)
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .comments("test-comments")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeaveApprovalFlow.ApprovalHistory dto = LeaveApprovalFlow.ApprovalHistory.builder()
                        .level(42)
            .approverId("test-approverId")
            .approverName("test-approverName")
            .status(ApprovalStatus.PENDING)
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .comments("test-comments")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}