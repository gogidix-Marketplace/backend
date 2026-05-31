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
class LeaveCommand_EncashBalanceCommandTest {

        @Test
    void testBuilder() {
        LeaveCommand.EncashBalanceCommand dto = LeaveCommand.EncashBalanceCommand.builder()
                        .tenantId("test-tenantId")
            .balanceId("test-balanceId")
            .employeeId("test-employeeId")
            .days(null)
            .dailyRate(null)
            .requestedBy("test-requestedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-balanceId", dto.getBalanceId());
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-requestedBy", dto.getRequestedBy());
    }

    @Test
    void testSettersAndGetters() {
        LeaveCommand.EncashBalanceCommand dto = new LeaveCommand.EncashBalanceCommand();
        dto.setTenantId("val-tenantId");
        dto.setBalanceId("val-balanceId");
        dto.setEmployeeId("val-employeeId");
        dto.setRequestedBy("val-requestedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-balanceId", dto.getBalanceId());
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-requestedBy", dto.getRequestedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        LeaveCommand.EncashBalanceCommand dto1 = LeaveCommand.EncashBalanceCommand.builder()
                        .tenantId("test-tenantId")
            .balanceId("test-balanceId")
            .employeeId("test-employeeId")
            .days(null)
            .dailyRate(null)
            .requestedBy("test-requestedBy")
            .build();
        LeaveCommand.EncashBalanceCommand dto2 = LeaveCommand.EncashBalanceCommand.builder()
                        .tenantId("test-tenantId")
            .balanceId("test-balanceId")
            .employeeId("test-employeeId")
            .days(null)
            .dailyRate(null)
            .requestedBy("test-requestedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeaveCommand.EncashBalanceCommand dto = LeaveCommand.EncashBalanceCommand.builder()
                        .tenantId("test-tenantId")
            .balanceId("test-balanceId")
            .employeeId("test-employeeId")
            .days(null)
            .dailyRate(null)
            .requestedBy("test-requestedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}