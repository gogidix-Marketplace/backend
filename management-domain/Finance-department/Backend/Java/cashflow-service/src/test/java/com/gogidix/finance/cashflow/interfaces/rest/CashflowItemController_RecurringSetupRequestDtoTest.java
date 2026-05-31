package com.gogidix.finance.cashflow.interfaces.rest;

import com.gogidix.finance.cashflow.domain.model.CashflowItem;
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
class CashflowItemController_RecurringSetupRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CashflowItemController.RecurringSetupRequestDto dto = new CashflowItemController.RecurringSetupRequestDto();


    }

    @Test
    void testEqualsAndHashCode() {
        CashflowItemController.RecurringSetupRequestDto dto1 = new CashflowItemController.RecurringSetupRequestDto();
        CashflowItemController.RecurringSetupRequestDto dto2 = new CashflowItemController.RecurringSetupRequestDto();
        dto1.setFrequency(CashflowItem.RecurringFrequency.DAILY);
        dto2.setFrequency(CashflowItem.RecurringFrequency.DAILY);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFrequency(CashflowItem.RecurringFrequency.WEEKLY);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowItemController.RecurringSetupRequestDto dto = new CashflowItemController.RecurringSetupRequestDto();
        dto.setFrequency(CashflowItem.RecurringFrequency.DAILY);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowItemController.RecurringSetupRequestDto dto = new CashflowItemController.RecurringSetupRequestDto();
        dto.setFrequency(CashflowItem.RecurringFrequency.DAILY);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}