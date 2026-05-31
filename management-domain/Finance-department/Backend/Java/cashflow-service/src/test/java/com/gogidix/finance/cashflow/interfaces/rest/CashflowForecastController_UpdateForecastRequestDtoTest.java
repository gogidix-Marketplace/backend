package com.gogidix.finance.cashflow.interfaces.rest;

import com.gogidix.finance.cashflow.domain.model.CashflowForecast;
import com.gogidix.finance.cashflow.interfaces.rest.CashflowForecastController;
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
class CashflowForecastController_UpdateForecastRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CashflowForecastController.UpdateForecastRequestDto dto = new CashflowForecastController.UpdateForecastRequestDto();
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setOpeningBalance(BigDecimal.ONE);
        dto.setNotes("val-notes");
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastController.UpdateForecastRequestDto dto1 = new CashflowForecastController.UpdateForecastRequestDto();
        CashflowForecastController.UpdateForecastRequestDto dto2 = new CashflowForecastController.UpdateForecastRequestDto();
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setOpeningBalance(BigDecimal.TEN);
        dto1.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto1.setTags(Collections.emptyList());
        dto1.setNotes("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setOpeningBalance(BigDecimal.TEN);
        dto2.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto2.setTags(Collections.emptyList());
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowForecastController.UpdateForecastRequestDto dto = new CashflowForecastController.UpdateForecastRequestDto();
        dto.setName("test");
        dto.setDescription("test");
        dto.setOpeningBalance(BigDecimal.TEN);
        dto.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowForecastController.UpdateForecastRequestDto dto = new CashflowForecastController.UpdateForecastRequestDto();
        dto.setName("test");
        dto.setDescription("test");
        dto.setOpeningBalance(BigDecimal.TEN);
        dto.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}