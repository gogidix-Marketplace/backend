package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.application.dto.request.AddDependentRequest;
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
class AddDependentRequestTest {

        @Test
    void testBuilder() {
        AddDependentRequest dto = AddDependentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .relationship("test-relationship")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .gender("test-gender")
            .address("test-address")
            .isStudent(true)
            .isDisabled(true)
            .ssnLast4("test-ssnLast4")
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-enrollmentId", dto.getEnrollmentId());
        assertEquals("test-firstName", dto.getFirstName());
        assertEquals("test-lastName", dto.getLastName());
        assertEquals("test-relationship", dto.getRelationship());
        assertEquals(LocalDate.of(2025,1,15), dto.getDateOfBirth());
        assertEquals("test-gender", dto.getGender());
        assertEquals("test-address", dto.getAddress());
        assertTrue(dto.getIsStudent());
        assertTrue(dto.getIsDisabled());
        assertEquals("test-ssnLast4", dto.getSsnLast4());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        AddDependentRequest dto = new AddDependentRequest();
        dto.setEnrollmentId("val-enrollmentId");
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
        dto.setRelationship("val-relationship");
        dto.setDateOfBirth(LocalDate.of(2025,6,1));
        dto.setGender("val-gender");
        dto.setAddress("val-address");
        dto.setIsStudent(true);
        dto.setIsDisabled(true);
        dto.setSsnLast4("val-ssnLast4");
        dto.setNotes("val-notes");
        assertEquals("val-enrollmentId", dto.getEnrollmentId());
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
        assertEquals("val-relationship", dto.getRelationship());
        assertEquals(LocalDate.of(2025,6,1), dto.getDateOfBirth());
        assertEquals("val-gender", dto.getGender());
        assertEquals("val-address", dto.getAddress());
        assertTrue(dto.getIsStudent());
        assertTrue(dto.getIsDisabled());
        assertEquals("val-ssnLast4", dto.getSsnLast4());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        AddDependentRequest dto1 = AddDependentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .relationship("test-relationship")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .gender("test-gender")
            .address("test-address")
            .isStudent(true)
            .isDisabled(true)
            .ssnLast4("test-ssnLast4")
            .notes("test-notes")
            .build();
        AddDependentRequest dto2 = AddDependentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .relationship("test-relationship")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .gender("test-gender")
            .address("test-address")
            .isStudent(true)
            .isDisabled(true)
            .ssnLast4("test-ssnLast4")
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AddDependentRequest dto = AddDependentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .relationship("test-relationship")
            .dateOfBirth(LocalDate.of(2025,1,15))
            .gender("test-gender")
            .address("test-address")
            .isStudent(true)
            .isDisabled(true)
            .ssnLast4("test-ssnLast4")
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}