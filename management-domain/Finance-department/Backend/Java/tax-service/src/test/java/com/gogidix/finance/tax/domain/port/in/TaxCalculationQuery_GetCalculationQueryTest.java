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
class TaxCalculationQuery_GetCalculationQueryTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationQuery.GetCalculationQuery dto = new TaxCalculationQuery.GetCalculationQuery();
        dto.setTenantId("val-tenantId");
        dto.setCalculationId("val-calculationId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-calculationId", dto.getCalculationId());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationQuery.GetCalculationQuery dto1 = new TaxCalculationQuery.GetCalculationQuery();
        TaxCalculationQuery.GetCalculationQuery dto2 = new TaxCalculationQuery.GetCalculationQuery();
        dto1.setTenantId("test");
        dto1.setCalculationId("test");
        dto2.setTenantId("test");
        dto2.setCalculationId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationQuery.GetCalculationQuery dto = new TaxCalculationQuery.GetCalculationQuery();
        dto.setTenantId("test");
        dto.setCalculationId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxCalculationQuery.GetCalculationQuery dto = new TaxCalculationQuery.GetCalculationQuery();
        dto.setTenantId("test");
        dto.setCalculationId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}