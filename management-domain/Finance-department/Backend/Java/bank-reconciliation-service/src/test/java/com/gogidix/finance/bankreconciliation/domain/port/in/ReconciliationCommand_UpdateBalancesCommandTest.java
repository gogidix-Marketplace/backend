package com.gogidix.finance.bankreconciliation.domain.port.in;

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
class ReconciliationCommand_UpdateBalancesCommandTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationCommand.UpdateBalancesCommand dto = new ReconciliationCommand.UpdateBalancesCommand();
        dto.setTenantId("val-tenantId");
        dto.setReconciliationId("val-reconciliationId");
        dto.setBookBalance(BigDecimal.ONE);
        dto.setBankBalance(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
        assertEquals(BigDecimal.ONE, dto.getBookBalance());
        assertEquals(BigDecimal.ONE, dto.getBankBalance());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationCommand.UpdateBalancesCommand dto1 = new ReconciliationCommand.UpdateBalancesCommand();
        ReconciliationCommand.UpdateBalancesCommand dto2 = new ReconciliationCommand.UpdateBalancesCommand();
        dto1.setTenantId("test");
        dto1.setReconciliationId("test");
        dto1.setBookBalance(BigDecimal.TEN);
        dto1.setBankBalance(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setReconciliationId("test");
        dto2.setBookBalance(BigDecimal.TEN);
        dto2.setBankBalance(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationCommand.UpdateBalancesCommand dto = new ReconciliationCommand.UpdateBalancesCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setBookBalance(BigDecimal.TEN);
        dto.setBankBalance(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationCommand.UpdateBalancesCommand dto = new ReconciliationCommand.UpdateBalancesCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setBookBalance(BigDecimal.TEN);
        dto.setBankBalance(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}