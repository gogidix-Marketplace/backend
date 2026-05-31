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
class BudgetTransactionCommand_RejectTransactionCommandTest {

        @Test
    void testSettersAndGetters() {
        BudgetTransactionCommand.RejectTransactionCommand dto = new BudgetTransactionCommand.RejectTransactionCommand();
        dto.setTenantId("val-tenantId");
        dto.setTransactionId("val-transactionId");
        dto.setRejecter("val-rejecter");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals("val-rejecter", dto.getRejecter());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTransactionCommand.RejectTransactionCommand dto1 = new BudgetTransactionCommand.RejectTransactionCommand();
        BudgetTransactionCommand.RejectTransactionCommand dto2 = new BudgetTransactionCommand.RejectTransactionCommand();
        dto1.setTenantId("test");
        dto1.setTransactionId("test");
        dto1.setRejecter("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setTransactionId("test");
        dto2.setRejecter("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTransactionCommand.RejectTransactionCommand dto = new BudgetTransactionCommand.RejectTransactionCommand();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        dto.setRejecter("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTransactionCommand.RejectTransactionCommand dto = new BudgetTransactionCommand.RejectTransactionCommand();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        dto.setRejecter("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}