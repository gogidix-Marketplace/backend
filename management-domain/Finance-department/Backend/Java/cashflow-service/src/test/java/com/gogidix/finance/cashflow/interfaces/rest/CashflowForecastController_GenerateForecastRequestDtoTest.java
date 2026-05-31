package com.gogidix.finance.cashflow.interfaces.rest;

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
class CashflowForecastController_GenerateForecastRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CashflowForecastController.GenerateForecastRequestDto dto = new CashflowForecastController.GenerateForecastRequestDto();
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastController.GenerateForecastRequestDto dto1 = new CashflowForecastController.GenerateForecastRequestDto();
        CashflowForecastController.GenerateForecastRequestDto dto2 = new CashflowForecastController.GenerateForecastRequestDto();
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setItemCategories(Collections.emptyList());
        dto1.setCostCenters(Collections.emptyList());
        dto1.setProjects(Collections.emptyList());
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setItemCategories(Collections.emptyList());
        dto2.setCostCenters(Collections.emptyList());
        dto2.setProjects(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setStartDate(LocalDate.of(2099,12,31));
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowForecastController.GenerateForecastRequestDto dto = new CashflowForecastController.GenerateForecastRequestDto();
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setItemCategories(Collections.emptyList());
        dto.setCostCenters(Collections.emptyList());
        dto.setProjects(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowForecastController.GenerateForecastRequestDto dto = new CashflowForecastController.GenerateForecastRequestDto();
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setItemCategories(Collections.emptyList());
        dto.setCostCenters(Collections.emptyList());
        dto.setProjects(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}