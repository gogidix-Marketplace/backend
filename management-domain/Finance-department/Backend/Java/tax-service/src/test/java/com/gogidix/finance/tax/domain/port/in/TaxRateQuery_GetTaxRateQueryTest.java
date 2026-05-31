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
class TaxRateQuery_GetTaxRateQueryTest {

        @Test
    void testSettersAndGetters() {
        TaxRateQuery.GetTaxRateQuery dto = new TaxRateQuery.GetTaxRateQuery();
        dto.setTenantId("val-tenantId");
        dto.setTaxRateId("val-taxRateId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-taxRateId", dto.getTaxRateId());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxRateQuery.GetTaxRateQuery dto1 = new TaxRateQuery.GetTaxRateQuery();
        TaxRateQuery.GetTaxRateQuery dto2 = new TaxRateQuery.GetTaxRateQuery();
        dto1.setTenantId("test");
        dto1.setTaxRateId("test");
        dto2.setTenantId("test");
        dto2.setTaxRateId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxRateQuery.GetTaxRateQuery dto = new TaxRateQuery.GetTaxRateQuery();
        dto.setTenantId("test");
        dto.setTaxRateId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxRateQuery.GetTaxRateQuery dto = new TaxRateQuery.GetTaxRateQuery();
        dto.setTenantId("test");
        dto.setTaxRateId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}