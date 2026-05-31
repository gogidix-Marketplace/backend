package com.gogidix.finance.compliance.domain.port.in;

import com.gogidix.finance.compliance.domain.port.in.ComplianceQuery;
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
class ComplianceQuery_GetPendingRemediationQueryTest {

        @Test
    void testSettersAndGetters() {
        ComplianceQuery.GetPendingRemediationQuery dto = new ComplianceQuery.GetPendingRemediationQuery();
        dto.setTenantId("val-tenantId");
        dto.setAssignedTo("val-assignedTo");
        dto.setDueBefore(LocalDate.of(2025,6,1));
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals(LocalDate.of(2025,6,1), dto.getDueBefore());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceQuery.GetPendingRemediationQuery dto1 = new ComplianceQuery.GetPendingRemediationQuery();
        ComplianceQuery.GetPendingRemediationQuery dto2 = new ComplianceQuery.GetPendingRemediationQuery();
        dto1.setTenantId("test");
        dto1.setAssignedTo("test");
        dto1.setDueBefore(LocalDate.of(2025,1,1));
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setAssignedTo("test");
        dto2.setDueBefore(LocalDate.of(2025,1,1));
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceQuery.GetPendingRemediationQuery dto = new ComplianceQuery.GetPendingRemediationQuery();
        dto.setTenantId("test");
        dto.setAssignedTo("test");
        dto.setDueBefore(LocalDate.of(2025,1,1));
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceQuery.GetPendingRemediationQuery dto = new ComplianceQuery.GetPendingRemediationQuery();
        dto.setTenantId("test");
        dto.setAssignedTo("test");
        dto.setDueBefore(LocalDate.of(2025,1,1));
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}