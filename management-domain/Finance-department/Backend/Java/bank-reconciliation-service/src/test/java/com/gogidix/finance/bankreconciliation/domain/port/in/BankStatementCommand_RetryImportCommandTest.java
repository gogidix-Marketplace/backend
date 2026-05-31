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
class BankStatementCommand_RetryImportCommandTest {

        @Test
    void testSettersAndGetters() {
        BankStatementCommand.RetryImportCommand dto = new BankStatementCommand.RetryImportCommand();
        dto.setTenantId("val-tenantId");
        dto.setStatementId("val-statementId");
        dto.setRetriedBy("val-retriedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-statementId", dto.getStatementId());
        assertEquals("val-retriedBy", dto.getRetriedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        BankStatementCommand.RetryImportCommand dto1 = new BankStatementCommand.RetryImportCommand();
        BankStatementCommand.RetryImportCommand dto2 = new BankStatementCommand.RetryImportCommand();
        dto1.setTenantId("test");
        dto1.setStatementId("test");
        dto1.setRetriedBy("test");
        dto2.setTenantId("test");
        dto2.setStatementId("test");
        dto2.setRetriedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BankStatementCommand.RetryImportCommand dto = new BankStatementCommand.RetryImportCommand();
        dto.setTenantId("test");
        dto.setStatementId("test");
        dto.setRetriedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BankStatementCommand.RetryImportCommand dto = new BankStatementCommand.RetryImportCommand();
        dto.setTenantId("test");
        dto.setStatementId("test");
        dto.setRetriedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}