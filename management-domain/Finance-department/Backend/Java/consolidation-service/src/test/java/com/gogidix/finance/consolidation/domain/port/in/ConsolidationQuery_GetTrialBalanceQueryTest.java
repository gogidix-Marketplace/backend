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
class ConsolidationQuery_GetTrialBalanceQueryTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationQuery.GetTrialBalanceQuery dto = new ConsolidationQuery.GetTrialBalanceQuery();
        dto.setTenantId("val-tenantId");
        dto.setAsOfDate(LocalDate.of(2025,6,1));
        dto.setBaseCurrency("val-baseCurrency");
        dto.setIncludeIntercompany(true);
        dto.setIncludeEliminations(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getAsOfDate());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertTrue(dto.getIncludeIntercompany());
        assertTrue(dto.getIncludeEliminations());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationQuery.GetTrialBalanceQuery dto1 = new ConsolidationQuery.GetTrialBalanceQuery();
        ConsolidationQuery.GetTrialBalanceQuery dto2 = new ConsolidationQuery.GetTrialBalanceQuery();
        dto1.setTenantId("test");
        dto1.setAsOfDate(LocalDate.of(2025,1,1));
        dto1.setBaseCurrency("test");
        dto1.setSubsidiaryIds(Collections.emptyList());
        dto1.setDepartmentIds(Collections.emptyList());
        dto1.setIncludeIntercompany(true);
        dto1.setIncludeEliminations(true);
        dto2.setTenantId("test");
        dto2.setAsOfDate(LocalDate.of(2025,1,1));
        dto2.setBaseCurrency("test");
        dto2.setSubsidiaryIds(Collections.emptyList());
        dto2.setDepartmentIds(Collections.emptyList());
        dto2.setIncludeIntercompany(true);
        dto2.setIncludeEliminations(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationQuery.GetTrialBalanceQuery dto = new ConsolidationQuery.GetTrialBalanceQuery();
        dto.setTenantId("test");
        dto.setAsOfDate(LocalDate.of(2025,1,1));
        dto.setBaseCurrency("test");
        dto.setSubsidiaryIds(Collections.emptyList());
        dto.setDepartmentIds(Collections.emptyList());
        dto.setIncludeIntercompany(true);
        dto.setIncludeEliminations(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationQuery.GetTrialBalanceQuery dto = new ConsolidationQuery.GetTrialBalanceQuery();
        dto.setTenantId("test");
        dto.setAsOfDate(LocalDate.of(2025,1,1));
        dto.setBaseCurrency("test");
        dto.setSubsidiaryIds(Collections.emptyList());
        dto.setDepartmentIds(Collections.emptyList());
        dto.setIncludeIntercompany(true);
        dto.setIncludeEliminations(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}