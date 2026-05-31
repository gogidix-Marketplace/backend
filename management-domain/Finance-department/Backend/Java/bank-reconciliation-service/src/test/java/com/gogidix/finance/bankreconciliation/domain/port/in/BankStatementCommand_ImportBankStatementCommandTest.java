package com.gogidix.finance.bankreconciliation.domain.port.in;

import com.gogidix.finance.bankreconciliation.domain.model.BankStatement;
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
class BankStatementCommand_ImportBankStatementCommandTest {

        @Test
    void testSettersAndGetters() {
        BankStatementCommand.ImportBankStatementCommand dto = new BankStatementCommand.ImportBankStatementCommand();
        dto.setTenantId("val-tenantId");
        dto.setAccountId("val-accountId");
        dto.setAccountNumber("val-accountNumber");
        dto.setStatementDate(LocalDate.of(2025,6,1));
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setOpeningBalance(BigDecimal.ONE);
        dto.setClosingBalance(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setFileReference("val-fileReference");
        dto.setBankReference("val-bankReference");
        dto.setImportedBy("val-importedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals(LocalDate.of(2025,6,1), dto.getStatementDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
        assertEquals(BigDecimal.ONE, dto.getClosingBalance());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-fileReference", dto.getFileReference());
        assertEquals("val-bankReference", dto.getBankReference());
        assertEquals("val-importedBy", dto.getImportedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        BankStatementCommand.ImportBankStatementCommand dto1 = new BankStatementCommand.ImportBankStatementCommand();
        BankStatementCommand.ImportBankStatementCommand dto2 = new BankStatementCommand.ImportBankStatementCommand();
        dto1.setTenantId("test");
        dto1.setAccountId("test");
        dto1.setAccountNumber("test");
        dto1.setStatementDate(LocalDate.of(2025,1,1));
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setOpeningBalance(BigDecimal.TEN);
        dto1.setClosingBalance(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setImportSource(BankStatement.ImportSource.MANUAL_UPLOAD);
        dto1.setFileReference("test");
        dto1.setBankReference("test");
        dto1.setStatementType(BankStatement.StatementType.STATEMENT);
        dto1.setTransactions(Collections.emptyList());
        dto1.setImportedBy("test");
        dto2.setTenantId("test");
        dto2.setAccountId("test");
        dto2.setAccountNumber("test");
        dto2.setStatementDate(LocalDate.of(2025,1,1));
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setOpeningBalance(BigDecimal.TEN);
        dto2.setClosingBalance(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setImportSource(BankStatement.ImportSource.MANUAL_UPLOAD);
        dto2.setFileReference("test");
        dto2.setBankReference("test");
        dto2.setStatementType(BankStatement.StatementType.STATEMENT);
        dto2.setTransactions(Collections.emptyList());
        dto2.setImportedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        BankStatementCommand.ImportBankStatementCommand dto = new BankStatementCommand.ImportBankStatementCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setAccountNumber("test");
        dto.setStatementDate(LocalDate.of(2025,1,1));
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setOpeningBalance(BigDecimal.TEN);
        dto.setClosingBalance(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setImportSource(BankStatement.ImportSource.MANUAL_UPLOAD);
        dto.setFileReference("test");
        dto.setBankReference("test");
        dto.setStatementType(BankStatement.StatementType.STATEMENT);
        dto.setTransactions(Collections.emptyList());
        dto.setImportedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        BankStatementCommand.ImportBankStatementCommand dto = new BankStatementCommand.ImportBankStatementCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setAccountNumber("test");
        dto.setStatementDate(LocalDate.of(2025,1,1));
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setOpeningBalance(BigDecimal.TEN);
        dto.setClosingBalance(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setImportSource(BankStatement.ImportSource.MANUAL_UPLOAD);
        dto.setFileReference("test");
        dto.setBankReference("test");
        dto.setStatementType(BankStatement.StatementType.STATEMENT);
        dto.setTransactions(Collections.emptyList());
        dto.setImportedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}