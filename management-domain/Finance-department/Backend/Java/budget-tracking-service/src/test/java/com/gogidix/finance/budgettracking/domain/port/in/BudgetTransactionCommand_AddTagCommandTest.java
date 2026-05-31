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
class BudgetTransactionCommand_AddTagCommandTest {

        @Test
    void testSettersAndGetters() {
        BudgetTransactionCommand.AddTagCommand dto = new BudgetTransactionCommand.AddTagCommand();
        dto.setTenantId("val-tenantId");
        dto.setTransactionId("val-transactionId");
        dto.setTag("val-tag");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals("val-tag", dto.getTag());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTransactionCommand.AddTagCommand dto1 = new BudgetTransactionCommand.AddTagCommand();
        BudgetTransactionCommand.AddTagCommand dto2 = new BudgetTransactionCommand.AddTagCommand();
        dto1.setTenantId("test");
        dto1.setTransactionId("test");
        dto1.setTag("test");
        dto2.setTenantId("test");
        dto2.setTransactionId("test");
        dto2.setTag("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTransactionCommand.AddTagCommand dto = new BudgetTransactionCommand.AddTagCommand();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        dto.setTag("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTransactionCommand.AddTagCommand dto = new BudgetTransactionCommand.AddTagCommand();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        dto.setTag("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}