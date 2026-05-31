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
class BudgetTransactionCommand_RecordTransactionCommandTest {

        @Test
    void testSettersAndGetters() {
        BudgetTransactionCommand.RecordTransactionCommand dto = new BudgetTransactionCommand.RecordTransactionCommand();
        dto.setTenantId("val-tenantId");
        dto.setTransactionId("val-transactionId");
        dto.setBalanceBefore(BigDecimal.ONE);
        dto.setBalanceAfter(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals(BigDecimal.ONE, dto.getBalanceBefore());
        assertEquals(BigDecimal.ONE, dto.getBalanceAfter());
    }

    @Test
    void testEqualsAndHashCode() {
        BudgetTransactionCommand.RecordTransactionCommand dto1 = new BudgetTransactionCommand.RecordTransactionCommand();
        BudgetTransactionCommand.RecordTransactionCommand dto2 = new BudgetTransactionCommand.RecordTransactionCommand();
        dto1.setTenantId("test");
        dto1.setTransactionId("test");
        dto1.setBalanceBefore(BigDecimal.TEN);
        dto1.setBalanceAfter(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setTransactionId("test");
        dto2.setBalanceBefore(BigDecimal.TEN);
        dto2.setBalanceAfter(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BudgetTransactionCommand.RecordTransactionCommand dto = new BudgetTransactionCommand.RecordTransactionCommand();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        dto.setBalanceBefore(BigDecimal.TEN);
        dto.setBalanceAfter(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BudgetTransactionCommand.RecordTransactionCommand dto = new BudgetTransactionCommand.RecordTransactionCommand();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        dto.setBalanceBefore(BigDecimal.TEN);
        dto.setBalanceAfter(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}