package com.gogidix.finance.cashflow.domain.port.in;

import com.gogidix.finance.cashflow.domain.port.in.CashflowItemCommand;
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
class CashflowItemCommand_UpdateCashflowItemCommandTest {

        @Test
    void testSettersAndGetters() {
        CashflowItemCommand.UpdateCashflowItemCommand dto = new CashflowItemCommand.UpdateCashflowItemCommand();
        dto.setTenantId("val-tenantId");
        dto.setCashflowItemId("val-cashflowItemId");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setExpectedDate(LocalDate.of(2025,6,1));
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setCounterparty("val-counterparty");
        dto.setAccount("val-account");
        dto.setCostCenter("val-costCenter");
        dto.setNotes("val-notes");
        dto.setTaxAmount(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-cashflowItemId", dto.getCashflowItemId());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpectedDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals("val-counterparty", dto.getCounterparty());
        assertEquals("val-account", dto.getAccount());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(BigDecimal.ONE, dto.getTaxAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowItemCommand.UpdateCashflowItemCommand dto1 = new CashflowItemCommand.UpdateCashflowItemCommand();
        CashflowItemCommand.UpdateCashflowItemCommand dto2 = new CashflowItemCommand.UpdateCashflowItemCommand();
        dto1.setTenantId("test");
        dto1.setCashflowItemId("test");
        dto1.setDescription("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setExpectedDate(LocalDate.of(2025,1,1));
        dto1.setTransactionDate(LocalDate.of(2025,1,1));
        dto1.setCounterparty("test");
        dto1.setAccount("test");
        dto1.setCostCenter("test");
        dto1.setTags(Collections.emptyList());
        dto1.setNotes("test");
        dto1.setTaxAmount(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setCashflowItemId("test");
        dto2.setDescription("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setExpectedDate(LocalDate.of(2025,1,1));
        dto2.setTransactionDate(LocalDate.of(2025,1,1));
        dto2.setCounterparty("test");
        dto2.setAccount("test");
        dto2.setCostCenter("test");
        dto2.setTags(Collections.emptyList());
        dto2.setNotes("test");
        dto2.setTaxAmount(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowItemCommand.UpdateCashflowItemCommand dto = new CashflowItemCommand.UpdateCashflowItemCommand();
        dto.setTenantId("test");
        dto.setCashflowItemId("test");
        dto.setDescription("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setExpectedDate(LocalDate.of(2025,1,1));
        dto.setTransactionDate(LocalDate.of(2025,1,1));
        dto.setCounterparty("test");
        dto.setAccount("test");
        dto.setCostCenter("test");
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        dto.setTaxAmount(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowItemCommand.UpdateCashflowItemCommand dto = new CashflowItemCommand.UpdateCashflowItemCommand();
        dto.setTenantId("test");
        dto.setCashflowItemId("test");
        dto.setDescription("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setExpectedDate(LocalDate.of(2025,1,1));
        dto.setTransactionDate(LocalDate.of(2025,1,1));
        dto.setCounterparty("test");
        dto.setAccount("test");
        dto.setCostCenter("test");
        dto.setTags(Collections.emptyList());
        dto.setNotes("test");
        dto.setTaxAmount(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}