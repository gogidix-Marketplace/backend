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
class ConsolidationQuery_GetConsolidatedBalancesQueryTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationQuery.GetConsolidatedBalancesQuery dto = new ConsolidationQuery.GetConsolidatedBalancesQuery();
        dto.setTenantId("val-tenantId");
        dto.setJobId("val-jobId");
        dto.setAsOfDate(LocalDate.of(2025,6,1));
        dto.setAccountCode("val-accountCode");
        dto.setSubsidiaryId("val-subsidiaryId");
        dto.setDepartmentId("val-departmentId");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-jobId", dto.getJobId());
        assertEquals(LocalDate.of(2025,6,1), dto.getAsOfDate());
        assertEquals("val-accountCode", dto.getAccountCode());
        assertEquals("val-subsidiaryId", dto.getSubsidiaryId());
        assertEquals("val-departmentId", dto.getDepartmentId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationQuery.GetConsolidatedBalancesQuery dto1 = new ConsolidationQuery.GetConsolidatedBalancesQuery();
        ConsolidationQuery.GetConsolidatedBalancesQuery dto2 = new ConsolidationQuery.GetConsolidatedBalancesQuery();
        dto1.setTenantId("test");
        dto1.setJobId("test");
        dto1.setAsOfDate(LocalDate.of(2025,1,1));
        dto1.setAccountCode("test");
        dto1.setSubsidiaryId("test");
        dto1.setDepartmentId("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setJobId("test");
        dto2.setAsOfDate(LocalDate.of(2025,1,1));
        dto2.setAccountCode("test");
        dto2.setSubsidiaryId("test");
        dto2.setDepartmentId("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationQuery.GetConsolidatedBalancesQuery dto = new ConsolidationQuery.GetConsolidatedBalancesQuery();
        dto.setTenantId("test");
        dto.setJobId("test");
        dto.setAsOfDate(LocalDate.of(2025,1,1));
        dto.setAccountCode("test");
        dto.setSubsidiaryId("test");
        dto.setDepartmentId("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationQuery.GetConsolidatedBalancesQuery dto = new ConsolidationQuery.GetConsolidatedBalancesQuery();
        dto.setTenantId("test");
        dto.setJobId("test");
        dto.setAsOfDate(LocalDate.of(2025,1,1));
        dto.setAccountCode("test");
        dto.setSubsidiaryId("test");
        dto.setDepartmentId("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}