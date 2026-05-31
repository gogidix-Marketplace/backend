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
class ComplianceCheckCommand_CreateCheckCommandTest {

        @Test
    void testSettersAndGetters() {
        ComplianceCheckCommand.CreateCheckCommand dto = new ComplianceCheckCommand.CreateCheckCommand();
        dto.setTenantId("val-tenantId");
        dto.setRuleId("val-ruleId");
        dto.setRuleName("val-ruleName");
        dto.setEntityType("val-entityType");
        dto.setEntityId("val-entityId");
        dto.setReferenceNumber("val-referenceNumber");
        dto.setEvaluatedByUserId("val-evaluatedByUserId");
        dto.setEvaluatedAmount(BigDecimal.ONE);
        dto.setEvaluatedCurrency("val-evaluatedCurrency");
        dto.setThresholdAmount(BigDecimal.ONE);
        dto.setThresholdCurrency("val-thresholdCurrency");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setExpenseCategory("val-expenseCategory");
        dto.setCorrelationId("val-correlationId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ruleId", dto.getRuleId());
        assertEquals("val-ruleName", dto.getRuleName());
        assertEquals("val-entityType", dto.getEntityType());
        assertEquals("val-entityId", dto.getEntityId());
        assertEquals("val-referenceNumber", dto.getReferenceNumber());
        assertEquals("val-evaluatedByUserId", dto.getEvaluatedByUserId());
        assertEquals(BigDecimal.ONE, dto.getEvaluatedAmount());
        assertEquals("val-evaluatedCurrency", dto.getEvaluatedCurrency());
        assertEquals(BigDecimal.ONE, dto.getThresholdAmount());
        assertEquals("val-thresholdCurrency", dto.getThresholdCurrency());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-expenseCategory", dto.getExpenseCategory());
        assertEquals("val-correlationId", dto.getCorrelationId());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceCheckCommand.CreateCheckCommand dto1 = new ComplianceCheckCommand.CreateCheckCommand();
        ComplianceCheckCommand.CreateCheckCommand dto2 = new ComplianceCheckCommand.CreateCheckCommand();
        dto1.setTenantId("test");
        dto1.setRuleId("test");
        dto1.setRuleName("test");
        dto1.setEntityType("test");
        dto1.setEntityId("test");
        dto1.setReferenceNumber("test");
        dto1.setEvaluatedByUserId("test");
        dto1.setContext(Collections.emptyMap());
        dto1.setEvaluatedAmount(BigDecimal.TEN);
        dto1.setEvaluatedCurrency("test");
        dto1.setThresholdAmount(BigDecimal.TEN);
        dto1.setThresholdCurrency("test");
        dto1.setDepartment("test");
        dto1.setCostCenter("test");
        dto1.setExpenseCategory("test");
        dto1.setCorrelationId("test");
        dto2.setTenantId("test");
        dto2.setRuleId("test");
        dto2.setRuleName("test");
        dto2.setEntityType("test");
        dto2.setEntityId("test");
        dto2.setReferenceNumber("test");
        dto2.setEvaluatedByUserId("test");
        dto2.setContext(Collections.emptyMap());
        dto2.setEvaluatedAmount(BigDecimal.TEN);
        dto2.setEvaluatedCurrency("test");
        dto2.setThresholdAmount(BigDecimal.TEN);
        dto2.setThresholdCurrency("test");
        dto2.setDepartment("test");
        dto2.setCostCenter("test");
        dto2.setExpenseCategory("test");
        dto2.setCorrelationId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceCheckCommand.CreateCheckCommand dto = new ComplianceCheckCommand.CreateCheckCommand();
        dto.setTenantId("test");
        dto.setRuleId("test");
        dto.setRuleName("test");
        dto.setEntityType("test");
        dto.setEntityId("test");
        dto.setReferenceNumber("test");
        dto.setEvaluatedByUserId("test");
        dto.setContext(Collections.emptyMap());
        dto.setEvaluatedAmount(BigDecimal.TEN);
        dto.setEvaluatedCurrency("test");
        dto.setThresholdAmount(BigDecimal.TEN);
        dto.setThresholdCurrency("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        dto.setExpenseCategory("test");
        dto.setCorrelationId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceCheckCommand.CreateCheckCommand dto = new ComplianceCheckCommand.CreateCheckCommand();
        dto.setTenantId("test");
        dto.setRuleId("test");
        dto.setRuleName("test");
        dto.setEntityType("test");
        dto.setEntityId("test");
        dto.setReferenceNumber("test");
        dto.setEvaluatedByUserId("test");
        dto.setContext(Collections.emptyMap());
        dto.setEvaluatedAmount(BigDecimal.TEN);
        dto.setEvaluatedCurrency("test");
        dto.setThresholdAmount(BigDecimal.TEN);
        dto.setThresholdCurrency("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        dto.setExpenseCategory("test");
        dto.setCorrelationId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}