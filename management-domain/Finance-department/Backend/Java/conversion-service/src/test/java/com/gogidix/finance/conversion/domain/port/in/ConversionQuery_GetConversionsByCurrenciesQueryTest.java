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
class ConversionQuery_GetConversionsByCurrenciesQueryTest {

        @Test
    void testSettersAndGetters() {
        ConversionQuery.GetConversionsByCurrenciesQuery dto = new ConversionQuery.GetConversionsByCurrenciesQuery();
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
        ConversionQuery.GetConversionsByCurrenciesQuery dto1 = new ConversionQuery.GetConversionsByCurrenciesQuery();
        ConversionQuery.GetConversionsByCurrenciesQuery dto2 = new ConversionQuery.GetConversionsByCurrenciesQuery();
        dto1.setTenantId("test");
        dto1.setFromCurrency("test");
        dto1.setToCurrency("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
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
        ConversionQuery.GetConversionsByCurrenciesQuery dto = new ConversionQuery.GetConversionsByCurrenciesQuery();
        dto.setTenantId("test");
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
        ConversionQuery.GetConversionsByCurrenciesQuery dto = new ConversionQuery.GetConversionsByCurrenciesQuery();
        dto.setTenantId("test");
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}