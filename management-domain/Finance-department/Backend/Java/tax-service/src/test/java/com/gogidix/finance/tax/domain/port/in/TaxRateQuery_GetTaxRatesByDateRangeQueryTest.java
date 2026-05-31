package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.port.in.TaxRateQuery;
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
class TaxRateQuery_GetTaxRatesByDateRangeQueryTest {

        @Test
    void testSettersAndGetters() {
        TaxRateQuery.GetTaxRatesByDateRangeQuery dto = new TaxRateQuery.GetTaxRatesByDateRangeQuery();
        dto.setTenantId("val-tenantId");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setJurisdiction("val-jurisdiction");
        dto.setTaxType("val-taxType");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals("val-jurisdiction", dto.getJurisdiction());
        assertEquals("val-taxType", dto.getTaxType());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxRateQuery.GetTaxRatesByDateRangeQuery dto1 = new TaxRateQuery.GetTaxRatesByDateRangeQuery();
        TaxRateQuery.GetTaxRatesByDateRangeQuery dto2 = new TaxRateQuery.GetTaxRatesByDateRangeQuery();
        dto1.setTenantId("test");
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setJurisdiction("test");
        dto1.setTaxType("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setJurisdiction("test");
        dto2.setTaxType("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxRateQuery.GetTaxRatesByDateRangeQuery dto = new TaxRateQuery.GetTaxRatesByDateRangeQuery();
        dto.setTenantId("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setJurisdiction("test");
        dto.setTaxType("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxRateQuery.GetTaxRatesByDateRangeQuery dto = new TaxRateQuery.GetTaxRatesByDateRangeQuery();
        dto.setTenantId("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setJurisdiction("test");
        dto.setTaxType("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}