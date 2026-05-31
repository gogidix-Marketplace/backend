package com.gogidix.finance.conversion.domain.port.in;

import com.gogidix.finance.conversion.domain.port.in.CurrencyConversionCommand;
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
class CurrencyConversionCommand_InvalidateRateCacheCommandTest {

        @Test
    void testSettersAndGetters() {
        CurrencyConversionCommand.InvalidateRateCacheCommand dto = new CurrencyConversionCommand.InvalidateRateCacheCommand();
        dto.setTenantId("val-tenantId");
        dto.setFromCurrency("val-fromCurrency");
        dto.setToCurrency("val-toCurrency");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-fromCurrency", dto.getFromCurrency());
        assertEquals("val-toCurrency", dto.getToCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        CurrencyConversionCommand.InvalidateRateCacheCommand dto1 = new CurrencyConversionCommand.InvalidateRateCacheCommand();
        CurrencyConversionCommand.InvalidateRateCacheCommand dto2 = new CurrencyConversionCommand.InvalidateRateCacheCommand();
        dto1.setTenantId("test");
        dto1.setFromCurrency("test");
        dto1.setToCurrency("test");
        dto2.setTenantId("test");
        dto2.setFromCurrency("test");
        dto2.setToCurrency("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CurrencyConversionCommand.InvalidateRateCacheCommand dto = new CurrencyConversionCommand.InvalidateRateCacheCommand();
        dto.setTenantId("test");
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CurrencyConversionCommand.InvalidateRateCacheCommand dto = new CurrencyConversionCommand.InvalidateRateCacheCommand();
        dto.setTenantId("test");
        dto.setFromCurrency("test");
        dto.setToCurrency("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}