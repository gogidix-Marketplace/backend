package com.gogidix.finance.consolidation.domain.port.in;

import com.gogidix.finance.consolidation.domain.port.in.ConsolidationQuery;
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
class ConsolidationQuery_GetConsolidationRuleQueryTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationQuery.GetConsolidationRuleQuery dto = new ConsolidationQuery.GetConsolidationRuleQuery();
        dto.setTenantId("val-tenantId");
        dto.setRuleId("val-ruleId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ruleId", dto.getRuleId());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationQuery.GetConsolidationRuleQuery dto1 = new ConsolidationQuery.GetConsolidationRuleQuery();
        ConsolidationQuery.GetConsolidationRuleQuery dto2 = new ConsolidationQuery.GetConsolidationRuleQuery();
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
        ConsolidationQuery.GetConsolidationRuleQuery dto = new ConsolidationQuery.GetConsolidationRuleQuery();
        dto.setTenantId("test");
        dto.setRuleId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationQuery.GetConsolidationRuleQuery dto = new ConsolidationQuery.GetConsolidationRuleQuery();
        dto.setTenantId("test");
        dto.setRuleId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}