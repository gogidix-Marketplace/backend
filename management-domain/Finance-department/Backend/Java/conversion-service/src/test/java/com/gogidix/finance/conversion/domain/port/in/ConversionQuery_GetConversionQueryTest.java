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
class ConversionQuery_GetConversionQueryTest {

        @Test
    void testSettersAndGetters() {
        ConversionQuery.GetConversionQuery dto = new ConversionQuery.GetConversionQuery();
        dto.setTenantId("val-tenantId");
        dto.setConversionId("val-conversionId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-conversionId", dto.getConversionId());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversionQuery.GetConversionQuery dto1 = new ConversionQuery.GetConversionQuery();
        ConversionQuery.GetConversionQuery dto2 = new ConversionQuery.GetConversionQuery();
        dto1.setTenantId("test");
        dto1.setConversionId("test");
        dto2.setTenantId("test");
        dto2.setConversionId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversionQuery.GetConversionQuery dto = new ConversionQuery.GetConversionQuery();
        dto.setTenantId("test");
        dto.setConversionId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversionQuery.GetConversionQuery dto = new ConversionQuery.GetConversionQuery();
        dto.setTenantId("test");
        dto.setConversionId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}