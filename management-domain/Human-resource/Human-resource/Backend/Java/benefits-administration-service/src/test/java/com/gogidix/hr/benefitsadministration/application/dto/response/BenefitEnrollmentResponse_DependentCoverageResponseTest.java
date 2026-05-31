package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.application.dto.response.BenefitEnrollmentResponse;
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
class BenefitEnrollmentResponse_DependentCoverageResponseTest {

        @Test
    void testBuilder() {
        BenefitEnrollmentResponse.DependentCoverageResponse dto = BenefitEnrollmentResponse.DependentCoverageResponse.builder()
                        .id("test-id")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .relationship("test-relationship")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .gender("test-gender")
            .address("test-address")
            .isStudent(true)
            .isDisabled(true)
            .ssnLast4("test-ssnLast4")
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-firstName", dto.getFirstName());
        assertEquals("test-lastName", dto.getLastName());
        assertEquals("test-relationship", dto.getRelationship());
        assertEquals(LocalDate.of(2025,1,15), dto.getDateOfBirth());
        assertEquals("test-gender", dto.getGender());
        assertEquals("test-address", dto.getAddress());
        assertTrue(dto.getIsStudent());
        assertTrue(dto.getIsDisabled());
        assertEquals("test-ssnLast4", dto.getSsnLast4());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        BenefitEnrollmentResponse.DependentCoverageResponse dto = new BenefitEnrollmentResponse.DependentCoverageResponse();
        dto.setId("val-id");
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
        dto.setRelationship("val-relationship");
        dto.setDateOfBirth(LocalDate.of(2025,6,1));
        dto.setGender("val-gender");
        dto.setAddress("val-address");
        dto.setIsStudent(true);
        dto.setIsDisabled(true);
        dto.setSsnLast4("val-ssnLast4");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        assertEquals("val-id", dto.getId());
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
        assertEquals("val-relationship", dto.getRelationship());
        assertEquals(LocalDate.of(2025,6,1), dto.getDateOfBirth());
        assertEquals("val-gender", dto.getGender());
        assertEquals("val-address", dto.getAddress());
        assertTrue(dto.getIsStudent());
        assertTrue(dto.getIsDisabled());
        assertEquals("val-ssnLast4", dto.getSsnLast4());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        BenefitEnrollmentResponse.DependentCoverageResponse dto1 = BenefitEnrollmentResponse.DependentCoverageResponse.builder()
                        .id("test-id")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .relationship("test-relationship")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .gender("test-gender")
            .address("test-address")
            .isStudent(true)
            .isDisabled(true)
            .ssnLast4("test-ssnLast4")
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        BenefitEnrollmentResponse.DependentCoverageResponse dto2 = BenefitEnrollmentResponse.DependentCoverageResponse.builder()
                        .id("test-id")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .relationship("test-relationship")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .gender("test-gender")
            .address("test-address")
            .isStudent(true)
            .isDisabled(true)
            .ssnLast4("test-ssnLast4")
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BenefitEnrollmentResponse.DependentCoverageResponse dto = BenefitEnrollmentResponse.DependentCoverageResponse.builder()
                        .id("test-id")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .relationship("test-relationship")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .gender("test-gender")
            .address("test-address")
            .isStudent(true)
            .isDisabled(true)
            .ssnLast4("test-ssnLast4")
            .effectiveDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}