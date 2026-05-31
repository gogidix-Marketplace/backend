package com.gogidix.sales.forecast.domain.port.in;

import com.gogidix.sales.forecast.domain.port.in.ForecastQuery;
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
class ForecastQuery_GetForecastsByStatusQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetForecastsByStatusQuery dto = new ForecastQuery.GetForecastsByStatusQuery();
        dto.setStatus("val-status");
        assertEquals("val-status", dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetForecastsByStatusQuery dto1 = new ForecastQuery.GetForecastsByStatusQuery();
        ForecastQuery.GetForecastsByStatusQuery dto2 = new ForecastQuery.GetForecastsByStatusQuery();
        dto1.setStatus("test");
        dto2.setStatus("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setStatus(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetForecastsByStatusQuery dto = new ForecastQuery.GetForecastsByStatusQuery();
        dto.setStatus("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetForecastsByStatusQuery dto = new ForecastQuery.GetForecastsByStatusQuery();
        dto.setStatus("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}