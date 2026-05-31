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
class ConversionQuery_GetConversionsByDateRangeQueryTest {

        @Test
    void testSettersAndGetters() {
        ConversionQuery.GetConversionsByDateRangeQuery dto = new ConversionQuery.GetConversionsByDateRangeQuery();
        dto.setTenantId("val-tenantId");
        dto.setFromCurrency("val-fromCurrency");
        dto.setToCurrency("val-toCurrency");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-fromCurrency", dto.getFromCurrency());
        assertEquals("val-toCurrency", dto.getToCurrency());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversionQuery.GetConversionsByDateRangeQuery dto1 = new ConversionQuery.GetConversionsByDateRangeQuery();
        ConversionQuery.GetConversionsByDateRangeQuery dto2 = new ConversionQuery.GetConversionsByDateRangeQuery();
        dto1.setTenantId("test");
        dto1.setStartDate(null);
        dto1.setEndDate(null);
        dto1.setFromCurrency("test");
        dto1.setToCurrency("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setStartDate(null);
        dto2.setEndDate(null);
        dto2.setFromCurrency("test");
        dto2.setToCurrency("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversionQuery.GetConversionsByDateRangeQuery dto = new ConversionQuery.GetConversionsByDateRangeQuery();
        dto.setTenantId("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversionQuery.GetConversionsByDateRangeQuery dto = new ConversionQuery.GetConversionsByDateRangeQuery();
        dto.setTenantId("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}