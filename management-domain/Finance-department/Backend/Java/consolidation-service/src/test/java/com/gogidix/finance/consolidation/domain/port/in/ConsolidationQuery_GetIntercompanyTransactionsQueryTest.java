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
class ConsolidationQuery_GetIntercompanyTransactionsQueryTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationQuery.GetIntercompanyTransactionsQuery dto = new ConsolidationQuery.GetIntercompanyTransactionsQuery();
        dto.setTenantId("val-tenantId");
        dto.setPeriodStart(LocalDate.of(2025,6,1));
        dto.setPeriodEnd(LocalDate.of(2025,6,1));
        dto.setEntity1Id("val-entity1Id");
        dto.setEntity2Id("val-entity2Id");
        dto.setOnlyUneliminated(true);
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodStart());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodEnd());
        assertEquals("val-entity1Id", dto.getEntity1Id());
        assertEquals("val-entity2Id", dto.getEntity2Id());
        assertTrue(dto.getOnlyUneliminated());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationQuery.GetIntercompanyTransactionsQuery dto1 = new ConsolidationQuery.GetIntercompanyTransactionsQuery();
        ConsolidationQuery.GetIntercompanyTransactionsQuery dto2 = new ConsolidationQuery.GetIntercompanyTransactionsQuery();
        dto1.setTenantId("test");
        dto1.setPeriodStart(LocalDate.of(2025,1,1));
        dto1.setPeriodEnd(LocalDate.of(2025,1,1));
        dto1.setEntity1Id("test");
        dto1.setEntity2Id("test");
        dto1.setOnlyUneliminated(true);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setPeriodStart(LocalDate.of(2025,1,1));
        dto2.setPeriodEnd(LocalDate.of(2025,1,1));
        dto2.setEntity1Id("test");
        dto2.setEntity2Id("test");
        dto2.setOnlyUneliminated(true);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationQuery.GetIntercompanyTransactionsQuery dto = new ConsolidationQuery.GetIntercompanyTransactionsQuery();
        dto.setTenantId("test");
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setEntity1Id("test");
        dto.setEntity2Id("test");
        dto.setOnlyUneliminated(true);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationQuery.GetIntercompanyTransactionsQuery dto = new ConsolidationQuery.GetIntercompanyTransactionsQuery();
        dto.setTenantId("test");
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setEntity1Id("test");
        dto.setEntity2Id("test");
        dto.setOnlyUneliminated(true);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}