package com.gogidix.hr.globalcompliance.interfaces.rest;

import com.gogidix.hr.globalcompliance.interfaces.rest.IssueController;
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
class IssueController_UpdateSeverityRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        IssueController.UpdateSeverityRequestDto dto = new IssueController.UpdateSeverityRequestDto();
        dto.setNewSeverity("val-newSeverity");
        dto.setReason("val-reason");
        assertEquals("val-newSeverity", dto.getNewSeverity());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        IssueController.UpdateSeverityRequestDto dto1 = new IssueController.UpdateSeverityRequestDto();
        IssueController.UpdateSeverityRequestDto dto2 = new IssueController.UpdateSeverityRequestDto();
        dto1.setNewSeverity("test");
        dto1.setReason("test");
        dto2.setNewSeverity("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setNewSeverity(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        IssueController.UpdateSeverityRequestDto dto = new IssueController.UpdateSeverityRequestDto();
        dto.setNewSeverity("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        IssueController.UpdateSeverityRequestDto dto = new IssueController.UpdateSeverityRequestDto();
        dto.setNewSeverity("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}