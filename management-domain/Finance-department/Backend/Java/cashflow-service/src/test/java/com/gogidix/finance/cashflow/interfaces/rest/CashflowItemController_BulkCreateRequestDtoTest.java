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
class CashflowItemController_BulkCreateRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CashflowItemController.BulkCreateRequestDto dto = new CashflowItemController.BulkCreateRequestDto();


    }

    @Test
    void testEqualsAndHashCode() {
        CashflowItemController.BulkCreateRequestDto dto1 = new CashflowItemController.BulkCreateRequestDto();
        CashflowItemController.BulkCreateRequestDto dto2 = new CashflowItemController.BulkCreateRequestDto();
        dto1.setItems(Collections.emptyList());
        dto2.setItems(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setItems(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowItemController.BulkCreateRequestDto dto = new CashflowItemController.BulkCreateRequestDto();
        dto.setItems(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowItemController.BulkCreateRequestDto dto = new CashflowItemController.BulkCreateRequestDto();
        dto.setItems(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}