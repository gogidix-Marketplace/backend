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
class ConversionQuery_GetHistoricalRatesQueryTest {

        @Test
    void testSettersAndGetters() {
        ConversionQuery.GetHistoricalRatesQuery dto = new ConversionQuery.GetHistoricalRatesQuery();
        dto.setTenantId("val-tenantId");
        dto.setFromCurrency("val-fromCurrency");
        dto.setToCurrency("val-toCurrency");
        dto.setProvider("val-provider");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-fromCurrency", dto.getFromCurrency());
        assertEquals("val-toCurrency", dto.getToCurrency());
        assertEquals("val-provider", dto.getProvider());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversionQuery.GetHistoricalRatesQuery dto1 = new ConversionQuery.GetHistoricalRatesQuery();
        ConversionQuery.GetHistoricalRatesQuery dto2 = new ConversionQuery.GetHistoricalRatesQuery();
        dto1.setTenantId("test");
        dto1.setFromCurrency("test");
        dto1.setToCurrency("test");
        dto1.setStartDate(null);
        dto1.setEndDate(null);
        dto1.setProvider("test");
        dto2.setTenantId("test");
        dto2.setFromCurrency("test");
        dto2.setToCurrency("test");
        dto2.setStartDate(null);
        dto2.setEndDate(null);
        dto2.setProvider("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversionQuery.GetHistoricalRatesQuery dto = new ConversionQuery.GetHistoricalRatesQuery();
        dto.setTenantId("test");
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setProvider("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversionQuery.GetHistoricalRatesQuery dto = new ConversionQuery.GetHistoricalRatesQuery();
        dto.setTenantId("test");
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setProvider("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}