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
class ComplianceCheckCommand_AssignRemediationCommandTest {

        @Test
    void testSettersAndGetters() {
        ComplianceCheckCommand.AssignRemediationCommand dto = new ComplianceCheckCommand.AssignRemediationCommand();
        dto.setTenantId("val-tenantId");
        dto.setCheckId("val-checkId");
        dto.setAssignedTo("val-assignedTo");
        dto.setAction("val-action");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals("val-action", dto.getAction());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceCheckCommand.AssignRemediationCommand dto1 = new ComplianceCheckCommand.AssignRemediationCommand();
        ComplianceCheckCommand.AssignRemediationCommand dto2 = new ComplianceCheckCommand.AssignRemediationCommand();
        dto1.setTenantId("test");
        dto1.setCheckId("test");
        dto1.setAssignedTo("test");
        dto1.setAction("test");
        dto1.setDueDate(null);
        dto2.setTenantId("test");
        dto2.setCheckId("test");
        dto2.setAssignedTo("test");
        dto2.setAction("test");
        dto2.setDueDate(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceCheckCommand.AssignRemediationCommand dto = new ComplianceCheckCommand.AssignRemediationCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setAssignedTo("test");
        dto.setAction("test");
        dto.setDueDate(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceCheckCommand.AssignRemediationCommand dto = new ComplianceCheckCommand.AssignRemediationCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setAssignedTo("test");
        dto.setAction("test");
        dto.setDueDate(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}