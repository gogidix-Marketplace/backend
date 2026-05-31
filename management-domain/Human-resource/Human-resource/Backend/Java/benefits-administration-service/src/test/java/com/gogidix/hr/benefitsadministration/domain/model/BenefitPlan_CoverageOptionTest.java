package com.gogidix.hr.benefitsadministration.domain.model;

import com.gogidix.hr.benefitsadministration.domain.model.BenefitPlan;
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
class BenefitPlan_CoverageOptionTest {

        @Test
    void testBuilder() {
        BenefitPlan.CoverageOption dto = BenefitPlan.CoverageOption.builder()
                        .optionCode("test-optionCode")
            .optionName("test-optionName")
            .description("test-description")
            .level(null)
            .employeeCost(BigDecimal.TEN)
            .employerCost(BigDecimal.TEN)
            .isAvailable(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-optionCode", dto.getOptionCode());
        assertEquals("test-optionName", dto.getOptionName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getEmployeeCost());
        assertEquals(BigDecimal.TEN, dto.getEmployerCost());
        assertTrue(dto.getIsAvailable());
    }

    @Test
    void testSettersAndGetters() {
        BenefitPlan.CoverageOption dto = new BenefitPlan.CoverageOption();
        dto.setOptionCode("val-optionCode");
        dto.setOptionName("val-optionName");
        dto.setDescription("val-description");
        dto.setEmployeeCost(BigDecimal.ONE);
        dto.setEmployerCost(BigDecimal.ONE);
        dto.setIsAvailable(true);
        assertEquals("val-optionCode", dto.getOptionCode());
        assertEquals("val-optionName", dto.getOptionName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getEmployeeCost());
        assertEquals(BigDecimal.ONE, dto.getEmployerCost());
        assertTrue(dto.getIsAvailable());
    }

    @Test
    void testEqualsAndHashCode() {
        BenefitPlan.CoverageOption dto1 = BenefitPlan.CoverageOption.builder()
                        .optionCode("test-optionCode")
            .optionName("test-optionName")
            .description("test-description")
            .level(null)
            .employeeCost(BigDecimal.TEN)
            .employerCost(BigDecimal.TEN)
            .isAvailable(true)
            .build();
        BenefitPlan.CoverageOption dto2 = BenefitPlan.CoverageOption.builder()
                        .optionCode("test-optionCode")
            .optionName("test-optionName")
            .description("test-description")
            .level(null)
            .employeeCost(BigDecimal.TEN)
            .employerCost(BigDecimal.TEN)
            .isAvailable(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BenefitPlan.CoverageOption dto = BenefitPlan.CoverageOption.builder()
                        .optionCode("test-optionCode")
            .optionName("test-optionName")
            .description("test-description")
            .level(null)
            .employeeCost(BigDecimal.TEN)
            .employerCost(BigDecimal.TEN)
            .isAvailable(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}