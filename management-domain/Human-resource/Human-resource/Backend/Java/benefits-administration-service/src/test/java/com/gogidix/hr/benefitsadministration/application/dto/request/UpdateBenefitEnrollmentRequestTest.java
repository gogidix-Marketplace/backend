package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.application.dto.request.UpdateBenefitEnrollmentRequest;
import com.gogidix.hr.benefitsadministration.domain.enums.EnrollmentStatus;
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
class UpdateBenefitEnrollmentRequestTest {

        @Test
    void testBuilder() {
        UpdateBenefitEnrollmentRequest dto = UpdateBenefitEnrollmentRequest.builder()
                        .coverageLevel("test-coverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status(EnrollmentStatus.ACTIVE)
            .coverageOptionCode("test-coverageOptionCode")
            .notes("test-notes")
            .evidenceDocuments(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-coverageLevel", dto.getCoverageLevel());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getExpiryDate());
        assertEquals("test-coverageOptionCode", dto.getCoverageOptionCode());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        UpdateBenefitEnrollmentRequest dto = new UpdateBenefitEnrollmentRequest();
        dto.setCoverageLevel("val-coverageLevel");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setExpiryDate(LocalDate.of(2025,6,1));
        dto.setCoverageOptionCode("val-coverageOptionCode");
        dto.setNotes("val-notes");
        assertEquals("val-coverageLevel", dto.getCoverageLevel());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpiryDate());
        assertEquals("val-coverageOptionCode", dto.getCoverageOptionCode());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdateBenefitEnrollmentRequest dto1 = UpdateBenefitEnrollmentRequest.builder()
                        .coverageLevel("test-coverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status(EnrollmentStatus.ACTIVE)
            .coverageOptionCode("test-coverageOptionCode")
            .notes("test-notes")
            .evidenceDocuments(Collections.emptyList())
            .build();
        UpdateBenefitEnrollmentRequest dto2 = UpdateBenefitEnrollmentRequest.builder()
                        .coverageLevel("test-coverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status(EnrollmentStatus.ACTIVE)
            .coverageOptionCode("test-coverageOptionCode")
            .notes("test-notes")
            .evidenceDocuments(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpdateBenefitEnrollmentRequest dto = UpdateBenefitEnrollmentRequest.builder()
                        .coverageLevel("test-coverageLevel")
            .effectiveDate(LocalDate.of(2025,1,15))
            .expiryDate(LocalDate.of(2025,1,15))
            .status(EnrollmentStatus.ACTIVE)
            .coverageOptionCode("test-coverageOptionCode")
            .notes("test-notes")
            .evidenceDocuments(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}