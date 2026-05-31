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
class LeaveCommand_UpdateBalanceCommandTest {

        @Test
    void testBuilder() {
        LeaveCommand.UpdateBalanceCommand dto = LeaveCommand.UpdateBalanceCommand.builder()
                        .tenantId("test-tenantId")
            .balanceId("test-balanceId")
            .totalAllocated(null)
            .carriedForward(null)
            .updatedBy("test-updatedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-balanceId", dto.getBalanceId());
        assertEquals("test-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testSettersAndGetters() {
        LeaveCommand.UpdateBalanceCommand dto = new LeaveCommand.UpdateBalanceCommand();
        dto.setTenantId("val-tenantId");
        dto.setBalanceId("val-balanceId");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-balanceId", dto.getBalanceId());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        LeaveCommand.UpdateBalanceCommand dto1 = LeaveCommand.UpdateBalanceCommand.builder()
                        .tenantId("test-tenantId")
            .balanceId("test-balanceId")
            .totalAllocated(null)
            .carriedForward(null)
            .updatedBy("test-updatedBy")
            .build();
        LeaveCommand.UpdateBalanceCommand dto2 = LeaveCommand.UpdateBalanceCommand.builder()
                        .tenantId("test-tenantId")
            .balanceId("test-balanceId")
            .totalAllocated(null)
            .carriedForward(null)
            .updatedBy("test-updatedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeaveCommand.UpdateBalanceCommand dto = LeaveCommand.UpdateBalanceCommand.builder()
                        .tenantId("test-tenantId")
            .balanceId("test-balanceId")
            .totalAllocated(null)
            .carriedForward(null)
            .updatedBy("test-updatedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}