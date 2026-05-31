package com.gogidix.sales.forecast.interfaces.rest;

import com.gogidix.sales.forecast.domain.model.ForecastLineItem;
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
class ForecastController_UpdateLineItemRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ForecastController.UpdateLineItemRequestDto dto = new ForecastController.UpdateLineItemRequestDto();
        dto.setName("val-name");
        dto.setBestCase(BigDecimal.ONE);
        dto.setLikely(BigDecimal.ONE);
        dto.setWorstCase(BigDecimal.ONE);
        dto.setNotes("val-notes");
        assertEquals("val-name", dto.getName());
        assertEquals(BigDecimal.ONE, dto.getBestCase());
        assertEquals(BigDecimal.ONE, dto.getLikely());
        assertEquals(BigDecimal.ONE, dto.getWorstCase());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastController.UpdateLineItemRequestDto dto1 = new ForecastController.UpdateLineItemRequestDto();
        ForecastController.UpdateLineItemRequestDto dto2 = new ForecastController.UpdateLineItemRequestDto();
        dto1.setName("test");
        dto1.setCategory(ForecastLineItem.ForecastCategory.BEST_CASE);
        dto1.setBestCase(BigDecimal.TEN);
        dto1.setLikely(BigDecimal.TEN);
        dto1.setWorstCase(BigDecimal.TEN);
        dto1.setNotes("test");
        dto2.setName("test");
        dto2.setCategory(ForecastLineItem.ForecastCategory.BEST_CASE);
        dto2.setBestCase(BigDecimal.TEN);
        dto2.setLikely(BigDecimal.TEN);
        dto2.setWorstCase(BigDecimal.TEN);
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastController.UpdateLineItemRequestDto dto = new ForecastController.UpdateLineItemRequestDto();
        dto.setName("test");
        dto.setCategory(ForecastLineItem.ForecastCategory.BEST_CASE);
        dto.setBestCase(BigDecimal.TEN);
        dto.setLikely(BigDecimal.TEN);
        dto.setWorstCase(BigDecimal.TEN);
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastController.UpdateLineItemRequestDto dto = new ForecastController.UpdateLineItemRequestDto();
        dto.setName("test");
        dto.setCategory(ForecastLineItem.ForecastCategory.BEST_CASE);
        dto.setBestCase(BigDecimal.TEN);
        dto.setLikely(BigDecimal.TEN);
        dto.setWorstCase(BigDecimal.TEN);
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}