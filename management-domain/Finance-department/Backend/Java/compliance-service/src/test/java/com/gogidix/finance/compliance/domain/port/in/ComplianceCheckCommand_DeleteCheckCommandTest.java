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
class ComplianceCheckCommand_DeleteCheckCommandTest {

        @Test
    void testSettersAndGetters() {
        ComplianceCheckCommand.DeleteCheckCommand dto = new ComplianceCheckCommand.DeleteCheckCommand();
        dto.setTenantId("val-tenantId");
        dto.setCheckId("val-checkId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-checkId", dto.getCheckId());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceCheckCommand.DeleteCheckCommand dto1 = new ComplianceCheckCommand.DeleteCheckCommand();
        ComplianceCheckCommand.DeleteCheckCommand dto2 = new ComplianceCheckCommand.DeleteCheckCommand();
        dto1.setTenantId("test");
        dto1.setCheckId("test");
        dto2.setTenantId("test");
        dto2.setCheckId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceCheckCommand.DeleteCheckCommand dto = new ComplianceCheckCommand.DeleteCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceCheckCommand.DeleteCheckCommand dto = new ComplianceCheckCommand.DeleteCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}