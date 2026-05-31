package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.application.dto.request.CalculatePremiumRequest;
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
class CalculatePremiumRequestTest {

        @Test
    void testBuilder() {
        CalculatePremiumRequest dto = CalculatePremiumRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .coverageLevel("test-coverageLevel")
            .numberOfDependents(42)
            .coverageOptionCode("test-coverageOptionCode")
            .employeeAgeGroup("test-employeeAgeGroup")
            .isSmoker(true)
            .gender("test-gender")
            .salaryBand("test-salaryBand")
            .effectiveDate(LocalDate.of(2025,1,15))
            .postalCode("test-postalCode")
            .includeSpouse(true)
            .numberOfChildren(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-planId", dto.getPlanId());
        assertEquals("test-coverageLevel", dto.getCoverageLevel());
        assertEquals(42, dto.getNumberOfDependents());
        assertEquals("test-coverageOptionCode", dto.getCoverageOptionCode());
        assertEquals("test-employeeAgeGroup", dto.getEmployeeAgeGroup());
        assertTrue(dto.getIsSmoker());
        assertEquals("test-gender", dto.getGender());
        assertEquals("test-salaryBand", dto.getSalaryBand());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
        assertEquals("test-postalCode", dto.getPostalCode());
        assertTrue(dto.getIncludeSpouse());
        assertEquals(42, dto.getNumberOfChildren());
    }

    @Test
    void testSettersAndGetters() {
        CalculatePremiumRequest dto = new CalculatePremiumRequest();
        dto.setEmployeeId("val-employeeId");
        dto.setPlanId("val-planId");
        dto.setCoverageLevel("val-coverageLevel");
        dto.setNumberOfDependents(99);
        dto.setCoverageOptionCode("val-coverageOptionCode");
        dto.setEmployeeAgeGroup("val-employeeAgeGroup");
        dto.setIsSmoker(true);
        dto.setGender("val-gender");
        dto.setSalaryBand("val-salaryBand");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        dto.setPostalCode("val-postalCode");
        dto.setIncludeSpouse(true);
        dto.setNumberOfChildren(99);
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-planId", dto.getPlanId());
        assertEquals("val-coverageLevel", dto.getCoverageLevel());
        assertEquals(99, dto.getNumberOfDependents());
        assertEquals("val-coverageOptionCode", dto.getCoverageOptionCode());
        assertEquals("val-employeeAgeGroup", dto.getEmployeeAgeGroup());
        assertTrue(dto.getIsSmoker());
        assertEquals("val-gender", dto.getGender());
        assertEquals("val-salaryBand", dto.getSalaryBand());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
        assertEquals("val-postalCode", dto.getPostalCode());
        assertTrue(dto.getIncludeSpouse());
        assertEquals(99, dto.getNumberOfChildren());
    }

    @Test
    void testEqualsAndHashCode() {
        CalculatePremiumRequest dto1 = CalculatePremiumRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .coverageLevel("test-coverageLevel")
            .numberOfDependents(42)
            .coverageOptionCode("test-coverageOptionCode")
            .employeeAgeGroup("test-employeeAgeGroup")
            .isSmoker(true)
            .gender("test-gender")
            .salaryBand("test-salaryBand")
            .effectiveDate(LocalDate.of(2025,1,15))
            .postalCode("test-postalCode")
            .includeSpouse(true)
            .numberOfChildren(42)
            .build();
        CalculatePremiumRequest dto2 = CalculatePremiumRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .coverageLevel("test-coverageLevel")
            .numberOfDependents(42)
            .coverageOptionCode("test-coverageOptionCode")
            .employeeAgeGroup("test-employeeAgeGroup")
            .isSmoker(true)
            .gender("test-gender")
            .salaryBand("test-salaryBand")
            .effectiveDate(LocalDate.of(2025,1,15))
            .postalCode("test-postalCode")
            .includeSpouse(true)
            .numberOfChildren(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CalculatePremiumRequest dto = CalculatePremiumRequest.builder()
                        .employeeId("test-employeeId")
            .planId("test-planId")
            .coverageLevel("test-coverageLevel")
            .numberOfDependents(42)
            .coverageOptionCode("test-coverageOptionCode")
            .employeeAgeGroup("test-employeeAgeGroup")
            .isSmoker(true)
            .gender("test-gender")
            .salaryBand("test-salaryBand")
            .effectiveDate(LocalDate.of(2025,1,15))
            .postalCode("test-postalCode")
            .includeSpouse(true)
            .numberOfChildren(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}