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
class ConsolidationQuery_GetActiveConsolidationRulesQueryTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationQuery.GetActiveConsolidationRulesQuery dto = new ConsolidationQuery.GetActiveConsolidationRulesQuery();
        dto.setTenantId("val-tenantId");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationQuery.GetActiveConsolidationRulesQuery dto1 = new ConsolidationQuery.GetActiveConsolidationRulesQuery();
        ConsolidationQuery.GetActiveConsolidationRulesQuery dto2 = new ConsolidationQuery.GetActiveConsolidationRulesQuery();
        dto1.setTenantId("test");
        dto1.setEffectiveDate(LocalDate.of(2025,1,1));
        dto1.setRuleType(null);
        dto2.setTenantId("test");
        dto2.setEffectiveDate(LocalDate.of(2025,1,1));
        dto2.setRuleType(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationQuery.GetActiveConsolidationRulesQuery dto = new ConsolidationQuery.GetActiveConsolidationRulesQuery();
        dto.setTenantId("test");
        dto.setEffectiveDate(LocalDate.of(2025,1,1));
        dto.setRuleType(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationQuery.GetActiveConsolidationRulesQuery dto = new ConsolidationQuery.GetActiveConsolidationRulesQuery();
        dto.setTenantId("test");
        dto.setEffectiveDate(LocalDate.of(2025,1,1));
        dto.setRuleType(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}