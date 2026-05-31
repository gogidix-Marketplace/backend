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
class CashflowForecastController_CreateForecastRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CashflowForecastController.CreateForecastRequestDto dto = new CashflowForecastController.CreateForecastRequestDto();
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setOpeningBalance(BigDecimal.ONE);
        dto.setNotes("val-notes");
        dto.setIsBaseline(true);
        dto.setParentForecastId("val-parentForecastId");
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
        assertEquals("val-notes", dto.getNotes());
        assertTrue(dto.getIsBaseline());
        assertEquals("val-parentForecastId", dto.getParentForecastId());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastController.CreateForecastRequestDto dto1 = new CashflowForecastController.CreateForecastRequestDto();
        CashflowForecastController.CreateForecastRequestDto dto2 = new CashflowForecastController.CreateForecastRequestDto();
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setPeriod(CashflowForecast.ForecastPeriod.DAILY);
        dto1.setScenario(CashflowForecast.ForecastScenario.BASELINE);
        dto1.setOpeningBalance(BigDecimal.TEN);
        dto1.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto1.setTags(Collections.emptyList());
        dto1.setNotes("test");
        dto1.setIsBaseline(true);
        dto1.setParentForecastId("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setPeriod(CashflowForecast.ForecastPeriod.DAILY);
        dto2.setScenario(CashflowForecast.ForecastScenario.BASELINE);
        dto2.setOpeningBalance(BigDecimal.TEN);
        dto2.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto2.setTags(Collections.emptyList());
        dto2.setNotes("test");
        dto2.setIsBaseline(true);
        dto2.setParentForecastId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowForecastController.CreateForecastRequestDto dto = new CashflowForecastController.CreateForecastRequestDto();
        dto.setName("test");
        dto.setDescription("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setPeriod(CashflowForecast.ForecastPeriod.DAILY);
        dto.setScenario(CashflowForecast.ForecastScenario.BASELINE);
        dto.setOpeningBalance(BigDecimal.TEN);
        dto.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        dto.setIsBaseline(true);
        dto.setParentForecastId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowForecastController.CreateForecastRequestDto dto = new CashflowForecastController.CreateForecastRequestDto();
        dto.setName("test");
        dto.setDescription("test");
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setPeriod(CashflowForecast.ForecastPeriod.DAILY);
        dto.setScenario(CashflowForecast.ForecastScenario.BASELINE);
        dto.setOpeningBalance(BigDecimal.TEN);
        dto.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        dto.setIsBaseline(true);
        dto.setParentForecastId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}