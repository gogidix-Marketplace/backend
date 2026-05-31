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
class ConversionQuery_GetSupportedCurrenciesQueryTest {

        @Test
    void testSettersAndGetters() {
        ConversionQuery.GetSupportedCurrenciesQuery dto = new ConversionQuery.GetSupportedCurrenciesQuery();
        dto.setTenantId("val-tenantId");
        assertEquals("val-tenantId", dto.getTenantId());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversionQuery.GetSupportedCurrenciesQuery dto1 = new ConversionQuery.GetSupportedCurrenciesQuery();
        ConversionQuery.GetSupportedCurrenciesQuery dto2 = new ConversionQuery.GetSupportedCurrenciesQuery();
        dto1.setTenantId("test");
        dto2.setTenantId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversionQuery.GetSupportedCurrenciesQuery dto = new ConversionQuery.GetSupportedCurrenciesQuery();
        dto.setTenantId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversionQuery.GetSupportedCurrenciesQuery dto = new ConversionQuery.GetSupportedCurrenciesQuery();
        dto.setTenantId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}