package com.gogidix.finance.ledger.domain.port.in;

import com.gogidix.finance.ledger.domain.port.in.LedgerAccountCommand;
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
class LedgerAccountCommand_UpdateAccountCommandTest {

        @Test
    void testSettersAndGetters() {
        LedgerAccountCommand.UpdateAccountCommand dto = new LedgerAccountCommand.UpdateAccountCommand();
        dto.setTenantId("val-tenantId");
        dto.setAccountId("val-accountId");
        dto.setAccountName("val-accountName");
        dto.setDescription("val-description");
        dto.setCostCenter("val-costCenter");
        dto.setDepartment("val-department");
        dto.setLocation("val-location");
        dto.setIsCashAccount(true);
        dto.setIsReconcilable(true);
        dto.setTaxCode("val-taxCode");
        dto.setAllowsManualEntry(true);
        dto.setCreditLimit(BigDecimal.ONE);
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-accountId", dto.getAccountId());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-location", dto.getLocation());
        assertTrue(dto.getIsCashAccount());
        assertTrue(dto.getIsReconcilable());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertTrue(dto.getAllowsManualEntry());
        assertEquals(BigDecimal.ONE, dto.getCreditLimit());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountCommand.UpdateAccountCommand dto1 = new LedgerAccountCommand.UpdateAccountCommand();
        LedgerAccountCommand.UpdateAccountCommand dto2 = new LedgerAccountCommand.UpdateAccountCommand();
        dto1.setTenantId("test");
        dto1.setAccountId("test");
        dto1.setAccountName("test");
        dto1.setDescription("test");
        dto1.setCostCenter("test");
        dto1.setDepartment("test");
        dto1.setLocation("test");
        dto1.setIsCashAccount(true);
        dto1.setIsReconcilable(true);
        dto1.setTaxCode("test");
        dto1.setAllowsManualEntry(true);
        dto1.setCreditLimit(BigDecimal.TEN);
        dto1.setNotes("test");
        dto1.setTags(Collections.emptyList());
        dto2.setTenantId("test");
        dto2.setAccountId("test");
        dto2.setAccountName("test");
        dto2.setDescription("test");
        dto2.setCostCenter("test");
        dto2.setDepartment("test");
        dto2.setLocation("test");
        dto2.setIsCashAccount(true);
        dto2.setIsReconcilable(true);
        dto2.setTaxCode("test");
        dto2.setAllowsManualEntry(true);
        dto2.setCreditLimit(BigDecimal.TEN);
        dto2.setNotes("test");
        dto2.setTags(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LedgerAccountCommand.UpdateAccountCommand dto = new LedgerAccountCommand.UpdateAccountCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setAccountName("test");
        dto.setDescription("test");
        dto.setCostCenter("test");
        dto.setDepartment("test");
        dto.setLocation("test");
        dto.setIsCashAccount(true);
        dto.setIsReconcilable(true);
        dto.setTaxCode("test");
        dto.setAllowsManualEntry(true);
        dto.setCreditLimit(BigDecimal.TEN);
        dto.setNotes("test");
        dto.setTags(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LedgerAccountCommand.UpdateAccountCommand dto = new LedgerAccountCommand.UpdateAccountCommand();
        dto.setTenantId("test");
        dto.setAccountId("test");
        dto.setAccountName("test");
        dto.setDescription("test");
        dto.setCostCenter("test");
        dto.setDepartment("test");
        dto.setLocation("test");
        dto.setIsCashAccount(true);
        dto.setIsReconcilable(true);
        dto.setTaxCode("test");
        dto.setAllowsManualEntry(true);
        dto.setCreditLimit(BigDecimal.TEN);
        dto.setNotes("test");
        dto.setTags(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}