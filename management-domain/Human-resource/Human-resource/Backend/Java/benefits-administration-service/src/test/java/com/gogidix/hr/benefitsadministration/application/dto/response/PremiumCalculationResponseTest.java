package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.application.dto.response.PremiumCalculationResponse;
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
class PremiumCalculationResponseTest {

        @Test
    void testBuilder() {
        PremiumCalculationResponse dto = PremiumCalculationResponse.builder()
                        .planId("test-planId")
            .planName("test-planName")
            .employeeId("test-employeeId")
            .coverageLevel("test-coverageLevel")
            .coverageOptionCode("test-coverageOptionCode")
            .numberOfDependents(42)
            .employeePremium(BigDecimal.TEN)
            .employerPremium(BigDecimal.TEN)
            .totalPremium(BigDecimal.TEN)
            .currency("test-currency")
            .deductionFrequency("test-deductionFrequency")
            .effectiveDate(LocalDate.of(2025,1,15))
            .breakdown(Collections.emptyList())
            .metadata(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-planId", dto.getPlanId());
        assertEquals("test-planName", dto.getPlanName());
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-coverageLevel", dto.getCoverageLevel());
        assertEquals("test-coverageOptionCode", dto.getCoverageOptionCode());
        assertEquals(42, dto.getNumberOfDependents());
        assertEquals(BigDecimal.TEN, dto.getEmployeePremium());
        assertEquals(BigDecimal.TEN, dto.getEmployerPremium());
        assertEquals(BigDecimal.TEN, dto.getTotalPremium());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-deductionFrequency", dto.getDeductionFrequency());
        assertEquals(LocalDate.of(2025,1,15), dto.getEffectiveDate());
    }

    @Test
    void testSettersAndGetters() {
        PremiumCalculationResponse dto = new PremiumCalculationResponse();
        dto.setPlanId("val-planId");
        dto.setPlanName("val-planName");
        dto.setEmployeeId("val-employeeId");
        dto.setCoverageLevel("val-coverageLevel");
        dto.setCoverageOptionCode("val-coverageOptionCode");
        dto.setNumberOfDependents(99);
        dto.setEmployeePremium(BigDecimal.ONE);
        dto.setEmployerPremium(BigDecimal.ONE);
        dto.setTotalPremium(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setDeductionFrequency("val-deductionFrequency");
        dto.setEffectiveDate(LocalDate.of(2025,6,1));
        assertEquals("val-planId", dto.getPlanId());
        assertEquals("val-planName", dto.getPlanName());
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-coverageLevel", dto.getCoverageLevel());
        assertEquals("val-coverageOptionCode", dto.getCoverageOptionCode());
        assertEquals(99, dto.getNumberOfDependents());
        assertEquals(BigDecimal.ONE, dto.getEmployeePremium());
        assertEquals(BigDecimal.ONE, dto.getEmployerPremium());
        assertEquals(BigDecimal.ONE, dto.getTotalPremium());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-deductionFrequency", dto.getDeductionFrequency());
        assertEquals(LocalDate.of(2025,6,1), dto.getEffectiveDate());
    }

    @Test
    void testEqualsAndHashCode() {
        PremiumCalculationResponse dto1 = PremiumCalculationResponse.builder()
                        .planId("test-planId")
            .planName("test-planName")
            .employeeId("test-employeeId")
            .coverageLevel("test-coverageLevel")
            .coverageOptionCode("test-coverageOptionCode")
            .numberOfDependents(42)
            .employeePremium(BigDecimal.TEN)
            .employerPremium(BigDecimal.TEN)
            .totalPremium(BigDecimal.TEN)
            .currency("test-currency")
            .deductionFrequency("test-deductionFrequency")
            .effectiveDate(LocalDate.of(2025,1,15))
            .breakdown(Collections.emptyList())
            .metadata(null)
            .build();
        PremiumCalculationResponse dto2 = PremiumCalculationResponse.builder()
                        .planId("test-planId")
            .planName("test-planName")
            .employeeId("test-employeeId")
            .coverageLevel("test-coverageLevel")
            .coverageOptionCode("test-coverageOptionCode")
            .numberOfDependents(42)
            .employeePremium(BigDecimal.TEN)
            .employerPremium(BigDecimal.TEN)
            .totalPremium(BigDecimal.TEN)
            .currency("test-currency")
            .deductionFrequency("test-deductionFrequency")
            .effectiveDate(LocalDate.of(2025,1,15))
            .breakdown(Collections.emptyList())
            .metadata(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PremiumCalculationResponse dto = PremiumCalculationResponse.builder()
                        .planId("test-planId")
            .planName("test-planName")
            .employeeId("test-employeeId")
            .coverageLevel("test-coverageLevel")
            .coverageOptionCode("test-coverageOptionCode")
            .numberOfDependents(42)
            .employeePremium(BigDecimal.TEN)
            .employerPremium(BigDecimal.TEN)
            .totalPremium(BigDecimal.TEN)
            .currency("test-currency")
            .deductionFrequency("test-deductionFrequency")
            .effectiveDate(LocalDate.of(2025,1,15))
            .breakdown(Collections.emptyList())
            .metadata(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}