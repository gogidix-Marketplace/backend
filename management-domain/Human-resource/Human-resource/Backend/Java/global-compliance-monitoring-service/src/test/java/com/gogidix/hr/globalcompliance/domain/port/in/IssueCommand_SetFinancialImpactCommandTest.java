package com.gogidix.hr.globalcompliance.domain.port.in;

import com.gogidix.hr.globalcompliance.domain.port.in.IssueCommand;
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
class IssueCommand_SetFinancialImpactCommandTest {

        @Test
    void testSettersAndGetters() {
        IssueCommand.SetFinancialImpactCommand dto = new IssueCommand.SetFinancialImpactCommand();
        dto.setTenantId("val-tenantId");
        dto.setIssueId("val-issueId");
        dto.setCurrency("val-currency");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-issueId", dto.getIssueId());
        assertEquals("val-currency", dto.getCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        IssueCommand.SetFinancialImpactCommand dto1 = new IssueCommand.SetFinancialImpactCommand();
        IssueCommand.SetFinancialImpactCommand dto2 = new IssueCommand.SetFinancialImpactCommand();
        dto1.setTenantId("test");
        dto1.setIssueId("test");
        dto1.setFinancialImpact(null);
        dto1.setCurrency("test");
        dto2.setTenantId("test");
        dto2.setIssueId("test");
        dto2.setFinancialImpact(null);
        dto2.setCurrency("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        IssueCommand.SetFinancialImpactCommand dto = new IssueCommand.SetFinancialImpactCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setFinancialImpact(null);
        dto.setCurrency("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        IssueCommand.SetFinancialImpactCommand dto = new IssueCommand.SetFinancialImpactCommand();
        dto.setTenantId("test");
        dto.setIssueId("test");
        dto.setFinancialImpact(null);
        dto.setCurrency("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}