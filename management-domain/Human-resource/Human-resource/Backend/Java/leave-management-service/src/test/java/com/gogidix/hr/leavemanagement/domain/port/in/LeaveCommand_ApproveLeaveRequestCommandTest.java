package com.gogidix.hr.leavemanagement.domain.port.in;

import com.gogidix.hr.leavemanagement.domain.port.in.LeaveCommand;
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
class LeaveCommand_ApproveLeaveRequestCommandTest {

        @Test
    void testBuilder() {
        LeaveCommand.ApproveLeaveRequestCommand dto = LeaveCommand.ApproveLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .approverId("test-approverId")
            .approverName("test-approverName")
            .comments("test-comments")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-requestId", dto.getRequestId());
        assertEquals("test-approverId", dto.getApproverId());
        assertEquals("test-approverName", dto.getApproverName());
        assertEquals("test-comments", dto.getComments());
    }

    @Test
    void testSettersAndGetters() {
        LeaveCommand.ApproveLeaveRequestCommand dto = new LeaveCommand.ApproveLeaveRequestCommand();
        dto.setTenantId("val-tenantId");
        dto.setRequestId("val-requestId");
        dto.setApproverId("val-approverId");
        dto.setApproverName("val-approverName");
        dto.setComments("val-comments");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requestId", dto.getRequestId());
        assertEquals("val-approverId", dto.getApproverId());
        assertEquals("val-approverName", dto.getApproverName());
        assertEquals("val-comments", dto.getComments());
    }

    @Test
    void testEqualsAndHashCode() {
        LeaveCommand.ApproveLeaveRequestCommand dto1 = LeaveCommand.ApproveLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .approverId("test-approverId")
            .approverName("test-approverName")
            .comments("test-comments")
            .build();
        LeaveCommand.ApproveLeaveRequestCommand dto2 = LeaveCommand.ApproveLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .approverId("test-approverId")
            .approverName("test-approverName")
            .comments("test-comments")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeaveCommand.ApproveLeaveRequestCommand dto = LeaveCommand.ApproveLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .approverId("test-approverId")
            .approverName("test-approverName")
            .comments("test-comments")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}