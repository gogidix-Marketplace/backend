package com.gogidix.hr.leavemanagement.domain.port.in;

import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.domain.port.in.LeaveCommand;
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
class LeaveCommand_CreateLeavePolicyCommandTest {

        @Test
    void testBuilder() {
        LeaveCommand.CreateLeavePolicyCommand dto = LeaveCommand.CreateLeavePolicyCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .policyCode("test-policyCode")
            .policyName("test-policyName")
            .leaveType(LeaveType.ANNUAL)
            .description("test-description")
            .annualAllocation(null)
            .accrualRate(null)
            .accrualFrequency("test-accrualFrequency")
            .requiresApproval(true)
            .documentsRequired(true)
            .paidLeave(true)
            .carryForwardAllowed(true)
            .carryForwardLimit(null)
            .isActive(true)
            .effectiveFrom(LocalDate.of(2025,1,15))
            .createdBy("test-createdBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-policyCode", dto.getPolicyCode());
        assertEquals("test-policyName", dto.getPolicyName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-accrualFrequency", dto.getAccrualFrequency());
        assertTrue(dto.getRequiresApproval());
        assertTrue(dto.getDocumentsRequired());
        assertTrue(dto.getPaidLeave());
        assertTrue(dto.getCarryForwardAllowed());
        assertTrue(dto.getIsActive());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveFrom());
        assertEquals("test-createdBy", dto.getCreatedBy());
    }

    @Test
    void testSettersAndGetters() {
        LeaveCommand.CreateLeavePolicyCommand dto = new LeaveCommand.CreateLeavePolicyCommand();
        dto.setTenantId("val-tenantId");
        dto.setCountryCode("val-countryCode");
        dto.setPolicyCode("val-policyCode");
        dto.setPolicyName("val-policyName");
        dto.setDescription("val-description");
        dto.setAccrualFrequency("val-accrualFrequency");
        dto.setRequiresApproval(true);
        dto.setDocumentsRequired(true);
        dto.setPaidLeave(true);
        dto.setCarryForwardAllowed(true);
        dto.setIsActive(true);
        dto.setEffectiveFrom(LocalDate.of(2025,6,1));
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-policyCode", dto.getPolicyCode());
        assertEquals("val-policyName", dto.getPolicyName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-accrualFrequency", dto.getAccrualFrequency());
        assertTrue(dto.getRequiresApproval());
        assertTrue(dto.getDocumentsRequired());
        assertTrue(dto.getPaidLeave());
        assertTrue(dto.getCarryForwardAllowed());
        assertTrue(dto.getIsActive());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveFrom());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        LeaveCommand.CreateLeavePolicyCommand dto1 = LeaveCommand.CreateLeavePolicyCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .policyCode("test-policyCode")
            .policyName("test-policyName")
            .leaveType(LeaveType.ANNUAL)
            .description("test-description")
            .annualAllocation(null)
            .accrualRate(null)
            .accrualFrequency("test-accrualFrequency")
            .requiresApproval(true)
            .documentsRequired(true)
            .paidLeave(true)
            .carryForwardAllowed(true)
            .carryForwardLimit(null)
            .isActive(true)
            .effectiveFrom(LocalDate.of(2025,1,15))
            .createdBy("test-createdBy")
            .build();
        LeaveCommand.CreateLeavePolicyCommand dto2 = LeaveCommand.CreateLeavePolicyCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .policyCode("test-policyCode")
            .policyName("test-policyName")
            .leaveType(LeaveType.ANNUAL)
            .description("test-description")
            .annualAllocation(null)
            .accrualRate(null)
            .accrualFrequency("test-accrualFrequency")
            .requiresApproval(true)
            .documentsRequired(true)
            .paidLeave(true)
            .carryForwardAllowed(true)
            .carryForwardLimit(null)
            .isActive(true)
            .effectiveFrom(LocalDate.of(2025,1,15))
            .createdBy("test-createdBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeaveCommand.CreateLeavePolicyCommand dto = LeaveCommand.CreateLeavePolicyCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .policyCode("test-policyCode")
            .policyName("test-policyName")
            .leaveType(LeaveType.ANNUAL)
            .description("test-description")
            .annualAllocation(null)
            .accrualRate(null)
            .accrualFrequency("test-accrualFrequency")
            .requiresApproval(true)
            .documentsRequired(true)
            .paidLeave(true)
            .carryForwardAllowed(true)
            .carryForwardLimit(null)
            .isActive(true)
            .effectiveFrom(LocalDate.of(2025,1,15))
            .createdBy("test-createdBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}