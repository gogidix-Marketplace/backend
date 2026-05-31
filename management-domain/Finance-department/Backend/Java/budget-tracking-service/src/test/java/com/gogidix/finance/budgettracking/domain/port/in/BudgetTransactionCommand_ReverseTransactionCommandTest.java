package com.gogidix.finance.budgettracking.domain.port.in;

import com.gogidix.finance.budgettracking.domain.port.in.BudgetTransactionCommand;
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
class BudgetTransactionCommand_ReverseTransactionCommandTest {

        @Test
    void testSettersAndGetters() {
        BudgetTransactionCommand.ReverseTransactionCommand dto = new BudgetTransactionCommand.ReverseTransactionCommand();
        dto.setTenantId("val-tenantId");
        dto.setTransactionId("val-transactionId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTransactionCommand.ReverseTransactionCommand dto1 = new BudgetTransactionCommand.ReverseTransactionCommand();
        BudgetTransactionCommand.ReverseTransactionCommand dto2 = new BudgetTransactionCommand.ReverseTransactionCommand();
        dto1.setTenantId("test");
        dto1.setTransactionId("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setTransactionId("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTransactionCommand.ReverseTransactionCommand dto = new BudgetTransactionCommand.ReverseTransactionCommand();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTransactionCommand.ReverseTransactionCommand dto = new BudgetTransactionCommand.ReverseTransactionCommand();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}