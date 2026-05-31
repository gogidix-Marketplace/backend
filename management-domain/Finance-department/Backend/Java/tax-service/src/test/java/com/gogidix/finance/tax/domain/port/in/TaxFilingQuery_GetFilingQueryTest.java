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
class TaxFilingQuery_GetFilingQueryTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingQuery.GetFilingQuery dto = new TaxFilingQuery.GetFilingQuery();
        dto.setTenantId("val-tenantId");
        dto.setFilingId("val-filingId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-filingId", dto.getFilingId());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingQuery.GetFilingQuery dto1 = new TaxFilingQuery.GetFilingQuery();
        TaxFilingQuery.GetFilingQuery dto2 = new TaxFilingQuery.GetFilingQuery();
        dto1.setTenantId("test");
        dto1.setFilingId("test");
        dto2.setTenantId("test");
        dto2.setFilingId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingQuery.GetFilingQuery dto = new TaxFilingQuery.GetFilingQuery();
        dto.setTenantId("test");
        dto.setFilingId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingQuery.GetFilingQuery dto = new TaxFilingQuery.GetFilingQuery();
        dto.setTenantId("test");
        dto.setFilingId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}