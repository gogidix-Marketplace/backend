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
class ComplianceCheckCommand_CompleteRemediationCommandTest {

        @Test
    void testSettersAndGetters() {
        ComplianceCheckCommand.CompleteRemediationCommand dto = new ComplianceCheckCommand.CompleteRemediationCommand();
        dto.setTenantId("val-tenantId");
        dto.setCheckId("val-checkId");
        dto.setCompletedBy("val-completedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals("val-completedBy", dto.getCompletedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceCheckCommand.CompleteRemediationCommand dto1 = new ComplianceCheckCommand.CompleteRemediationCommand();
        ComplianceCheckCommand.CompleteRemediationCommand dto2 = new ComplianceCheckCommand.CompleteRemediationCommand();
        dto1.setTenantId("test");
        dto1.setCheckId("test");
        dto1.setCompletedBy("test");
        dto2.setTenantId("test");
        dto2.setCheckId("test");
        dto2.setCompletedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceCheckCommand.CompleteRemediationCommand dto = new ComplianceCheckCommand.CompleteRemediationCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setCompletedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceCheckCommand.CompleteRemediationCommand dto = new ComplianceCheckCommand.CompleteRemediationCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setCompletedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}