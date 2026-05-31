package com.gogidix.finance.compliance.interfaces.rest;

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
class ComplianceCheckController_EvaluateRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ComplianceCheckController.EvaluateRequestDto dto = new ComplianceCheckController.EvaluateRequestDto();
        dto.setRuleId("val-ruleId");
        dto.setEntityType("val-entityType");
        dto.setEntityId("val-entityId");
        assertEquals("val-ruleId", dto.getRuleId());
        assertEquals("val-entityType", dto.getEntityType());
        assertEquals("val-entityId", dto.getEntityId());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceCheckController.EvaluateRequestDto dto1 = new ComplianceCheckController.EvaluateRequestDto();
        ComplianceCheckController.EvaluateRequestDto dto2 = new ComplianceCheckController.EvaluateRequestDto();
        dto1.setRuleId("test");
        dto1.setEntityType("test");
        dto1.setEntityId("test");
        dto1.setContext(Collections.emptyMap());
        dto2.setRuleId("test");
        dto2.setEntityType("test");
        dto2.setEntityId("test");
        dto2.setContext(Collections.emptyMap());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setRuleId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceCheckController.EvaluateRequestDto dto = new ComplianceCheckController.EvaluateRequestDto();
        dto.setRuleId("test");
        dto.setEntityType("test");
        dto.setEntityId("test");
        dto.setContext(Collections.emptyMap());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceCheckController.EvaluateRequestDto dto = new ComplianceCheckController.EvaluateRequestDto();
        dto.setRuleId("test");
        dto.setEntityType("test");
        dto.setEntityId("test");
        dto.setContext(Collections.emptyMap());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}