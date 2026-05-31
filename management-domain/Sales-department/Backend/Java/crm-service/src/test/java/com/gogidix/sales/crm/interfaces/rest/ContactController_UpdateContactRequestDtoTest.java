package com.gogidix.sales.crm.interfaces.rest;

import com.gogidix.sales.crm.interfaces.rest.ContactController;
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
class ContactController_UpdateContactRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ContactController.UpdateContactRequestDto dto = new ContactController.UpdateContactRequestDto();
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
        dto.setEmail("val-email");
        dto.setPhone("val-phone");
        dto.setMobilePhone("val-mobilePhone");
        dto.setTitle("val-title");
        dto.setDepartment("val-department");
        dto.setAlternatePhone("val-alternatePhone");
        dto.setLinkedInUrl("val-linkedInUrl");
        dto.setTimezone("val-timezone");
        dto.setPreferredContactMethod("val-preferredContactMethod");
        dto.setNotes("val-notes");
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-phone", dto.getPhone());
        assertEquals("val-mobilePhone", dto.getMobilePhone());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-alternatePhone", dto.getAlternatePhone());
        assertEquals("val-linkedInUrl", dto.getLinkedInUrl());
        assertEquals("val-timezone", dto.getTimezone());
        assertEquals("val-preferredContactMethod", dto.getPreferredContactMethod());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ContactController.UpdateContactRequestDto dto1 = new ContactController.UpdateContactRequestDto();
        ContactController.UpdateContactRequestDto dto2 = new ContactController.UpdateContactRequestDto();
        dto1.setFirstName("test");
        dto1.setLastName("test");
        dto1.setEmail("test");
        dto1.setPhone("test");
        dto1.setMobilePhone("test");
        dto1.setTitle("test");
        dto1.setDepartment("test");
        dto1.setAlternatePhone("test");
        dto1.setLinkedInUrl("test");
        dto1.setTimezone("test");
        dto1.setPreferredContactMethod("test");
        dto1.setNotes("test");
        dto1.setTags(Collections.emptyList());
        dto2.setFirstName("test");
        dto2.setLastName("test");
        dto2.setEmail("test");
        dto2.setPhone("test");
        dto2.setMobilePhone("test");
        dto2.setTitle("test");
        dto2.setDepartment("test");
        dto2.setAlternatePhone("test");
        dto2.setLinkedInUrl("test");
        dto2.setTimezone("test");
        dto2.setPreferredContactMethod("test");
        dto2.setNotes("test");
        dto2.setTags(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFirstName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ContactController.UpdateContactRequestDto dto = new ContactController.UpdateContactRequestDto();
        dto.setFirstName("test");
        dto.setLastName("test");
        dto.setEmail("test");
        dto.setPhone("test");
        dto.setMobilePhone("test");
        dto.setTitle("test");
        dto.setDepartment("test");
        dto.setAlternatePhone("test");
        dto.setLinkedInUrl("test");
        dto.setTimezone("test");
        dto.setPreferredContactMethod("test");
        dto.setNotes("test");
        dto.setTags(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ContactController.UpdateContactRequestDto dto = new ContactController.UpdateContactRequestDto();
        dto.setFirstName("test");
        dto.setLastName("test");
        dto.setEmail("test");
        dto.setPhone("test");
        dto.setMobilePhone("test");
        dto.setTitle("test");
        dto.setDepartment("test");
        dto.setAlternatePhone("test");
        dto.setLinkedInUrl("test");
        dto.setTimezone("test");
        dto.setPreferredContactMethod("test");
        dto.setNotes("test");
        dto.setTags(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}