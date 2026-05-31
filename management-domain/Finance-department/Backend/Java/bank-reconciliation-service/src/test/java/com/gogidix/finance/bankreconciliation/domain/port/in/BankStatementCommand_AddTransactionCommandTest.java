package com.gogidix.finance.bankreconciliation.domain.port.in;

import com.gogidix.finance.bankreconciliation.domain.port.in.BankStatementCommand;
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
class BankStatementCommand_AddTransactionCommandTest {

        @Test
    void testSettersAndGetters() {
        BankStatementCommand.AddTransactionCommand dto = new BankStatementCommand.AddTransactionCommand();
        dto.setTenantId("val-tenantId");
        dto.setStatementId("val-statementId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-statementId", dto.getStatementId());
    }

    @Test
    void testEqualsAndHashCode() {
        BankStatementCommand.AddTransactionCommand dto1 = new BankStatementCommand.AddTransactionCommand();
        BankStatementCommand.AddTransactionCommand dto2 = new BankStatementCommand.AddTransactionCommand();
        dto1.setTenantId("test");
        dto1.setStatementId("test");
        dto1.setTransaction(null);
        dto2.setTenantId("test");
        dto2.setStatementId("test");
        dto2.setTransaction(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BankStatementCommand.AddTransactionCommand dto = new BankStatementCommand.AddTransactionCommand();
        dto.setTenantId("test");
        dto.setStatementId("test");
        dto.setTransaction(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BankStatementCommand.AddTransactionCommand dto = new BankStatementCommand.AddTransactionCommand();
        dto.setTenantId("test");
        dto.setStatementId("test");
        dto.setTransaction(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}