package com.gogidix.finance.generalledger.interfaces.rest;

import com.gogidix.finance.generalledger.interfaces.rest.LedgerAccountController;
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
class LedgerAccountController_OpeningBalanceRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        LedgerAccountController.OpeningBalanceRequestDto dto = new LedgerAccountController.OpeningBalanceRequestDto();
        dto.setOpeningBalance(BigDecimal.ONE);
        dto.setAsOfDate(LocalDate.of(2025,6,1));
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
        assertEquals(LocalDate.of(2025,6,1), dto.getAsOfDate());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountController.OpeningBalanceRequestDto dto1 = new LedgerAccountController.OpeningBalanceRequestDto();
        LedgerAccountController.OpeningBalanceRequestDto dto2 = new LedgerAccountController.OpeningBalanceRequestDto();
        dto1.setOpeningBalance(BigDecimal.TEN);
        dto1.setAsOfDate(LocalDate.of(2025,1,1));
        dto2.setOpeningBalance(BigDecimal.TEN);
        dto2.setAsOfDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setOpeningBalance(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LedgerAccountController.OpeningBalanceRequestDto dto = new LedgerAccountController.OpeningBalanceRequestDto();
        dto.setOpeningBalance(BigDecimal.TEN);
        dto.setAsOfDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LedgerAccountController.OpeningBalanceRequestDto dto = new LedgerAccountController.OpeningBalanceRequestDto();
        dto.setOpeningBalance(BigDecimal.TEN);
        dto.setAsOfDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}