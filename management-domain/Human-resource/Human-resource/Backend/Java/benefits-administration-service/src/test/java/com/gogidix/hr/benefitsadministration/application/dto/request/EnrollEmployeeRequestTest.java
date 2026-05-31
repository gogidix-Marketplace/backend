package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.application.dto.request.EnrollEmployeeRequest;
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
class EnrollEmployeeRequestTest {

        @Test
    void testBuilder() {
        EnrollEmployeeRequest dto = EnrollEmployeeRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .effectiveDate(LocalDate.of(2025,1,15))
            .coverageLevel("test-coverageLevel")
            .coverageOptionCode("test-coverageOptionCode")
            .autoEnroll(true)
            .enrollmentSource("test-enrollmentSource")
            .build();
        assertNotNull(dto);
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-planId", dto.getPlanId());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals("test-coverageLevel", dto.getCoverageLevel());
        assertEquals("test-coverageOptionCode", dto.getCoverageOptionCode());
        assertTrue(dto.getAutoEnroll());
        assertEquals("test-enrollmentSource", dto.getEnrollmentSource());
    }

    @Test
    void testSettersAndGetters() {
        EnrollEmployeeRequest dto = new EnrollEmployeeRequest();
        dto.setEmployeeId("val-employeeId");
        dto.setPlanId("val-planId");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setCoverageLevel("val-coverageLevel");
        dto.setCoverageOptionCode("val-coverageOptionCode");
        dto.setAutoEnroll(true);
        dto.setEnrollmentSource("val-enrollmentSource");
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-planId", dto.getPlanId());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals("val-coverageLevel", dto.getCoverageLevel());
        assertEquals("val-coverageOptionCode", dto.getCoverageOptionCode());
        assertTrue(dto.getAutoEnroll());
        assertEquals("val-enrollmentSource", dto.getEnrollmentSource());
    }

    @Test
    void testEqualsAndHashCode() {
        EnrollEmployeeRequest dto1 = EnrollEmployeeRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .effectiveDate(LocalDate.of(2025,1,15))
            .coverageLevel("test-coverageLevel")
            .coverageOptionCode("test-coverageOptionCode")
            .autoEnroll(true)
            .enrollmentSource("test-enrollmentSource")
            .build();
        EnrollEmployeeRequest dto2 = EnrollEmployeeRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .effectiveDate(LocalDate.of(2025,1,15))
            .coverageLevel("test-coverageLevel")
            .coverageOptionCode("test-coverageOptionCode")
            .autoEnroll(true)
            .enrollmentSource("test-enrollmentSource")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        EnrollEmployeeRequest dto = EnrollEmployeeRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .effectiveDate(LocalDate.of(2025,1,15))
            .coverageLevel("test-coverageLevel")
            .coverageOptionCode("test-coverageOptionCode")
            .autoEnroll(true)
            .enrollmentSource("test-enrollmentSource")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}