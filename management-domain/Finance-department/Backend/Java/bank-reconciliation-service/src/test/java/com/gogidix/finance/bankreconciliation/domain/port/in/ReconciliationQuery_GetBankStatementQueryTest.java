package com.gogidix.finance.bankreconciliation.domain.port.in;

import com.gogidix.finance.bankreconciliation.domain.port.in.ReconciliationQuery;
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
class ReconciliationQuery_GetBankStatementQueryTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationQuery.GetBankStatementQuery dto = new ReconciliationQuery.GetBankStatementQuery();
        dto.setTenantId("val-tenantId");
        dto.setStatementId("val-statementId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-statementId", dto.getStatementId());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationQuery.GetBankStatementQuery dto1 = new ReconciliationQuery.GetBankStatementQuery();
        ReconciliationQuery.GetBankStatementQuery dto2 = new ReconciliationQuery.GetBankStatementQuery();
        dto1.setTenantId("test");
        dto1.setStatementId("test");
        dto2.setTenantId("test");
        dto2.setStatementId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationQuery.GetBankStatementQuery dto = new ReconciliationQuery.GetBankStatementQuery();
        dto.setTenantId("test");
        dto.setStatementId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationQuery.GetBankStatementQuery dto = new ReconciliationQuery.GetBankStatementQuery();
        dto.setTenantId("test");
        dto.setStatementId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}