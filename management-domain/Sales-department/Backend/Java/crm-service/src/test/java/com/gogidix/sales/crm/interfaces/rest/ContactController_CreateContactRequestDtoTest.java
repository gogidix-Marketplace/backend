package com.gogidix.sales.crm.interfaces.rest;

import com.gogidix.sales.crm.domain.model.Contact;
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
class ContactController_CreateContactRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ContactController.CreateContactRequestDto dto = new ContactController.CreateContactRequestDto();
        dto.setCustomerId("val-customerId");
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
        dto.setEmail("val-email");
        dto.setTitle("val-title");
        dto.setDepartment("val-department");
        dto.setPhone("val-phone");
        dto.setMobilePhone("val-mobilePhone");
        dto.setAlternatePhone("val-alternatePhone");
        dto.setIsPrimary(true);
        dto.setIsDecisionMaker(true);
        dto.setLinkedInUrl("val-linkedInUrl");
        dto.setTimezone("val-timezone");
        dto.setPreferredContactMethod("val-preferredContactMethod");
        dto.setAssistantName("val-assistantName");
        dto.setAssistantPhone("val-assistantPhone");
        dto.setAssistantEmail("val-assistantEmail");
        dto.setReportsToContactId("val-reportsToContactId");
        dto.setBirthDate(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        dto.setAddressStreet("val-addressStreet");
        dto.setAddressCity("val-addressCity");
        dto.setAddressState("val-addressState");
        dto.setAddressPostalCode("val-addressPostalCode");
        dto.setAddressCountry("val-addressCountry");
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-phone", dto.getPhone());
        assertEquals("val-mobilePhone", dto.getMobilePhone());
        assertEquals("val-alternatePhone", dto.getAlternatePhone());
        assertTrue(dto.getIsPrimary());
        assertTrue(dto.getIsDecisionMaker());
        assertEquals("val-linkedInUrl", dto.getLinkedInUrl());
        assertEquals("val-timezone", dto.getTimezone());
        assertEquals("val-preferredContactMethod", dto.getPreferredContactMethod());
        assertEquals("val-assistantName", dto.getAssistantName());
        assertEquals("val-assistantPhone", dto.getAssistantPhone());
        assertEquals("val-assistantEmail", dto.getAssistantEmail());
        assertEquals("val-reportsToContactId", dto.getReportsToContactId());
        assertEquals(LocalDate.of(2025,6,1), dto.getBirthDate());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-addressStreet", dto.getAddressStreet());
        assertEquals("val-addressCity", dto.getAddressCity());
        assertEquals("val-addressState", dto.getAddressState());
        assertEquals("val-addressPostalCode", dto.getAddressPostalCode());
        assertEquals("val-addressCountry", dto.getAddressCountry());
    }

    @Test
    void testEqualsAndHashCode() {
        ContactController.CreateContactRequestDto dto1 = new ContactController.CreateContactRequestDto();
        ContactController.CreateContactRequestDto dto2 = new ContactController.CreateContactRequestDto();
        dto1.setCustomerId("test");
        dto1.setFirstName("test");
        dto1.setLastName("test");
        dto1.setEmail("test");
        dto1.setTitle("test");
        dto1.setDepartment("test");
        dto1.setContactType(Contact.ContactType.DECISION_MAKER);
        dto1.setPhone("test");
        dto1.setMobilePhone("test");
        dto1.setAlternatePhone("test");
        dto1.setIsPrimary(true);
        dto1.setIsDecisionMaker(true);
        dto1.setLinkedInUrl("test");
        dto1.setTimezone("test");
        dto1.setPreferredContactMethod("test");
        dto1.setAssistantName("test");
        dto1.setAssistantPhone("test");
        dto1.setAssistantEmail("test");
        dto1.setReportsToContactId("test");
        dto1.setBirthDate(LocalDate.of(2025,1,1));
        dto1.setNotes("test");
        dto1.setTags(Collections.emptyList());
        dto1.setAddressStreet("test");
        dto1.setAddressCity("test");
        dto1.setAddressState("test");
        dto1.setAddressPostalCode("test");
        dto1.setAddressCountry("test");
        dto2.setCustomerId("test");
        dto2.setFirstName("test");
        dto2.setLastName("test");
        dto2.setEmail("test");
        dto2.setTitle("test");
        dto2.setDepartment("test");
        dto2.setContactType(Contact.ContactType.DECISION_MAKER);
        dto2.setPhone("test");
        dto2.setMobilePhone("test");
        dto2.setAlternatePhone("test");
        dto2.setIsPrimary(true);
        dto2.setIsDecisionMaker(true);
        dto2.setLinkedInUrl("test");
        dto2.setTimezone("test");
        dto2.setPreferredContactMethod("test");
        dto2.setAssistantName("test");
        dto2.setAssistantPhone("test");
        dto2.setAssistantEmail("test");
        dto2.setReportsToContactId("test");
        dto2.setBirthDate(LocalDate.of(2025,1,1));
        dto2.setNotes("test");
        dto2.setTags(Collections.emptyList());
        dto2.setAddressStreet("test");
        dto2.setAddressCity("test");
        dto2.setAddressState("test");
        dto2.setAddressPostalCode("test");
        dto2.setAddressCountry("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setCustomerId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ContactController.CreateContactRequestDto dto = new ContactController.CreateContactRequestDto();
        dto.setCustomerId("test");
        dto.setFirstName("test");
        dto.setLastName("test");
        dto.setEmail("test");
        dto.setTitle("test");
        dto.setDepartment("test");
        dto.setContactType(Contact.ContactType.DECISION_MAKER);
        dto.setPhone("test");
        dto.setMobilePhone("test");
        dto.setAlternatePhone("test");
        dto.setIsPrimary(true);
        dto.setIsDecisionMaker(true);
        dto.setLinkedInUrl("test");
        dto.setTimezone("test");
        dto.setPreferredContactMethod("test");
        dto.setAssistantName("test");
        dto.setAssistantPhone("test");
        dto.setAssistantEmail("test");
        dto.setReportsToContactId("test");
        dto.setBirthDate(LocalDate.of(2025,1,1));
        dto.setNotes("test");
        dto.setTags(Collections.emptyList());
        dto.setAddressStreet("test");
        dto.setAddressCity("test");
        dto.setAddressState("test");
        dto.setAddressPostalCode("test");
        dto.setAddressCountry("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ContactController.CreateContactRequestDto dto = new ContactController.CreateContactRequestDto();
        dto.setCustomerId("test");
        dto.setFirstName("test");
        dto.setLastName("test");
        dto.setEmail("test");
        dto.setTitle("test");
        dto.setDepartment("test");
        dto.setContactType(Contact.ContactType.DECISION_MAKER);
        dto.setPhone("test");
        dto.setMobilePhone("test");
        dto.setAlternatePhone("test");
        dto.setIsPrimary(true);
        dto.setIsDecisionMaker(true);
        dto.setLinkedInUrl("test");
        dto.setTimezone("test");
        dto.setPreferredContactMethod("test");
        dto.setAssistantName("test");
        dto.setAssistantPhone("test");
        dto.setAssistantEmail("test");
        dto.setReportsToContactId("test");
        dto.setBirthDate(LocalDate.of(2025,1,1));
        dto.setNotes("test");
        dto.setTags(Collections.emptyList());
        dto.setAddressStreet("test");
        dto.setAddressCity("test");
        dto.setAddressState("test");
        dto.setAddressPostalCode("test");
        dto.setAddressCountry("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}