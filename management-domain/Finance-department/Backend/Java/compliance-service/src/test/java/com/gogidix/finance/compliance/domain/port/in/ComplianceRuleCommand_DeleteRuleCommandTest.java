package com.gogidix.finance.compliance.domain.port.in;

import com.gogidix.finance.compliance.domain.port.in.ComplianceRuleCommand;
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
class ComplianceRuleCommand_DeleteRuleCommandTest {

        @Test
    void testSettersAndGetters() {
        ComplianceRuleCommand.DeleteRuleCommand dto = new ComplianceRuleCommand.DeleteRuleCommand();
        dto.setTenantId("val-tenantId");
        dto.setRuleId("val-ruleId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ruleId", dto.getRuleId());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceRuleCommand.DeleteRuleCommand dto1 = new ComplianceRuleCommand.DeleteRuleCommand();
        ComplianceRuleCommand.DeleteRuleCommand dto2 = new ComplianceRuleCommand.DeleteRuleCommand();
        dto1.setTenantId("test");
        dto1.setRuleId("test");
        dto2.setTenantId("test");
        dto2.setRuleId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceRuleCommand.DeleteRuleCommand dto = new ComplianceRuleCommand.DeleteRuleCommand();
        dto.setTenantId("test");
        dto.setRuleId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceRuleCommand.DeleteRuleCommand dto = new ComplianceRuleCommand.DeleteRuleCommand();
        dto.setTenantId("test");
        dto.setRuleId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}