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
class LeaveCommand_CancelLeaveRequestCommandTest {

        @Test
    void testBuilder() {
        LeaveCommand.CancelLeaveRequestCommand dto = LeaveCommand.CancelLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .employeeId("test-employeeId")
            .reason("test-reason")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-requestId", dto.getRequestId());
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-reason", dto.getReason());
    }

    @Test
    void testSettersAndGetters() {
        LeaveCommand.CancelLeaveRequestCommand dto = new LeaveCommand.CancelLeaveRequestCommand();
        dto.setTenantId("val-tenantId");
        dto.setRequestId("val-requestId");
        dto.setEmployeeId("val-employeeId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requestId", dto.getRequestId());
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        LeaveCommand.CancelLeaveRequestCommand dto1 = LeaveCommand.CancelLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .employeeId("test-employeeId")
            .reason("test-reason")
            .build();
        LeaveCommand.CancelLeaveRequestCommand dto2 = LeaveCommand.CancelLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .employeeId("test-employeeId")
            .reason("test-reason")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeaveCommand.CancelLeaveRequestCommand dto = LeaveCommand.CancelLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .employeeId("test-employeeId")
            .reason("test-reason")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}