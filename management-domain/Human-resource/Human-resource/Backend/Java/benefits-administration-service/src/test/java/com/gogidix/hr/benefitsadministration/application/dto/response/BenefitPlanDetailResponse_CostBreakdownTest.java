package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.application.dto.response.BenefitPlanDetailResponse;
import com.gogidix.hr.benefitsadministration.domain.enums.DeductionFrequency;
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
class BenefitPlanDetailResponse_CostBreakdownTest {

        @Test
    void testBuilder() {
        BenefitPlanDetailResponse.CostBreakdown dto = BenefitPlanDetailResponse.CostBreakdown.builder()
                        .baseEmployeeCost(BigDecimal.TEN)
            .baseEmployerCost(BigDecimal.TEN)
            .totalCost(BigDecimal.TEN)
            .currency("test-currency")
            .deductionFrequency(DeductionFrequency.WEEKLY)
            .costByCoverageLevel(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getBaseEmployeeCost());
        assertEquals(BigDecimal.TEN, dto.getBaseEmployerCost());
        assertEquals(BigDecimal.TEN, dto.getTotalCost());
        assertEquals("test-currency", dto.getCurrency());
    }

    @Test
    void testSettersAndGetters() {
        BenefitPlanDetailResponse.CostBreakdown dto = new BenefitPlanDetailResponse.CostBreakdown();
        dto.setBaseEmployeeCost(BigDecimal.ONE);
        dto.setBaseEmployerCost(BigDecimal.ONE);
        dto.setTotalCost(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        assertEquals(BigDecimal.ONE, dto.getBaseEmployeeCost());
        assertEquals(BigDecimal.ONE, dto.getBaseEmployerCost());
        assertEquals(BigDecimal.ONE, dto.getTotalCost());
        assertEquals("val-currency", dto.getCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        BenefitPlanDetailResponse.CostBreakdown dto1 = BenefitPlanDetailResponse.CostBreakdown.builder()
                        .baseEmployeeCost(BigDecimal.TEN)
            .baseEmployerCost(BigDecimal.TEN)
            .totalCost(BigDecimal.TEN)
            .currency("test-currency")
            .deductionFrequency(DeductionFrequency.WEEKLY)
            .costByCoverageLevel(Collections.emptyList())
            .build();
        BenefitPlanDetailResponse.CostBreakdown dto2 = BenefitPlanDetailResponse.CostBreakdown.builder()
                        .baseEmployeeCost(BigDecimal.TEN)
            .baseEmployerCost(BigDecimal.TEN)
            .totalCost(BigDecimal.TEN)
            .currency("test-currency")
            .deductionFrequency(DeductionFrequency.WEEKLY)
            .costByCoverageLevel(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BenefitPlanDetailResponse.CostBreakdown dto = BenefitPlanDetailResponse.CostBreakdown.builder()
                        .baseEmployeeCost(BigDecimal.TEN)
            .baseEmployerCost(BigDecimal.TEN)
            .totalCost(BigDecimal.TEN)
            .currency("test-currency")
            .deductionFrequency(DeductionFrequency.WEEKLY)
            .costByCoverageLevel(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}