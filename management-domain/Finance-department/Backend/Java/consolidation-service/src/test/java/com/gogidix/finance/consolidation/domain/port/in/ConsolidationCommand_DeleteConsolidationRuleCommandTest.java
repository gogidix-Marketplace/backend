package com.gogidix.finance.consolidation.domain.port.in;

import com.gogidix.finance.consolidation.domain.port.in.ConsolidationCommand;
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
class ConsolidationCommand_DeleteConsolidationRuleCommandTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationCommand.DeleteConsolidationRuleCommand dto = new ConsolidationCommand.DeleteConsolidationRuleCommand();
        dto.setTenantId("val-tenantId");
        dto.setRuleId("val-ruleId");
        dto.setDeletedBy("val-deletedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ruleId", dto.getRuleId());
        assertEquals("val-deletedBy", dto.getDeletedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationCommand.DeleteConsolidationRuleCommand dto1 = new ConsolidationCommand.DeleteConsolidationRuleCommand();
        ConsolidationCommand.DeleteConsolidationRuleCommand dto2 = new ConsolidationCommand.DeleteConsolidationRuleCommand();
        dto1.setTenantId("test");
        dto1.setRuleId("test");
        dto1.setDeletedBy("test");
        dto2.setTenantId("test");
        dto2.setRuleId("test");
        dto2.setDeletedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationCommand.DeleteConsolidationRuleCommand dto = new ConsolidationCommand.DeleteConsolidationRuleCommand();
        dto.setTenantId("test");
        dto.setRuleId("test");
        dto.setDeletedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationCommand.DeleteConsolidationRuleCommand dto = new ConsolidationCommand.DeleteConsolidationRuleCommand();
        dto.setTenantId("test");
        dto.setRuleId("test");
        dto.setDeletedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}