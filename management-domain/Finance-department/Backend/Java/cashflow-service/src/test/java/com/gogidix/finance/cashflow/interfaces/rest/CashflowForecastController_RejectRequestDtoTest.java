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
class CashflowForecastController_RejectRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CashflowForecastController.RejectRequestDto dto = new CashflowForecastController.RejectRequestDto();
        dto.setReason("val-reason");
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastController.RejectRequestDto dto1 = new CashflowForecastController.RejectRequestDto();
        CashflowForecastController.RejectRequestDto dto2 = new CashflowForecastController.RejectRequestDto();
        dto1.setReason("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setReason(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowForecastController.RejectRequestDto dto = new CashflowForecastController.RejectRequestDto();
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowForecastController.RejectRequestDto dto = new CashflowForecastController.RejectRequestDto();
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}