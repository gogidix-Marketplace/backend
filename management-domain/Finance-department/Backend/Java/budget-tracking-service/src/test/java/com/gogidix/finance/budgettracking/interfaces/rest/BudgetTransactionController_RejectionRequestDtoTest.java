package com.gogidix.finance.budgettracking.interfaces.rest;

import com.gogidix.finance.budgettracking.interfaces.rest.BudgetTransactionController;
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
class BudgetTransactionController_RejectionRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        BudgetTransactionController.RejectionRequestDto dto = new BudgetTransactionController.RejectionRequestDto();
        dto.setReason("val-reason");
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTransactionController.RejectionRequestDto dto1 = new BudgetTransactionController.RejectionRequestDto();
        BudgetTransactionController.RejectionRequestDto dto2 = new BudgetTransactionController.RejectionRequestDto();
        dto1.setReason("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setReason(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTransactionController.RejectionRequestDto dto = new BudgetTransactionController.RejectionRequestDto();
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTransactionController.RejectionRequestDto dto = new BudgetTransactionController.RejectionRequestDto();
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}