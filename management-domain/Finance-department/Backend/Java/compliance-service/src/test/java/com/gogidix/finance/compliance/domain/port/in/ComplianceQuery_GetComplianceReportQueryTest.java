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
class ComplianceQuery_GetComplianceReportQueryTest {

        @Test
    void testSettersAndGetters() {
        ComplianceQuery.GetComplianceReportQuery dto = new ComplianceQuery.GetComplianceReportQuery();
        dto.setTenantId("val-tenantId");
        dto.setReportId("val-reportId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reportId", dto.getReportId());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceQuery.GetComplianceReportQuery dto1 = new ComplianceQuery.GetComplianceReportQuery();
        ComplianceQuery.GetComplianceReportQuery dto2 = new ComplianceQuery.GetComplianceReportQuery();
        dto1.setTenantId("test");
        dto1.setReportId("test");
        dto2.setTenantId("test");
        dto2.setReportId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceQuery.GetComplianceReportQuery dto = new ComplianceQuery.GetComplianceReportQuery();
        dto.setTenantId("test");
        dto.setReportId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceQuery.GetComplianceReportQuery dto = new ComplianceQuery.GetComplianceReportQuery();
        dto.setTenantId("test");
        dto.setReportId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}