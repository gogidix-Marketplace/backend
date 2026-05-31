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
class ReconciliationCommand_VerifyMatchCommandTest {

        @Test
    void testSettersAndGetters() {
        ReconciliationCommand.VerifyMatchCommand dto = new ReconciliationCommand.VerifyMatchCommand();
        dto.setTenantId("val-tenantId");
        dto.setReconciliationLineId("val-reconciliationLineId");
        dto.setVerifiedBy("val-verifiedBy");
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reconciliationLineId", dto.getReconciliationLineId());
        assertEquals("val-verifiedBy", dto.getVerifiedBy());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ReconciliationCommand.VerifyMatchCommand dto1 = new ReconciliationCommand.VerifyMatchCommand();
        ReconciliationCommand.VerifyMatchCommand dto2 = new ReconciliationCommand.VerifyMatchCommand();
        dto1.setTenantId("test");
        dto1.setReconciliationLineId("test");
        dto1.setVerifiedBy("test");
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setReconciliationLineId("test");
        dto2.setVerifiedBy("test");
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReconciliationCommand.VerifyMatchCommand dto = new ReconciliationCommand.VerifyMatchCommand();
        dto.setTenantId("test");
        dto.setReconciliationLineId("test");
        dto.setVerifiedBy("test");
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReconciliationCommand.VerifyMatchCommand dto = new ReconciliationCommand.VerifyMatchCommand();
        dto.setTenantId("test");
        dto.setReconciliationLineId("test");
        dto.setVerifiedBy("test");
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}