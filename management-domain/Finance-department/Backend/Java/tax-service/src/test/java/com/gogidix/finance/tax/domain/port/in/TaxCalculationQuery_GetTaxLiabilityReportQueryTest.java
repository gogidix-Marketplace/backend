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
class TaxCalculationQuery_GetTaxLiabilityReportQueryTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationQuery.GetTaxLiabilityReportQuery dto = new TaxCalculationQuery.GetTaxLiabilityReportQuery();
        dto.setTenantId("val-tenantId");
        dto.setJurisdiction("val-jurisdiction");
        dto.setTaxType("val-taxType");
        dto.setIncludePending(true);
        dto.setIncludeVerified(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-jurisdiction", dto.getJurisdiction());
        assertEquals("val-taxType", dto.getTaxType());
        assertTrue(dto.getIncludePending());
        assertTrue(dto.getIncludeVerified());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationQuery.GetTaxLiabilityReportQuery dto1 = new TaxCalculationQuery.GetTaxLiabilityReportQuery();
        TaxCalculationQuery.GetTaxLiabilityReportQuery dto2 = new TaxCalculationQuery.GetTaxLiabilityReportQuery();
        dto1.setTenantId("test");
        dto1.setPeriod(null);
        dto1.setJurisdiction("test");
        dto1.setTaxType("test");
        dto1.setIncludePending(true);
        dto1.setIncludeVerified(true);
        dto2.setTenantId("test");
        dto2.setPeriod(null);
        dto2.setJurisdiction("test");
        dto2.setTaxType("test");
        dto2.setIncludePending(true);
        dto2.setIncludeVerified(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationQuery.GetTaxLiabilityReportQuery dto = new TaxCalculationQuery.GetTaxLiabilityReportQuery();
        dto.setTenantId("test");
        dto.setPeriod(null);
        dto.setJurisdiction("test");
        dto.setTaxType("test");
        dto.setIncludePending(true);
        dto.setIncludeVerified(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxCalculationQuery.GetTaxLiabilityReportQuery dto = new TaxCalculationQuery.GetTaxLiabilityReportQuery();
        dto.setTenantId("test");
        dto.setPeriod(null);
        dto.setJurisdiction("test");
        dto.setTaxType("test");
        dto.setIncludePending(true);
        dto.setIncludeVerified(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}