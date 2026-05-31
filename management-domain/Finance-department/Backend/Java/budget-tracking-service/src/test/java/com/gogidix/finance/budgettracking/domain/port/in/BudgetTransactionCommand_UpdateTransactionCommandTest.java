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
class BudgetTransactionCommand_UpdateTransactionCommandTest {

        @Test
    void testSettersAndGetters() {
        BudgetTransactionCommand.UpdateTransactionCommand dto = new BudgetTransactionCommand.UpdateTransactionCommand();
        dto.setTenantId("val-tenantId");
        dto.setTransactionId("val-transactionId");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTransactionCommand.UpdateTransactionCommand dto1 = new BudgetTransactionCommand.UpdateTransactionCommand();
        BudgetTransactionCommand.UpdateTransactionCommand dto2 = new BudgetTransactionCommand.UpdateTransactionCommand();
        dto1.setTenantId("test");
        dto1.setTransactionId("test");
        dto1.setDescription("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setTags(Collections.emptyList());
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setTransactionId("test");
        dto2.setDescription("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setTags(Collections.emptyList());
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTransactionCommand.UpdateTransactionCommand dto = new BudgetTransactionCommand.UpdateTransactionCommand();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        dto.setDescription("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTransactionCommand.UpdateTransactionCommand dto = new BudgetTransactionCommand.UpdateTransactionCommand();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        dto.setDescription("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}