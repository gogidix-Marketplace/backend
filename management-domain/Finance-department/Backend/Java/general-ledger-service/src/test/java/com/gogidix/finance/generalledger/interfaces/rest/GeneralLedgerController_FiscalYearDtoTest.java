package com.gogidix.finance.generalledger.interfaces.rest;

import com.gogidix.finance.generalledger.interfaces.rest.GeneralLedgerController;
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
class GeneralLedgerController_FiscalYearDtoTest {

        @Test
    void testSettersAndGetters() {
        GeneralLedgerController.FiscalYearDto dto = new GeneralLedgerController.FiscalYearDto();
        dto.setFiscalYear(99);
        dto.setClosed(true);
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        assertEquals(99, dto.getFiscalYear());
        assertTrue(dto.isClosed());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
    }

    @Test
    void testEqualsAndHashCode() {
        GeneralLedgerController.FiscalYearDto dto1 = new GeneralLedgerController.FiscalYearDto();
        GeneralLedgerController.FiscalYearDto dto2 = new GeneralLedgerController.FiscalYearDto();
        dto1.setFiscalYear(42);
        dto1.setClosed(true);
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto2.setFiscalYear(42);
        dto2.setClosed(true);
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFiscalYear(999);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        GeneralLedgerController.FiscalYearDto dto = new GeneralLedgerController.FiscalYearDto();
        dto.setFiscalYear(42);
        dto.setClosed(true);
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        GeneralLedgerController.FiscalYearDto dto = new GeneralLedgerController.FiscalYearDto();
        dto.setFiscalYear(42);
        dto.setClosed(true);
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}