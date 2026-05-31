package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.application.dto.request.RemoveDependentRequest;
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
class RemoveDependentRequestTest {

        @Test
    void testBuilder() {
        RemoveDependentRequest dto = RemoveDependentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .dependentId("test-dependentId")
            .effectiveDate(LocalDate.of(2025,1,15))
            .removalReason("test-removalReason")
            .build();
        assertNotNull(dto);
        assertEquals("test-enrollmentId", dto.getEnrollmentId());
        assertEquals("test-dependentId", dto.getDependentId());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals("test-removalReason", dto.getRemovalReason());
    }

    @Test
    void testSettersAndGetters() {
        RemoveDependentRequest dto = new RemoveDependentRequest();
        dto.setEnrollmentId("val-enrollmentId");
        dto.setDependentId("val-dependentId");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setRemovalReason("val-removalReason");
        assertEquals("val-enrollmentId", dto.getEnrollmentId());
        assertEquals("val-dependentId", dto.getDependentId());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals("val-removalReason", dto.getRemovalReason());
    }

    @Test
    void testEqualsAndHashCode() {
        RemoveDependentRequest dto1 = RemoveDependentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .dependentId("test-dependentId")
            .effectiveDate(LocalDate.of(2025,1,15))
            .removalReason("test-removalReason")
            .build();
        RemoveDependentRequest dto2 = RemoveDependentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .dependentId("test-dependentId")
            .effectiveDate(LocalDate.of(2025,1,15))
            .removalReason("test-removalReason")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RemoveDependentRequest dto = RemoveDependentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .dependentId("test-dependentId")
            .effectiveDate(LocalDate.of(2025,1,15))
            .removalReason("test-removalReason")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}