package com.gogidix.finance.compliance.interfaces.rest;

import com.gogidix.finance.compliance.domain.model.ComplianceCheck;
import com.gogidix.finance.compliance.interfaces.rest.ComplianceCheckController;
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
class ComplianceCheckController_ExecuteCheckRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ComplianceCheckController.ExecuteCheckRequestDto dto = new ComplianceCheckController.ExecuteCheckRequestDto();
        dto.setViolationDescription("val-violationDescription");
        dto.setVariance(BigDecimal.ONE);
        assertEquals("val-violationDescription", dto.getViolationDescription());
        assertEquals(BigDecimal.ONE, dto.getVariance());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceCheckController.ExecuteCheckRequestDto dto1 = new ComplianceCheckController.ExecuteCheckRequestDto();
        ComplianceCheckController.ExecuteCheckRequestDto dto2 = new ComplianceCheckController.ExecuteCheckRequestDto();
        dto1.setResult(ComplianceCheck.CheckResult.COMPLIANT);
        dto1.setViolationDescription("test");
        dto1.setSeverity(ComplianceCheck.SeverityLevel.INFO);
        dto1.setContext(Collections.emptyMap());
        dto1.setVariance(BigDecimal.TEN);
        dto2.setResult(ComplianceCheck.CheckResult.COMPLIANT);
        dto2.setViolationDescription("test");
        dto2.setSeverity(ComplianceCheck.SeverityLevel.INFO);
        dto2.setContext(Collections.emptyMap());
        dto2.setVariance(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setResult(ComplianceCheck.CheckResult.NON_COMPLIANT);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceCheckController.ExecuteCheckRequestDto dto = new ComplianceCheckController.ExecuteCheckRequestDto();
        dto.setResult(ComplianceCheck.CheckResult.COMPLIANT);
        dto.setViolationDescription("test");
        dto.setSeverity(ComplianceCheck.SeverityLevel.INFO);
        dto.setContext(Collections.emptyMap());
        dto.setVariance(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceCheckController.ExecuteCheckRequestDto dto = new ComplianceCheckController.ExecuteCheckRequestDto();
        dto.setResult(ComplianceCheck.CheckResult.COMPLIANT);
        dto.setViolationDescription("test");
        dto.setSeverity(ComplianceCheck.SeverityLevel.INFO);
        dto.setContext(Collections.emptyMap());
        dto.setVariance(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}