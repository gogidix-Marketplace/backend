package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.application.dto.response.BenefitPlanDetailResponse;
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
class BenefitPlanDetailResponse_CostByCoverageTest {

        @Test
    void testBuilder() {
        BenefitPlanDetailResponse.CostByCoverage dto = BenefitPlanDetailResponse.CostByCoverage.builder()
                        .coverageLevel("test-coverageLevel")
            .employeeCost(BigDecimal.TEN)
            .employerCost(BigDecimal.TEN)
            .totalCost(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals("test-coverageLevel", dto.getCoverageLevel());
        assertEquals(BigDecimal.TEN, dto.getEmployeeCost());
        assertEquals(BigDecimal.TEN, dto.getEmployerCost());
        assertEquals(BigDecimal.TEN, dto.getTotalCost());
    }

    @Test
    void testSettersAndGetters() {
        BenefitPlanDetailResponse.CostByCoverage dto = new BenefitPlanDetailResponse.CostByCoverage();
        dto.setCoverageLevel("val-coverageLevel");
        dto.setEmployeeCost(BigDecimal.ONE);
        dto.setEmployerCost(BigDecimal.ONE);
        dto.setTotalCost(BigDecimal.ONE);
        assertEquals("val-coverageLevel", dto.getCoverageLevel());
        assertEquals(BigDecimal.ONE, dto.getEmployeeCost());
        assertEquals(BigDecimal.ONE, dto.getEmployerCost());
        assertEquals(BigDecimal.ONE, dto.getTotalCost());
    }

    @Test
    void testEqualsAndHashCode() {
        BenefitPlanDetailResponse.CostByCoverage dto1 = BenefitPlanDetailResponse.CostByCoverage.builder()
                        .coverageLevel("test-coverageLevel")
            .employeeCost(BigDecimal.TEN)
            .employerCost(BigDecimal.TEN)
            .totalCost(BigDecimal.TEN)
            .build();
        BenefitPlanDetailResponse.CostByCoverage dto2 = BenefitPlanDetailResponse.CostByCoverage.builder()
                        .coverageLevel("test-coverageLevel")
            .employeeCost(BigDecimal.TEN)
            .employerCost(BigDecimal.TEN)
            .totalCost(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BenefitPlanDetailResponse.CostByCoverage dto = BenefitPlanDetailResponse.CostByCoverage.builder()
                        .coverageLevel("test-coverageLevel")
            .employeeCost(BigDecimal.TEN)
            .employerCost(BigDecimal.TEN)
            .totalCost(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}