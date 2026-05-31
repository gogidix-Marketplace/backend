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
class BankStatementCommand_ProcessBankStatementCommandTest {

        @Test
    void testSettersAndGetters() {
        BankStatementCommand.ProcessBankStatementCommand dto = new BankStatementCommand.ProcessBankStatementCommand();
        dto.setTenantId("val-tenantId");
        dto.setStatementId("val-statementId");
        dto.setProcessedBy("val-processedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-statementId", dto.getStatementId());
        assertEquals("val-processedBy", dto.getProcessedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        BankStatementCommand.ProcessBankStatementCommand dto1 = new BankStatementCommand.ProcessBankStatementCommand();
        BankStatementCommand.ProcessBankStatementCommand dto2 = new BankStatementCommand.ProcessBankStatementCommand();
        dto1.setTenantId("test");
        dto1.setStatementId("test");
        dto1.setProcessedBy("test");
        dto2.setTenantId("test");
        dto2.setStatementId("test");
        dto2.setProcessedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BankStatementCommand.ProcessBankStatementCommand dto = new BankStatementCommand.ProcessBankStatementCommand();
        dto.setTenantId("test");
        dto.setStatementId("test");
        dto.setProcessedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BankStatementCommand.ProcessBankStatementCommand dto = new BankStatementCommand.ProcessBankStatementCommand();
        dto.setTenantId("test");
        dto.setStatementId("test");
        dto.setProcessedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}