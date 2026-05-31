package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.port.in.TaxCalculationQuery;
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
class TaxCalculationQuery_GetTaxSummaryQueryTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationQuery.GetTaxSummaryQuery dto = new TaxCalculationQuery.GetTaxSummaryQuery();
        dto.setTenantId("val-tenantId");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setJurisdiction("val-jurisdiction");
        dto.setTaxType("val-taxType");
        dto.setCurrency("val-currency");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals("val-jurisdiction", dto.getJurisdiction());
        assertEquals("val-taxType", dto.getTaxType());
        assertEquals("val-currency", dto.getCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationQuery.GetTaxSummaryQuery dto1 = new TaxCalculationQuery.GetTaxSummaryQuery();
        TaxCalculationQuery.GetTaxSummaryQuery dto2 = new TaxCalculationQuery.GetTaxSummaryQuery();
        dto1.setTenantId("test");
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setJurisdiction("test");
        dto1.setTaxType("test");
        dto1.setCurrency("test");
        dto2.setTenantId("test");
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setJurisdiction("test");
        dto2.setTaxType("test");
        dto2.setCurrency("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationQuery.GetTaxSummaryQuery dto = new TaxCalculationQuery.GetTaxSummaryQuery();
        dto.setTenantId("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setJurisdiction("test");
        dto.setTaxType("test");
        dto.setCurrency("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxCalculationQuery.GetTaxSummaryQuery dto = new TaxCalculationQuery.GetTaxSummaryQuery();
        dto.setTenantId("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setJurisdiction("test");
        dto.setTaxType("test");
        dto.setCurrency("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}