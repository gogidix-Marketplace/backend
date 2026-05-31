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
class CurrencyConversionCommand_UpdateRateThresholdsCommandTest {

        @Test
    void testSettersAndGetters() {
        CurrencyConversionCommand.UpdateRateThresholdsCommand dto = new CurrencyConversionCommand.UpdateRateThresholdsCommand();
        dto.setTenantId("val-tenantId");
        dto.setRateChangeThreshold(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(BigDecimal.ONE, dto.getRateChangeThreshold());
    }

    @Test
    void testEqualsAndHashCode() {
        CurrencyConversionCommand.UpdateRateThresholdsCommand dto1 = new CurrencyConversionCommand.UpdateRateThresholdsCommand();
        CurrencyConversionCommand.UpdateRateThresholdsCommand dto2 = new CurrencyConversionCommand.UpdateRateThresholdsCommand();
        dto1.setTenantId("test");
        dto1.setRateChangeThreshold(BigDecimal.TEN);
        dto1.setCacheTtlSeconds(42L);
        dto2.setTenantId("test");
        dto2.setRateChangeThreshold(BigDecimal.TEN);
        dto2.setCacheTtlSeconds(42L);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CurrencyConversionCommand.UpdateRateThresholdsCommand dto = new CurrencyConversionCommand.UpdateRateThresholdsCommand();
        dto.setTenantId("test");
        dto.setRateChangeThreshold(BigDecimal.TEN);
        dto.setCacheTtlSeconds(42L);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CurrencyConversionCommand.UpdateRateThresholdsCommand dto = new CurrencyConversionCommand.UpdateRateThresholdsCommand();
        dto.setTenantId("test");
        dto.setRateChangeThreshold(BigDecimal.TEN);
        dto.setCacheTtlSeconds(42L);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}