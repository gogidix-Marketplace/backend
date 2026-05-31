package com.gogidix.finance.conversion.domain.port.in;

import com.gogidix.finance.conversion.domain.port.in.ConversionQuery;
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
class ConversionQuery_GetConversionRateQueryTest {

        @Test
    void testSettersAndGetters() {
        ConversionQuery.GetConversionRateQuery dto = new ConversionQuery.GetConversionRateQuery();
        dto.setTenantId("val-tenantId");
        dto.setFromCurrency("val-fromCurrency");
        dto.setToCurrency("val-toCurrency");
        dto.setForceRefresh(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-fromCurrency", dto.getFromCurrency());
        assertEquals("val-toCurrency", dto.getToCurrency());
        assertTrue(dto.getForceRefresh());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversionQuery.GetConversionRateQuery dto1 = new ConversionQuery.GetConversionRateQuery();
        ConversionQuery.GetConversionRateQuery dto2 = new ConversionQuery.GetConversionRateQuery();
        dto1.setTenantId("test");
        dto1.setFromCurrency("test");
        dto1.setToCurrency("test");
        dto1.setForceRefresh(true);
        dto2.setTenantId("test");
        dto2.setFromCurrency("test");
        dto2.setToCurrency("test");
        dto2.setForceRefresh(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversionQuery.GetConversionRateQuery dto = new ConversionQuery.GetConversionRateQuery();
        dto.setTenantId("test");
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        dto.setForceRefresh(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversionQuery.GetConversionRateQuery dto = new ConversionQuery.GetConversionRateQuery();
        dto.setTenantId("test");
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        dto.setForceRefresh(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}