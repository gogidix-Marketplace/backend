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
class TaxFilingQuery_GetFilingSummaryQueryTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingQuery.GetFilingSummaryQuery dto = new TaxFilingQuery.GetFilingSummaryQuery();
        dto.setTenantId("val-tenantId");
        dto.setJurisdiction("val-jurisdiction");
        dto.setTaxType("val-taxType");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-jurisdiction", dto.getJurisdiction());
        assertEquals("val-taxType", dto.getTaxType());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingQuery.GetFilingSummaryQuery dto1 = new TaxFilingQuery.GetFilingSummaryQuery();
        TaxFilingQuery.GetFilingSummaryQuery dto2 = new TaxFilingQuery.GetFilingSummaryQuery();
        dto1.setTenantId("test");
        dto1.setPeriod(null);
        dto1.setJurisdiction("test");
        dto1.setTaxType("test");
        dto2.setTenantId("test");
        dto2.setPeriod(null);
        dto2.setJurisdiction("test");
        dto2.setTaxType("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingQuery.GetFilingSummaryQuery dto = new TaxFilingQuery.GetFilingSummaryQuery();
        dto.setTenantId("test");
        dto.setPeriod(null);
        dto.setJurisdiction("test");
        dto.setTaxType("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingQuery.GetFilingSummaryQuery dto = new TaxFilingQuery.GetFilingSummaryQuery();
        dto.setTenantId("test");
        dto.setPeriod(null);
        dto.setJurisdiction("test");
        dto.setTaxType("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}