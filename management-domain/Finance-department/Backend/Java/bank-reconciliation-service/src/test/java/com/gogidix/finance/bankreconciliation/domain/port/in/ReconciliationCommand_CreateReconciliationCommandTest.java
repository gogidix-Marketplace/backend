package com.gogidix.finance.bankreconciliation.domain.port.in;

import com.gogidix.finance.bankreconciliation.domain.model.Reconciliation;
import com.gogidix.finance.bankreconciliation.domain.port.in.ReconciliationCommand;
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
class ReconciliationCommand_CreateReconciliationCommandTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationCommand.CreateReconciliationCommand dto = new ReconciliationCommand.CreateReconciliationCommand();
        dto.setTenantId("val-tenantId");
        dto.setAccountId("val-accountId");
        dto.setAccountNumber("val-accountNumber");
        dto.setStatementId("val-statementId");
        dto.setReconciliationDate(LocalDate.of(2025,6,1));
        dto.setPeriodStart(LocalDate.of(2025,6,1));
        dto.setPeriodEnd(LocalDate.of(2025,6,1));
        dto.setStartingBalance(BigDecimal.ONE);
        dto.setEndingBalance(BigDecimal.ONE);
        dto.setTolerance(BigDecimal.ONE);
        dto.setAutoReconcile(true);
        dto.setNotes("val-notes");
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountNumber", dto.getAccountNumber());
        assertEquals("val-statementId", dto.getStatementId());
        assertEquals(LocalDate.of(2025,6,1), dto.getReconciliationDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodStart());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodEnd());
        assertEquals(BigDecimal.ONE, dto.getStartingBalance());
        assertEquals(BigDecimal.ONE, dto.getEndingBalance());
        assertEquals(BigDecimal.ONE, dto.getTolerance());
        assertTrue(dto.getAutoReconcile());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationCommand.CreateReconciliationCommand dto1 = new ReconciliationCommand.CreateReconciliationCommand();
        ReconciliationCommand.CreateReconciliationCommand dto2 = new ReconciliationCommand.CreateReconciliationCommand();
        dto1.setTenantId("test");
        dto1.setAccountId("test");
        dto1.setAccountNumber("test");
        dto1.setStatementId("test");
        dto1.setReconciliationDate(LocalDate.of(2025,1,1));
        dto1.setPeriodStart(LocalDate.of(2025,1,1));
        dto1.setPeriodEnd(LocalDate.of(2025,1,1));
        dto1.setStartingBalance(BigDecimal.TEN);
        dto1.setEndingBalance(BigDecimal.TEN);
        dto1.setTolerance(BigDecimal.TEN);
        dto1.setReconciliationMethod(Reconciliation.ReconciliationMethod.AUTOMATIC);
        dto1.setAutoReconcile(true);
        dto1.setNotes("test");
        dto1.setCreatedBy("test");
        dto2.setTenantId("test");
        dto2.setAccountId("test");
        dto2.setAccountNumber("test");
        dto2.setStatementId("test");
        dto2.setReconciliationDate(LocalDate.of(2025,1,1));
        dto2.setPeriodStart(LocalDate.of(2025,1,1));
        dto2.setPeriodEnd(LocalDate.of(2025,1,1));
        dto2.setStartingBalance(BigDecimal.TEN);
        dto2.setEndingBalance(BigDecimal.TEN);
        dto2.setTolerance(BigDecimal.TEN);
        dto2.setReconciliationMethod(Reconciliation.ReconciliationMethod.AUTOMATIC);
        dto2.setAutoReconcile(true);
        dto2.setNotes("test");
        dto2.setCreatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationCommand.CreateReconciliationCommand dto = new ReconciliationCommand.CreateReconciliationCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setAccountNumber("test");
        dto.setStatementId("test");
        dto.setReconciliationDate(LocalDate.of(2025,1,1));
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setStartingBalance(BigDecimal.TEN);
        dto.setEndingBalance(BigDecimal.TEN);
        dto.setTolerance(BigDecimal.TEN);
        dto.setReconciliationMethod(Reconciliation.ReconciliationMethod.AUTOMATIC);
        dto.setAutoReconcile(true);
        dto.setNotes("test");
        dto.setCreatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationCommand.CreateReconciliationCommand dto = new ReconciliationCommand.CreateReconciliationCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setAccountNumber("test");
        dto.setStatementId("test");
        dto.setReconciliationDate(LocalDate.of(2025,1,1));
        dto.setPeriodStart(LocalDate.of(2025,1,1));
        dto.setPeriodEnd(LocalDate.of(2025,1,1));
        dto.setStartingBalance(BigDecimal.TEN);
        dto.setEndingBalance(BigDecimal.TEN);
        dto.setTolerance(BigDecimal.TEN);
        dto.setReconciliationMethod(Reconciliation.ReconciliationMethod.AUTOMATIC);
        dto.setAutoReconcile(true);
        dto.setNotes("test");
        dto.setCreatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}