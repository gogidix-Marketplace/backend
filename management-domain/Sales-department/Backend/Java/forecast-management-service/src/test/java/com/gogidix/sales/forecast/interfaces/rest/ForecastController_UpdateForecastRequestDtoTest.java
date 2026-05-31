package com.gogidix.sales.forecast.interfaces.rest;

import com.gogidix.sales.forecast.interfaces.rest.ForecastController;
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
class ForecastController_UpdateForecastRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ForecastController.UpdateForecastRequestDto dto = new ForecastController.UpdateForecastRequestDto();
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setRegion("val-region");
        dto.setTerritory("val-territory");
        dto.setBusinessUnit("val-businessUnit");
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-territory", dto.getTerritory());
        assertEquals("val-businessUnit", dto.getBusinessUnit());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastController.UpdateForecastRequestDto dto1 = new ForecastController.UpdateForecastRequestDto();
        ForecastController.UpdateForecastRequestDto dto2 = new ForecastController.UpdateForecastRequestDto();
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setStartDate(null);
        dto1.setEndDate(null);
        dto1.setRegion("test");
        dto1.setTerritory("test");
        dto1.setBusinessUnit("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setStartDate(null);
        dto2.setEndDate(null);
        dto2.setRegion("test");
        dto2.setTerritory("test");
        dto2.setBusinessUnit("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastController.UpdateForecastRequestDto dto = new ForecastController.UpdateForecastRequestDto();
        dto.setName("test");
        dto.setDescription("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setRegion("test");
        dto.setTerritory("test");
        dto.setBusinessUnit("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastController.UpdateForecastRequestDto dto = new ForecastController.UpdateForecastRequestDto();
        dto.setName("test");
        dto.setDescription("test");
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setRegion("test");
        dto.setTerritory("test");
        dto.setBusinessUnit("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}