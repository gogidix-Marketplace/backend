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
class ConversionQuery_GetConversionStatisticsQueryTest {

        @Test
    void testSettersAndGetters() {
        ConversionQuery.GetConversionStatisticsQuery dto = new ConversionQuery.GetConversionStatisticsQuery();
        dto.setTenantId("val-tenantId");
        dto.setFromCurrency("val-fromCurrency");
        dto.setToCurrency("val-toCurrency");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-fromCurrency", dto.getFromCurrency());
        assertEquals("val-toCurrency", dto.getToCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversionQuery.GetConversionStatisticsQuery dto1 = new ConversionQuery.GetConversionStatisticsQuery();
        ConversionQuery.GetConversionStatisticsQuery dto2 = new ConversionQuery.GetConversionStatisticsQuery();
        dto1.setTenantId("test");
        dto1.setStartDate(null);
        dto1.setEndDate(null);
        dto1.setFromCurrency("test");
        dto1.setToCurrency("test");
        dto2.setTenantId("test");
        dto2.setStartDate(null);
        dto2.setEndDate(null);
        dto2.setFromCurrency("test");
        dto2.setToCurrency("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversionQuery.GetConversionStatisticsQuery dto = new ConversionQuery.GetConversionStatisticsQuery();
        dto.setTenantId("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversionQuery.GetConversionStatisticsQuery dto = new ConversionQuery.GetConversionStatisticsQuery();
        dto.setTenantId("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}