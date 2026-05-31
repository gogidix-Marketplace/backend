package com.gogidix.sales.leadmanagement.interfaces.rest;

import com.gogidix.sales.leadmanagement.interfaces.rest.LeadController;
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
class LeadController_AssignLeadRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        LeadController.AssignLeadRequestDto dto = new LeadController.AssignLeadRequestDto();
        dto.setOwnerId("val-ownerId");
        dto.setOwnerName("val-ownerName");
        dto.setReason("val-reason");
        dto.setAssignmentStrategy("val-assignmentStrategy");
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals("val-ownerName", dto.getOwnerName());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-assignmentStrategy", dto.getAssignmentStrategy());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadController.AssignLeadRequestDto dto1 = new LeadController.AssignLeadRequestDto();
        LeadController.AssignLeadRequestDto dto2 = new LeadController.AssignLeadRequestDto();
        dto1.setOwnerId("test");
        dto1.setOwnerName("test");
        dto1.setReason("test");
        dto1.setAssignmentStrategy("test");
        dto2.setOwnerId("test");
        dto2.setOwnerName("test");
        dto2.setReason("test");
        dto2.setAssignmentStrategy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setOwnerId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadController.AssignLeadRequestDto dto = new LeadController.AssignLeadRequestDto();
        dto.setOwnerId("test");
        dto.setOwnerName("test");
        dto.setReason("test");
        dto.setAssignmentStrategy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadController.AssignLeadRequestDto dto = new LeadController.AssignLeadRequestDto();
        dto.setOwnerId("test");
        dto.setOwnerName("test");
        dto.setReason("test");
        dto.setAssignmentStrategy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}