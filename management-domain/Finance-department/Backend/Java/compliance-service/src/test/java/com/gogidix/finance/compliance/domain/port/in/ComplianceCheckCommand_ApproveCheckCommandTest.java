package com.gogidix.finance.compliance.domain.port.in;

import com.gogidix.finance.compliance.domain.port.in.ComplianceCheckCommand;
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
class ComplianceCheckCommand_ApproveCheckCommandTest {

        @Test
    void testSettersAndGetters() {
        ComplianceCheckCommand.ApproveCheckCommand dto = new ComplianceCheckCommand.ApproveCheckCommand();
        dto.setTenantId("val-tenantId");
        dto.setCheckId("val-checkId");
        dto.setApprovedBy("val-approvedBy");
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceCheckCommand.ApproveCheckCommand dto1 = new ComplianceCheckCommand.ApproveCheckCommand();
        ComplianceCheckCommand.ApproveCheckCommand dto2 = new ComplianceCheckCommand.ApproveCheckCommand();
        dto1.setTenantId("test");
        dto1.setCheckId("test");
        dto1.setApprovedBy("test");
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setCheckId("test");
        dto2.setApprovedBy("test");
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceCheckCommand.ApproveCheckCommand dto = new ComplianceCheckCommand.ApproveCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setApprovedBy("test");
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceCheckCommand.ApproveCheckCommand dto = new ComplianceCheckCommand.ApproveCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setApprovedBy("test");
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}