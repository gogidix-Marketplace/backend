package com.gogidix.finance.cashflow.interfaces.rest;

import com.gogidix.finance.cashflow.interfaces.rest.CashflowItemController;
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
class CashflowItemController_SettleRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CashflowItemController.SettleRequestDto dto = new CashflowItemController.SettleRequestDto();
        dto.setBankReference("val-bankReference");
        assertEquals("val-bankReference", dto.getBankReference());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowItemController.SettleRequestDto dto1 = new CashflowItemController.SettleRequestDto();
        CashflowItemController.SettleRequestDto dto2 = new CashflowItemController.SettleRequestDto();
        dto1.setBankReference("test");
        dto2.setBankReference("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setBankReference(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowItemController.SettleRequestDto dto = new CashflowItemController.SettleRequestDto();
        dto.setBankReference("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowItemController.SettleRequestDto dto = new CashflowItemController.SettleRequestDto();
        dto.setBankReference("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}