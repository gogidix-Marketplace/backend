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
class ComplianceCheckController_ApproveCheckRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ComplianceCheckController.ApproveCheckRequestDto dto = new ComplianceCheckController.ApproveCheckRequestDto();
        dto.setNotes("val-notes");
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceCheckController.ApproveCheckRequestDto dto1 = new ComplianceCheckController.ApproveCheckRequestDto();
        ComplianceCheckController.ApproveCheckRequestDto dto2 = new ComplianceCheckController.ApproveCheckRequestDto();
        dto1.setNotes("test");
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setNotes(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ComplianceCheckController.ApproveCheckRequestDto dto = new ComplianceCheckController.ApproveCheckRequestDto();
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ComplianceCheckController.ApproveCheckRequestDto dto = new ComplianceCheckController.ApproveCheckRequestDto();
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}