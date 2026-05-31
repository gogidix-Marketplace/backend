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
class IssueCommand_CreateIssueCommandTest {

        @Test
    void testSettersAndGetters() {
        IssueCommand.CreateIssueCommand dto = new IssueCommand.CreateIssueCommand();
        dto.setTenantId("val-tenantId");
        dto.setRequirementId("val-requirementId");
        dto.setCheckId("val-checkId");
        dto.setCountryCode("val-countryCode");
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setSeverity("val-severity");
        dto.setIdentifiedBy("val-identifiedBy");
        dto.setIdentifiedByName("val-identifiedByName");
        dto.setCurrency("val-currency");
        dto.setDepartment("val-department");
        dto.setLocation("val-location");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requirementId", dto.getRequirementId());
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-severity", dto.getSeverity());
        assertEquals("val-identifiedBy", dto.getIdentifiedBy());
        assertEquals("val-identifiedByName", dto.getIdentifiedByName());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-location", dto.getLocation());
    }

    @Test
    void testEqualsAndHashCode() {
        IssueCommand.CreateIssueCommand dto1 = new IssueCommand.CreateIssueCommand();
        IssueCommand.CreateIssueCommand dto2 = new IssueCommand.CreateIssueCommand();
        dto1.setTenantId("test");
        dto1.setRequirementId("test");
        dto1.setCheckId("test");
        dto1.setCountryCode("test");
        dto1.setTitle("test");
        dto1.setDescription("test");
        dto1.setSeverity("test");
        dto1.setIdentifiedBy("test");
        dto1.setIdentifiedByName("test");
        dto1.setAffectedEmployees(Collections.emptyList());
        dto1.setFinancialImpact(null);
        dto1.setCurrency("test");
        dto1.setDepartment("test");
        dto1.setLocation("test");
        dto2.setTenantId("test");
        dto2.setRequirementId("test");
        dto2.setCheckId("test");
        dto2.setCountryCode("test");
        dto2.setTitle("test");
        dto2.setDescription("test");
        dto2.setSeverity("test");
        dto2.setIdentifiedBy("test");
        dto2.setIdentifiedByName("test");
        dto2.setAffectedEmployees(Collections.emptyList());
        dto2.setFinancialImpact(null);
        dto2.setCurrency("test");
        dto2.setDepartment("test");
        dto2.setLocation("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        IssueCommand.CreateIssueCommand dto = new IssueCommand.CreateIssueCommand();
        dto.setTenantId("test");
        dto.setRequirementId("test");
        dto.setCheckId("test");
        dto.setCountryCode("test");
        dto.setTitle("test");
        dto.setDescription("test");
        dto.setSeverity("test");
        dto.setIdentifiedBy("test");
        dto.setIdentifiedByName("test");
        dto.setAffectedEmployees(Collections.emptyList());
        dto.setFinancialImpact(null);
        dto.setCurrency("test");
        dto.setDepartment("test");
        dto.setLocation("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        IssueCommand.CreateIssueCommand dto = new IssueCommand.CreateIssueCommand();
        dto.setTenantId("test");
        dto.setRequirementId("test");
        dto.setCheckId("test");
        dto.setCountryCode("test");
        dto.setTitle("test");
        dto.setDescription("test");
        dto.setSeverity("test");
        dto.setIdentifiedBy("test");
        dto.setIdentifiedByName("test");
        dto.setAffectedEmployees(Collections.emptyList());
        dto.setFinancialImpact(null);
        dto.setCurrency("test");
        dto.setDepartment("test");
        dto.setLocation("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}