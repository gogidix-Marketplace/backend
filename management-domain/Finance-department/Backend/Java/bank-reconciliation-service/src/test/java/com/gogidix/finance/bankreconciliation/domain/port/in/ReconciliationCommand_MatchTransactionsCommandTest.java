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
class ReconciliationCommand_MatchTransactionsCommandTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationCommand.MatchTransactionsCommand dto = new ReconciliationCommand.MatchTransactionsCommand();
        dto.setTenantId("val-tenantId");
        dto.setReconciliationId("val-reconciliationId");
        dto.setBankTransactionId("val-bankTransactionId");
        dto.setBookTransactionId("val-bookTransactionId");
        dto.setMatchedBy("val-matchedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reconciliationId", dto.getReconciliationId());
        assertEquals("val-bankTransactionId", dto.getBankTransactionId());
        assertEquals("val-bookTransactionId", dto.getBookTransactionId());
        assertEquals("val-matchedBy", dto.getMatchedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationCommand.MatchTransactionsCommand dto1 = new ReconciliationCommand.MatchTransactionsCommand();
        ReconciliationCommand.MatchTransactionsCommand dto2 = new ReconciliationCommand.MatchTransactionsCommand();
        dto1.setTenantId("test");
        dto1.setReconciliationId("test");
        dto1.setBankTransactionId("test");
        dto1.setBookTransactionId("test");
        dto1.setMatchedBy("test");
        dto1.setMatchConfidence(null);
        dto2.setTenantId("test");
        dto2.setReconciliationId("test");
        dto2.setBankTransactionId("test");
        dto2.setBookTransactionId("test");
        dto2.setMatchedBy("test");
        dto2.setMatchConfidence(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationCommand.MatchTransactionsCommand dto = new ReconciliationCommand.MatchTransactionsCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setBankTransactionId("test");
        dto.setBookTransactionId("test");
        dto.setMatchedBy("test");
        dto.setMatchConfidence(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationCommand.MatchTransactionsCommand dto = new ReconciliationCommand.MatchTransactionsCommand();
        dto.setTenantId("test");
        dto.setReconciliationId("test");
        dto.setBankTransactionId("test");
        dto.setBookTransactionId("test");
        dto.setMatchedBy("test");
        dto.setMatchConfidence(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}