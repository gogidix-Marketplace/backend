package com.gogidix.finance.compliance.domain.port.in;

import com.gogidix.finance.compliance.domain.port.in.ComplianceCheckCommand;
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
class ComplianceCheckCommand_WaiveViolationCommandTest {

        @Test
    void testSettersAndGetters() {
        ComplianceCheckCommand.WaiveViolationCommand dto = new ComplianceCheckCommand.WaiveViolationCommand();
        dto.setTenantId("val-tenantId");
        dto.setCheckId("val-checkId");
        dto.setWaivedBy("val-waivedBy");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals("val-waivedBy", dto.getWaivedBy());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceCheckCommand.WaiveViolationCommand dto1 = new ComplianceCheckCommand.WaiveViolationCommand();
        ComplianceCheckCommand.WaiveViolationCommand dto2 = new ComplianceCheckCommand.WaiveViolationCommand();
        dto1.setTenantId("test");
        dto1.setCheckId("test");
        dto1.setWaivedBy("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setCheckId("test");
        dto2.setWaivedBy("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceCheckCommand.WaiveViolationCommand dto = new ComplianceCheckCommand.WaiveViolationCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setWaivedBy("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceCheckCommand.WaiveViolationCommand dto = new ComplianceCheckCommand.WaiveViolationCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setWaivedBy("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}