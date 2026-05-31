package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.application.dto.request.CancelEnrollmentRequest;
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
class CancelEnrollmentRequestTest {

        @Test
    void testBuilder() {
        CancelEnrollmentRequest dto = CancelEnrollmentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .cancellationReason("test-cancellationReason")
            .effectiveDate(LocalDate.of(2025,1,15))
            .cancelledBy("test-cancelledBy")
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-enrollmentId", dto.getEnrollmentId());
        assertEquals("test-cancellationReason", dto.getCancellationReason());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals("test-cancelledBy", dto.getCancelledBy());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        CancelEnrollmentRequest dto = new CancelEnrollmentRequest();
        dto.setEnrollmentId("val-enrollmentId");
        dto.setCancellationReason("val-cancellationReason");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setCancelledBy("val-cancelledBy");
        dto.setNotes("val-notes");
        assertEquals("val-enrollmentId", dto.getEnrollmentId());
        assertEquals("val-cancellationReason", dto.getCancellationReason());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals("val-cancelledBy", dto.getCancelledBy());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        CancelEnrollmentRequest dto1 = CancelEnrollmentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .cancellationReason("test-cancellationReason")
            .effectiveDate(LocalDate.of(2025,1,15))
            .cancelledBy("test-cancelledBy")
            .notes("test-notes")
            .build();
        CancelEnrollmentRequest dto2 = CancelEnrollmentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .cancellationReason("test-cancellationReason")
            .effectiveDate(LocalDate.of(2025,1,15))
            .cancelledBy("test-cancelledBy")
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CancelEnrollmentRequest dto = CancelEnrollmentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .cancellationReason("test-cancellationReason")
            .effectiveDate(LocalDate.of(2025,1,15))
            .cancelledBy("test-cancelledBy")
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}