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
class ComplianceRuleCommand_DeactivateRuleCommandTest {

        @Test
    void testSettersAndGetters() {
        ComplianceRuleCommand.DeactivateRuleCommand dto = new ComplianceRuleCommand.DeactivateRuleCommand();
        dto.setTenantId("val-tenantId");
        dto.setRuleId("val-ruleId");
        dto.setDeactivatedByUserId("val-deactivatedByUserId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ruleId", dto.getRuleId());
        assertEquals("val-deactivatedByUserId", dto.getDeactivatedByUserId());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceRuleCommand.DeactivateRuleCommand dto1 = new ComplianceRuleCommand.DeactivateRuleCommand();
        ComplianceRuleCommand.DeactivateRuleCommand dto2 = new ComplianceRuleCommand.DeactivateRuleCommand();
        dto1.setTenantId("test");
        dto1.setRuleId("test");
        dto1.setDeactivatedByUserId("test");
        dto2.setTenantId("test");
        dto2.setRuleId("test");
        dto2.setDeactivatedByUserId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceRuleCommand.DeactivateRuleCommand dto = new ComplianceRuleCommand.DeactivateRuleCommand();
        dto.setTenantId("test");
        dto.setRuleId("test");
        dto.setDeactivatedByUserId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceRuleCommand.DeactivateRuleCommand dto = new ComplianceRuleCommand.DeactivateRuleCommand();
        dto.setTenantId("test");
        dto.setRuleId("test");
        dto.setDeactivatedByUserId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}