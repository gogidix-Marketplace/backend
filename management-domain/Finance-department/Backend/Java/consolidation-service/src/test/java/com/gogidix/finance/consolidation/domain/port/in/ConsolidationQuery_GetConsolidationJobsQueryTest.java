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
class ConsolidationQuery_GetConsolidationJobsQueryTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationQuery.GetConsolidationJobsQuery dto = new ConsolidationQuery.GetConsolidationJobsQuery();
        dto.setTenantId("val-tenantId");
        dto.setFromDate(LocalDate.of(2025,6,1));
        dto.setToDate(LocalDate.of(2025,6,1));
        dto.setPage(99);
        dto.setSize(99);
        dto.setSortBy("val-sortBy");
        dto.setSortDirection("val-sortDirection");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getFromDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getToDate());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
        assertEquals("val-sortBy", dto.getSortBy());
        assertEquals("val-sortDirection", dto.getSortDirection());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationQuery.GetConsolidationJobsQuery dto1 = new ConsolidationQuery.GetConsolidationJobsQuery();
        ConsolidationQuery.GetConsolidationJobsQuery dto2 = new ConsolidationQuery.GetConsolidationJobsQuery();
        dto1.setTenantId("test");
        dto1.setStatus(null);
        dto1.setJobType(null);
        dto1.setFromDate(LocalDate.of(2025,1,1));
        dto1.setToDate(LocalDate.of(2025,1,1));
        dto1.setPage(42);
        dto1.setSize(42);
        dto1.setSortBy("test");
        dto1.setSortDirection("test");
        dto2.setTenantId("test");
        dto2.setStatus(null);
        dto2.setJobType(null);
        dto2.setFromDate(LocalDate.of(2025,1,1));
        dto2.setToDate(LocalDate.of(2025,1,1));
        dto2.setPage(42);
        dto2.setSize(42);
        dto2.setSortBy("test");
        dto2.setSortDirection("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationQuery.GetConsolidationJobsQuery dto = new ConsolidationQuery.GetConsolidationJobsQuery();
        dto.setTenantId("test");
        dto.setStatus(null);
        dto.setJobType(null);
        dto.setFromDate(LocalDate.of(2025,1,1));
        dto.setToDate(LocalDate.of(2025,1,1));
        dto.setPage(42);
        dto.setSize(42);
        dto.setSortBy("test");
        dto.setSortDirection("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationQuery.GetConsolidationJobsQuery dto = new ConsolidationQuery.GetConsolidationJobsQuery();
        dto.setTenantId("test");
        dto.setStatus(null);
        dto.setJobType(null);
        dto.setFromDate(LocalDate.of(2025,1,1));
        dto.setToDate(LocalDate.of(2025,1,1));
        dto.setPage(42);
        dto.setSize(42);
        dto.setSortBy("test");
        dto.setSortDirection("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}