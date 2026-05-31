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
class LeaveCommand_RejectLeaveRequestCommandTest {

        @Test
    void testBuilder() {
        LeaveCommand.RejectLeaveRequestCommand dto = LeaveCommand.RejectLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .approverId("test-approverId")
            .reason("test-reason")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-requestId", dto.getRequestId());
        assertEquals("test-approverId", dto.getApproverId());
        assertEquals("test-reason", dto.getReason());
    }

    @Test
    void testSettersAndGetters() {
        LeaveCommand.RejectLeaveRequestCommand dto = new LeaveCommand.RejectLeaveRequestCommand();
        dto.setTenantId("val-tenantId");
        dto.setRequestId("val-requestId");
        dto.setApproverId("val-approverId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requestId", dto.getRequestId());
        assertEquals("val-approverId", dto.getApproverId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        LeaveCommand.RejectLeaveRequestCommand dto1 = LeaveCommand.RejectLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .approverId("test-approverId")
            .reason("test-reason")
            .build();
        LeaveCommand.RejectLeaveRequestCommand dto2 = LeaveCommand.RejectLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .approverId("test-approverId")
            .reason("test-reason")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeaveCommand.RejectLeaveRequestCommand dto = LeaveCommand.RejectLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .approverId("test-approverId")
            .reason("test-reason")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}