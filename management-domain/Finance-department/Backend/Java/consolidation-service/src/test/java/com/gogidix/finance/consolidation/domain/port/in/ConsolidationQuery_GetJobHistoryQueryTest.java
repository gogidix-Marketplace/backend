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
class ConsolidationQuery_GetJobHistoryQueryTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationQuery.GetJobHistoryQuery dto = new ConsolidationQuery.GetJobHistoryQuery();
        dto.setTenantId("val-tenantId");
        dto.setPeriodStart(LocalDate.of(2025,6,1));
        dto.setPeriodEnd(LocalDate.of(2025,6,1));
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodStart());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodEnd());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationQuery.GetJobHistoryQuery dto1 = new ConsolidationQuery.GetJobHistoryQuery();
        ConsolidationQuery.GetJobHistoryQuery dto2 = new ConsolidationQuery.GetJobHistoryQuery();
        dto1.setTenantId("test");
        dto1.setPeriodStart(LocalDate.of(2025,1,1));
        dto1.setPeriodEnd(LocalDate.of(2025,1,1));
        dto1.setJobType(null);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setPeriodStart(LocalDate.of(2025,1,1));
        dto2.setPeriodEnd(LocalDate.of(2025,1,1));
        dto2.setJobType(null);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationQuery.GetJobHistoryQuery dto = new ConsolidationQuery.GetJobHistoryQuery();
        dto.setTenantId("test");
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setJobType(null);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationQuery.GetJobHistoryQuery dto = new ConsolidationQuery.GetJobHistoryQuery();
        dto.setTenantId("test");
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setJobType(null);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}