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
class GeneralLedgerController_FiscalPeriodDtoTest {

        @Test
    void testSettersAndGetters() {
        GeneralLedgerController.FiscalPeriodDto dto = new GeneralLedgerController.FiscalPeriodDto();
        dto.setFiscalYear(99);
        dto.setFiscalPeriod(99);
        dto.setPeriodName("val-periodName");
        dto.setClosed(true);
        assertEquals(99, dto.getFiscalYear());
        assertEquals(99, dto.getFiscalPeriod());
        assertEquals("val-periodName", dto.getPeriodName());
        assertTrue(dto.isClosed());
    }

    @Test
    void testEqualsAndHashCode() {
        GeneralLedgerController.FiscalPeriodDto dto1 = new GeneralLedgerController.FiscalPeriodDto();
        GeneralLedgerController.FiscalPeriodDto dto2 = new GeneralLedgerController.FiscalPeriodDto();
        dto1.setFiscalYear(42);
        dto1.setFiscalPeriod(42);
        dto1.setPeriodName("test");
        dto1.setClosed(true);
        dto2.setFiscalYear(42);
        dto2.setFiscalPeriod(42);
        dto2.setPeriodName("test");
        dto2.setClosed(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFiscalYear(999);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        GeneralLedgerController.FiscalPeriodDto dto = new GeneralLedgerController.FiscalPeriodDto();
        dto.setFiscalYear(42);
        dto.setFiscalPeriod(42);
        dto.setPeriodName("test");
        dto.setClosed(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        GeneralLedgerController.FiscalPeriodDto dto = new GeneralLedgerController.FiscalPeriodDto();
        dto.setFiscalYear(42);
        dto.setFiscalPeriod(42);
        dto.setPeriodName("test");
        dto.setClosed(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}