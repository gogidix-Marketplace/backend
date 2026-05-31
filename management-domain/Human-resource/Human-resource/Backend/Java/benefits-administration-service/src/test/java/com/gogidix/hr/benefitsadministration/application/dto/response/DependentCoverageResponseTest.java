package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.application.dto.response.DependentCoverageResponse;
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
class DependentCoverageResponseTest {

        @Test
    void testBuilder() {
        DependentCoverageResponse dto = DependentCoverageResponse.builder()
                        .id("test-id")
            .enrollmentId("test-enrollmentId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .relationship("test-relationship")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .gender("test-gender")
            .address("test-address")
            .isStudent(true)
            .isDisabled(true)
            .ssnLast4("test-ssnLast4")
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .isActive(true)
            .status("test-status")
            .notes("test-notes")
            .createdAt(LocalDate.of(2025,1,15))
            .updatedAt(LocalDate.of(2025,1,15))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-enrollmentId", dto.getEnrollmentId());
        assertEquals("test-firstName", dto.getFirstName());
        assertEquals("test-lastName", dto.getLastName());
        assertEquals("test-fullName", dto.getFullName());
        assertEquals("test-relationship", dto.getRelationship());
        assertEquals(LocalDate.of(2025,1,15), dto.getDateOfBirth());
        assertEquals("test-gender", dto.getGender());
        assertEquals("test-address", dto.getAddress());
        assertTrue(dto.getIsStudent());
        assertTrue(dto.getIsDisabled());
        assertEquals("test-ssnLast4", dto.getSsnLast4());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertTrue(dto.getIsActive());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-notes", dto.getNotes());
        assertEquals(LocalDate.of(2025,1,15), dto.getCreatedAt());
        assertEquals(LocalDate.of(2025,1,15), dto.getUpdatedAt());
    }

    @Test
    void testSettersAndGetters() {
        DependentCoverageResponse dto = new DependentCoverageResponse();
        dto.setId("val-id");
        dto.setEnrollmentId("val-enrollmentId");
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
        dto.setFullName("val-fullName");
        dto.setRelationship("val-relationship");
        dto.setDateOfBirth(LocalDate.of(2025,6,1));
        dto.setGender("val-gender");
        dto.setAddress("val-address");
        dto.setIsStudent(true);
        dto.setIsDisabled(true);
        dto.setSsnLast4("val-ssnLast4");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setIsActive(true);
        dto.setStatus("val-status");
        dto.setNotes("val-notes");
        dto.setCreatedAt(LocalDate.of(2025,6,1));
        dto.setUpdatedAt(LocalDate.of(2025,6,1));
        assertEquals("val-id", dto.getId());
        assertEquals("val-enrollmentId", dto.getEnrollmentId());
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
        assertEquals("val-fullName", dto.getFullName());
        assertEquals("val-relationship", dto.getRelationship());
        assertEquals(LocalDate.of(2025,6,1), dto.getDateOfBirth());
        assertEquals("val-gender", dto.getGender());
        assertEquals("val-address", dto.getAddress());
        assertTrue(dto.getIsStudent());
        assertTrue(dto.getIsDisabled());
        assertEquals("val-ssnLast4", dto.getSsnLast4());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertTrue(dto.getIsActive());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(LocalDate.of(2025,6,1), dto.getCreatedAt());
        assertEquals(LocalDate.of(2025,6,1), dto.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        DependentCoverageResponse dto1 = DependentCoverageResponse.builder()
                        .id("test-id")
            .enrollmentId("test-enrollmentId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .relationship("test-relationship")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .gender("test-gender")
            .address("test-address")
            .isStudent(true)
            .isDisabled(true)
            .ssnLast4("test-ssnLast4")
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .isActive(true)
            .status("test-status")
            .notes("test-notes")
            .createdAt(LocalDate.of(2025,1,15))
            .updatedAt(LocalDate.of(2025,1,15))
            .build();
        DependentCoverageResponse dto2 = DependentCoverageResponse.builder()
                        .id("test-id")
            .enrollmentId("test-enrollmentId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .relationship("test-relationship")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .gender("test-gender")
            .address("test-address")
            .isStudent(true)
            .isDisabled(true)
            .ssnLast4("test-ssnLast4")
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .isActive(true)
            .status("test-status")
            .notes("test-notes")
            .createdAt(LocalDate.of(2025,1,15))
            .updatedAt(LocalDate.of(2025,1,15))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DependentCoverageResponse dto = DependentCoverageResponse.builder()
                        .id("test-id")
            .enrollmentId("test-enrollmentId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .relationship("test-relationship")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .gender("test-gender")
            .address("test-address")
            .isStudent(true)
            .isDisabled(true)
            .ssnLast4("test-ssnLast4")
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .isActive(true)
            .status("test-status")
            .notes("test-notes")
            .createdAt(LocalDate.of(2025,1,15))
            .updatedAt(LocalDate.of(2025,1,15))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}