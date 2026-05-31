package com.gogidix.sales.crm.application.dto.response;

import com.gogidix.sales.crm.application.dto.response.ContactResponseDto;
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
class ContactResponseDtoTest {

        @Test
    void testBuilder() {
        ContactResponseDto dto = ContactResponseDto.builder()
                        .id("test-id")
            .contactId("test-contactId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .title("test-title")
            .department("test-department")
            .email("test-email")
            .phone("test-phone")
            .mobilePhone("test-mobilePhone")
            .alternatePhone("test-alternatePhone")
            .contactType(ContactResponseDto.ContactTypeDto.DECISION_MAKER)
            .isPrimary(true)
            .isDecisionMaker(true)
            .isActive(true)
            .linkedInUrl("test-linkedInUrl")
            .timezone("test-timezone")
            .preferredContactMethod("test-preferredContactMethod")
            .lastContactDate(LocalDate.of(2025,1,15))
            .interactionCount(42)
            .assistantName("test-assistantName")
            .assistantPhone("test-assistantPhone")
            .assistantEmail("test-assistantEmail")
            .reportsTo("test-reportsTo")
            .reportsToContactId("test-reportsToContactId")
            .birthDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .tags(Collections.emptyList())
            .address(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-contactId", dto.getContactId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-firstName", dto.getFirstName());
        assertEquals("test-lastName", dto.getLastName());
        assertEquals("test-fullName", dto.getFullName());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-email", dto.getEmail());
        assertEquals("test-phone", dto.getPhone());
        assertEquals("test-mobilePhone", dto.getMobilePhone());
        assertEquals("test-alternatePhone", dto.getAlternatePhone());
        assertEquals(ContactResponseDto.ContactTypeDto.DECISION_MAKER, dto.getContactType());
        assertTrue(dto.getIsPrimary());
        assertTrue(dto.getIsDecisionMaker());
        assertTrue(dto.getIsActive());
        assertEquals("test-linkedInUrl", dto.getLinkedInUrl());
        assertEquals("test-timezone", dto.getTimezone());
        assertEquals("test-preferredContactMethod", dto.getPreferredContactMethod());
        assertEquals(LocalDate.of(2025,1,15), dto.getLastContactDate());
        assertEquals(42, dto.getInteractionCount());
        assertEquals("test-assistantName", dto.getAssistantName());
        assertEquals("test-assistantPhone", dto.getAssistantPhone());
        assertEquals("test-assistantEmail", dto.getAssistantEmail());
        assertEquals("test-reportsTo", dto.getReportsTo());
        assertEquals("test-reportsToContactId", dto.getReportsToContactId());
        assertEquals(LocalDate.of(2025,1,15), dto.getBirthDate());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        ContactResponseDto dto = new ContactResponseDto();
        dto.setId("val-id");
        dto.setContactId("val-contactId");
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
        dto.setFullName("val-fullName");
        dto.setTitle("val-title");
        dto.setDepartment("val-department");
        dto.setEmail("val-email");
        dto.setPhone("val-phone");
        dto.setMobilePhone("val-mobilePhone");
        dto.setAlternatePhone("val-alternatePhone");
        dto.setContactType(ContactResponseDto.ContactTypeDto.DECISION_MAKER);
        dto.setIsPrimary(true);
        dto.setIsDecisionMaker(true);
        dto.setIsActive(true);
        dto.setLinkedInUrl("val-linkedInUrl");
        dto.setTimezone("val-timezone");
        dto.setPreferredContactMethod("val-preferredContactMethod");
        dto.setLastContactDate(LocalDate.of(2025,6,1));
        dto.setInteractionCount(99);
        dto.setAssistantName("val-assistantName");
        dto.setAssistantPhone("val-assistantPhone");
        dto.setAssistantEmail("val-assistantEmail");
        dto.setReportsTo("val-reportsTo");
        dto.setReportsToContactId("val-reportsToContactId");
        dto.setBirthDate(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        assertEquals("val-id", dto.getId());
        assertEquals("val-contactId", dto.getContactId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
        assertEquals("val-fullName", dto.getFullName());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-phone", dto.getPhone());
        assertEquals("val-mobilePhone", dto.getMobilePhone());
        assertEquals("val-alternatePhone", dto.getAlternatePhone());
        assertEquals(ContactResponseDto.ContactTypeDto.DECISION_MAKER, dto.getContactType());
        assertTrue(dto.getIsPrimary());
        assertTrue(dto.getIsDecisionMaker());
        assertTrue(dto.getIsActive());
        assertEquals("val-linkedInUrl", dto.getLinkedInUrl());
        assertEquals("val-timezone", dto.getTimezone());
        assertEquals("val-preferredContactMethod", dto.getPreferredContactMethod());
        assertEquals(LocalDate.of(2025,6,1), dto.getLastContactDate());
        assertEquals(99, dto.getInteractionCount());
        assertEquals("val-assistantName", dto.getAssistantName());
        assertEquals("val-assistantPhone", dto.getAssistantPhone());
        assertEquals("val-assistantEmail", dto.getAssistantEmail());
        assertEquals("val-reportsTo", dto.getReportsTo());
        assertEquals("val-reportsToContactId", dto.getReportsToContactId());
        assertEquals(LocalDate.of(2025,6,1), dto.getBirthDate());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ContactResponseDto dto1 = ContactResponseDto.builder()
                        .id("test-id")
            .contactId("test-contactId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .title("test-title")
            .department("test-department")
            .email("test-email")
            .phone("test-phone")
            .mobilePhone("test-mobilePhone")
            .alternatePhone("test-alternatePhone")
            .contactType(ContactResponseDto.ContactTypeDto.DECISION_MAKER)
            .isPrimary(true)
            .isDecisionMaker(true)
            .isActive(true)
            .linkedInUrl("test-linkedInUrl")
            .timezone("test-timezone")
            .preferredContactMethod("test-preferredContactMethod")
            .lastContactDate(LocalDate.of(2025,1,15))
            .interactionCount(42)
            .assistantName("test-assistantName")
            .assistantPhone("test-assistantPhone")
            .assistantEmail("test-assistantEmail")
            .reportsTo("test-reportsTo")
            .reportsToContactId("test-reportsToContactId")
            .birthDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .tags(Collections.emptyList())
            .address(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ContactResponseDto dto2 = ContactResponseDto.builder()
                        .id("test-id")
            .contactId("test-contactId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .title("test-title")
            .department("test-department")
            .email("test-email")
            .phone("test-phone")
            .mobilePhone("test-mobilePhone")
            .alternatePhone("test-alternatePhone")
            .contactType(ContactResponseDto.ContactTypeDto.DECISION_MAKER)
            .isPrimary(true)
            .isDecisionMaker(true)
            .isActive(true)
            .linkedInUrl("test-linkedInUrl")
            .timezone("test-timezone")
            .preferredContactMethod("test-preferredContactMethod")
            .lastContactDate(LocalDate.of(2025,1,15))
            .interactionCount(42)
            .assistantName("test-assistantName")
            .assistantPhone("test-assistantPhone")
            .assistantEmail("test-assistantEmail")
            .reportsTo("test-reportsTo")
            .reportsToContactId("test-reportsToContactId")
            .birthDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .tags(Collections.emptyList())
            .address(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ContactResponseDto dto = ContactResponseDto.builder()
                        .id("test-id")
            .contactId("test-contactId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .title("test-title")
            .department("test-department")
            .email("test-email")
            .phone("test-phone")
            .mobilePhone("test-mobilePhone")
            .alternatePhone("test-alternatePhone")
            .contactType(ContactResponseDto.ContactTypeDto.DECISION_MAKER)
            .isPrimary(true)
            .isDecisionMaker(true)
            .isActive(true)
            .linkedInUrl("test-linkedInUrl")
            .timezone("test-timezone")
            .preferredContactMethod("test-preferredContactMethod")
            .lastContactDate(LocalDate.of(2025,1,15))
            .interactionCount(42)
            .assistantName("test-assistantName")
            .assistantPhone("test-assistantPhone")
            .assistantEmail("test-assistantEmail")
            .reportsTo("test-reportsTo")
            .reportsToContactId("test-reportsToContactId")
            .birthDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .tags(Collections.emptyList())
            .address(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}