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
class ComplianceCheckController_AssignRemediationRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ComplianceCheckController.AssignRemediationRequestDto dto = new ComplianceCheckController.AssignRemediationRequestDto();
        dto.setAssignedTo("val-assignedTo");
        dto.setAction("val-action");
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals("val-action", dto.getAction());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceCheckController.AssignRemediationRequestDto dto1 = new ComplianceCheckController.AssignRemediationRequestDto();
        ComplianceCheckController.AssignRemediationRequestDto dto2 = new ComplianceCheckController.AssignRemediationRequestDto();
        dto1.setAssignedTo("test");
        dto1.setAction("test");
        dto1.setDueDate(null);
        dto2.setAssignedTo("test");
        dto2.setAction("test");
        dto2.setDueDate(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setAssignedTo(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceCheckController.AssignRemediationRequestDto dto = new ComplianceCheckController.AssignRemediationRequestDto();
        dto.setAssignedTo("test");
        dto.setAction("test");
        dto.setDueDate(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceCheckController.AssignRemediationRequestDto dto = new ComplianceCheckController.AssignRemediationRequestDto();
        dto.setAssignedTo("test");
        dto.setAction("test");
        dto.setDueDate(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}