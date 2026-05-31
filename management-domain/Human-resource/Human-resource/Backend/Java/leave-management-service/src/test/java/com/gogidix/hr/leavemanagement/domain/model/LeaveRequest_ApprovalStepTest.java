package com.gogidix.hr.leavemanagement.domain.model;

import com.gogidix.hr.leavemanagement.domain.enums.ApprovalStatus;
import com.gogidix.hr.leavemanagement.domain.model.LeaveRequest;
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
class LeaveRequest_ApprovalStepTest {

        @Test
    void testBuilder() {
        LeaveRequest.ApprovalStep dto = LeaveRequest.ApprovalStep.builder()
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
        LeaveRequest.ApprovalStep dto = new LeaveRequest.ApprovalStep();
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
        LeaveRequest.ApprovalStep dto1 = LeaveRequest.ApprovalStep.builder()
                        .level(42)
            .approverId("test-approverId")
            .approverName("test-approverName")
            .status(ApprovalStatus.PENDING)
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .comments("test-comments")
            .build();
        LeaveRequest.ApprovalStep dto2 = LeaveRequest.ApprovalStep.builder()
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
        LeaveRequest.ApprovalStep dto = LeaveRequest.ApprovalStep.builder()
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