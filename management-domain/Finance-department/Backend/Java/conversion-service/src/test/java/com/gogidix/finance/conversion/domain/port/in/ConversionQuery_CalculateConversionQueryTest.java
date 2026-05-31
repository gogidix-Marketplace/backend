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
class ConversionQuery_CalculateConversionQueryTest {

        @Test
    void testSettersAndGetters() {
        ConversionQuery.CalculateConversionQuery dto = new ConversionQuery.CalculateConversionQuery();
        dto.setTenantId("val-tenantId");
        dto.setAmount(BigDecimal.ONE);
        dto.setFromCurrency("val-fromCurrency");
        dto.setToCurrency("val-toCurrency");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-fromCurrency", dto.getFromCurrency());
        assertEquals("val-toCurrency", dto.getToCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversionQuery.CalculateConversionQuery dto1 = new ConversionQuery.CalculateConversionQuery();
        ConversionQuery.CalculateConversionQuery dto2 = new ConversionQuery.CalculateConversionQuery();
        dto1.setTenantId("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setFromCurrency("test");
        dto1.setToCurrency("test");
        dto2.setTenantId("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setFromCurrency("test");
        dto2.setToCurrency("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversionQuery.CalculateConversionQuery dto = new ConversionQuery.CalculateConversionQuery();
        dto.setTenantId("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversionQuery.CalculateConversionQuery dto = new ConversionQuery.CalculateConversionQuery();
        dto.setTenantId("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}