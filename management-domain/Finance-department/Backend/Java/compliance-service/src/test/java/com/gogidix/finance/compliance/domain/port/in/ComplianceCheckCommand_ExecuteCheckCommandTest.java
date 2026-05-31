package com.gogidix.finance.compliance.domain.port.in;

import com.gogidix.finance.compliance.domain.model.ComplianceCheck;
import com.gogidix.finance.compliance.domain.model.ComplianceRule;
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
class ComplianceCheckCommand_ExecuteCheckCommandTest {

        @Test
    void testSettersAndGetters() {
        ComplianceCheckCommand.ExecuteCheckCommand dto = new ComplianceCheckCommand.ExecuteCheckCommand();
        dto.setTenantId("val-tenantId");
        dto.setCheckId("val-checkId");
        dto.setEvaluatedBy("val-evaluatedBy");
        dto.setViolationDescription("val-violationDescription");
        dto.setVariance(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals("val-evaluatedBy", dto.getEvaluatedBy());
        assertEquals("val-violationDescription", dto.getViolationDescription());
        assertEquals(BigDecimal.ONE, dto.getVariance());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceCheckCommand.ExecuteCheckCommand dto1 = new ComplianceCheckCommand.ExecuteCheckCommand();
        ComplianceCheckCommand.ExecuteCheckCommand dto2 = new ComplianceCheckCommand.ExecuteCheckCommand();
        dto1.setTenantId("test");
        dto1.setCheckId("test");
        dto1.setEvaluatedBy("test");
        dto1.setResult(ComplianceCheck.CheckResult.COMPLIANT);
        dto1.setViolationDescription("test");
        dto1.setSeverity(ComplianceRule.SeverityLevel.INFO);
        dto1.setContext(Collections.emptyMap());
        dto1.setVariance(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setCheckId("test");
        dto2.setEvaluatedBy("test");
        dto2.setResult(ComplianceCheck.CheckResult.COMPLIANT);
        dto2.setViolationDescription("test");
        dto2.setSeverity(ComplianceRule.SeverityLevel.INFO);
        dto2.setContext(Collections.emptyMap());
        dto2.setVariance(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceCheckCommand.ExecuteCheckCommand dto = new ComplianceCheckCommand.ExecuteCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setEvaluatedBy("test");
        dto.setResult(ComplianceCheck.CheckResult.COMPLIANT);
        dto.setViolationDescription("test");
        dto.setSeverity(ComplianceRule.SeverityLevel.INFO);
        dto.setContext(Collections.emptyMap());
        dto.setVariance(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceCheckCommand.ExecuteCheckCommand dto = new ComplianceCheckCommand.ExecuteCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setEvaluatedBy("test");
        dto.setResult(ComplianceCheck.CheckResult.COMPLIANT);
        dto.setViolationDescription("test");
        dto.setSeverity(ComplianceRule.SeverityLevel.INFO);
        dto.setContext(Collections.emptyMap());
        dto.setVariance(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}