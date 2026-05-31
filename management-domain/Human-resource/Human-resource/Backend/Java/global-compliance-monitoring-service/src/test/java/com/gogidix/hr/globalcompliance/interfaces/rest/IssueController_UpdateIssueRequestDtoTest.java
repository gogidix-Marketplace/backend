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
class IssueController_UpdateIssueRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        IssueController.UpdateIssueRequestDto dto = new IssueController.UpdateIssueRequestDto();
        dto.setAssignedTo("val-assignedTo");
        dto.setAssignedToName("val-assignedToName");
        dto.setCurrency("val-currency");
        dto.setAction("val-action");
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals("val-assignedToName", dto.getAssignedToName());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-action", dto.getAction());
    }

    @Test
    void testEqualsAndHashCode() {
        IssueController.UpdateIssueRequestDto dto1 = new IssueController.UpdateIssueRequestDto();
        IssueController.UpdateIssueRequestDto dto2 = new IssueController.UpdateIssueRequestDto();
        dto1.setAssignedTo("test");
        dto1.setAssignedToName("test");
        dto1.setFinancialImpact(null);
        dto1.setCurrency("test");
        dto1.setAction("test");
        dto2.setAssignedTo("test");
        dto2.setAssignedToName("test");
        dto2.setFinancialImpact(null);
        dto2.setCurrency("test");
        dto2.setAction("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setAssignedTo(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        IssueController.UpdateIssueRequestDto dto = new IssueController.UpdateIssueRequestDto();
        dto.setAssignedTo("test");
        dto.setAssignedToName("test");
        dto.setFinancialImpact(null);
        dto.setCurrency("test");
        dto.setAction("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        IssueController.UpdateIssueRequestDto dto = new IssueController.UpdateIssueRequestDto();
        dto.setAssignedTo("test");
        dto.setAssignedToName("test");
        dto.setFinancialImpact(null);
        dto.setCurrency("test");
        dto.setAction("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}