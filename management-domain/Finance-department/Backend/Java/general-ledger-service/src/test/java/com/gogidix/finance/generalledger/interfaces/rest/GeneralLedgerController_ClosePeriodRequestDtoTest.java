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
class GeneralLedgerController_ClosePeriodRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        GeneralLedgerController.ClosePeriodRequestDto dto = new GeneralLedgerController.ClosePeriodRequestDto();
        dto.setFiscalYear(99);
        dto.setFiscalPeriod(99);
        dto.setClosingDate(LocalDate.of(2025,6,1));
        assertEquals(99, dto.getFiscalYear());
        assertEquals(99, dto.getFiscalPeriod());
        assertEquals(LocalDate.of(2025,6,1), dto.getClosingDate());
    }

    @Test
    void testEqualsAndHashCode() {
        GeneralLedgerController.ClosePeriodRequestDto dto1 = new GeneralLedgerController.ClosePeriodRequestDto();
        GeneralLedgerController.ClosePeriodRequestDto dto2 = new GeneralLedgerController.ClosePeriodRequestDto();
        dto1.setFiscalYear(42);
        dto1.setFiscalPeriod(42);
        dto1.setClosingDate(LocalDate.of(2025,1,1));
        dto2.setFiscalYear(42);
        dto2.setFiscalPeriod(42);
        dto2.setClosingDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFiscalYear(999);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        GeneralLedgerController.ClosePeriodRequestDto dto = new GeneralLedgerController.ClosePeriodRequestDto();
        dto.setFiscalYear(42);
        dto.setFiscalPeriod(42);
        dto.setClosingDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        GeneralLedgerController.ClosePeriodRequestDto dto = new GeneralLedgerController.ClosePeriodRequestDto();
        dto.setFiscalYear(42);
        dto.setFiscalPeriod(42);
        dto.setClosingDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}