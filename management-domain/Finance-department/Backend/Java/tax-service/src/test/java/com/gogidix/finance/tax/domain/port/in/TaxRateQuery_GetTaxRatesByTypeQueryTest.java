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
class TaxRateQuery_GetTaxRatesByTypeQueryTest {

        @Test
    void testSettersAndGetters() {
        TaxRateQuery.GetTaxRatesByTypeQuery dto = new TaxRateQuery.GetTaxRatesByTypeQuery();
        dto.setTenantId("val-tenantId");
        dto.setTaxType("val-taxType");
        dto.setIncludeInactive(true);
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-taxType", dto.getTaxType());
        assertTrue(dto.getIncludeInactive());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxRateQuery.GetTaxRatesByTypeQuery dto1 = new TaxRateQuery.GetTaxRatesByTypeQuery();
        TaxRateQuery.GetTaxRatesByTypeQuery dto2 = new TaxRateQuery.GetTaxRatesByTypeQuery();
        dto1.setTenantId("test");
        dto1.setTaxType("test");
        dto1.setIncludeInactive(true);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setTaxType("test");
        dto2.setIncludeInactive(true);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxRateQuery.GetTaxRatesByTypeQuery dto = new TaxRateQuery.GetTaxRatesByTypeQuery();
        dto.setTenantId("test");
        dto.setTaxType("test");
        dto.setIncludeInactive(true);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxRateQuery.GetTaxRatesByTypeQuery dto = new TaxRateQuery.GetTaxRatesByTypeQuery();
        dto.setTenantId("test");
        dto.setTaxType("test");
        dto.setIncludeInactive(true);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}