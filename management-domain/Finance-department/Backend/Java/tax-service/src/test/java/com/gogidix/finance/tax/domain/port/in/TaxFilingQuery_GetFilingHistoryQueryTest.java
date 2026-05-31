package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.port.in.TaxFilingQuery;
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
class TaxFilingQuery_GetFilingHistoryQueryTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingQuery.GetFilingHistoryQuery dto = new TaxFilingQuery.GetFilingHistoryQuery();
        dto.setTenantId("val-tenantId");
        dto.setJurisdiction("val-jurisdiction");
        dto.setTaxType("val-taxType");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-jurisdiction", dto.getJurisdiction());
        assertEquals("val-taxType", dto.getTaxType());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingQuery.GetFilingHistoryQuery dto1 = new TaxFilingQuery.GetFilingHistoryQuery();
        TaxFilingQuery.GetFilingHistoryQuery dto2 = new TaxFilingQuery.GetFilingHistoryQuery();
        dto1.setTenantId("test");
        dto1.setJurisdiction("test");
        dto1.setTaxType("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
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
        TaxFilingQuery.GetFilingHistoryQuery dto = new TaxFilingQuery.GetFilingHistoryQuery();
        dto.setTenantId("test");
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
        TaxFilingQuery.GetFilingHistoryQuery dto = new TaxFilingQuery.GetFilingHistoryQuery();
        dto.setTenantId("test");
        dto.setJurisdiction("test");
        dto.setTaxType("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}