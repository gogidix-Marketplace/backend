package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.application.dto.request.UpdateCoverageRequest;
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
class UpdateCoverageRequestTest {

        @Test
    void testBuilder() {
        UpdateCoverageRequest dto = UpdateCoverageRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .newCoverageLevel("test-newCoverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .newCoverageOptionCode("test-newCoverageOptionCode")
            .reason("test-reason")
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-enrollmentId", dto.getEnrollmentId());
        assertEquals("test-newCoverageLevel", dto.getNewCoverageLevel());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals("test-newCoverageOptionCode", dto.getNewCoverageOptionCode());
        assertEquals("test-reason", dto.getReason());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        UpdateCoverageRequest dto = new UpdateCoverageRequest();
        dto.setEnrollmentId("val-enrollmentId");
        dto.setNewCoverageLevel("val-newCoverageLevel");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setNewCoverageOptionCode("val-newCoverageOptionCode");
        dto.setReason("val-reason");
        dto.setNotes("val-notes");
        assertEquals("val-enrollmentId", dto.getEnrollmentId());
        assertEquals("val-newCoverageLevel", dto.getNewCoverageLevel());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals("val-newCoverageOptionCode", dto.getNewCoverageOptionCode());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdateCoverageRequest dto1 = UpdateCoverageRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .newCoverageLevel("test-newCoverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .newCoverageOptionCode("test-newCoverageOptionCode")
            .reason("test-reason")
            .notes("test-notes")
            .build();
        UpdateCoverageRequest dto2 = UpdateCoverageRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .newCoverageLevel("test-newCoverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .newCoverageOptionCode("test-newCoverageOptionCode")
            .reason("test-reason")
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpdateCoverageRequest dto = UpdateCoverageRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .newCoverageLevel("test-newCoverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .newCoverageOptionCode("test-newCoverageOptionCode")
            .reason("test-reason")
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}