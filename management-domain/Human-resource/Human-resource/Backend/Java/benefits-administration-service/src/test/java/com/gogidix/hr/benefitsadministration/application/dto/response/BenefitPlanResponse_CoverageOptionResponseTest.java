package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.application.dto.response.BenefitPlanResponse;
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
class BenefitPlanResponse_CoverageOptionResponseTest {

        @Test
    void testBuilder() {
        BenefitPlanResponse.CoverageOptionResponse dto = BenefitPlanResponse.CoverageOptionResponse.builder()
                        .optionCode("test-optionCode")
            .optionName("test-optionName")
            .description("test-description")
            .level("test-level")
            .employeeCost(BigDecimal.TEN)
            .employerCost(BigDecimal.TEN)
            .isAvailable(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-optionCode", dto.getOptionCode());
        assertEquals("test-optionName", dto.getOptionName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-level", dto.getLevel());
        assertEquals(BigDecimal.TEN, dto.getEmployeeCost());
        assertEquals(BigDecimal.TEN, dto.getEmployerCost());
        assertTrue(dto.getIsAvailable());
    }

    @Test
    void testSettersAndGetters() {
        BenefitPlanResponse.CoverageOptionResponse dto = new BenefitPlanResponse.CoverageOptionResponse();
        dto.setOptionCode("val-optionCode");
        dto.setOptionName("val-optionName");
        dto.setDescription("val-description");
        dto.setLevel("val-level");
        dto.setEmployeeCost(BigDecimal.ONE);
        dto.setEmployerCost(BigDecimal.ONE);
        dto.setIsAvailable(true);
        assertEquals("val-optionCode", dto.getOptionCode());
        assertEquals("val-optionName", dto.getOptionName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-level", dto.getLevel());
        assertEquals(BigDecimal.ONE, dto.getEmployeeCost());
        assertEquals(BigDecimal.ONE, dto.getEmployerCost());
        assertTrue(dto.getIsAvailable());
    }

    @Test
    void testEqualsAndHashCode() {
        BenefitPlanResponse.CoverageOptionResponse dto1 = BenefitPlanResponse.CoverageOptionResponse.builder()
                        .optionCode("test-optionCode")
            .optionName("test-optionName")
            .description("test-description")
            .level("test-level")
            .employeeCost(BigDecimal.TEN)
            .employerCost(BigDecimal.TEN)
            .isAvailable(true)
            .build();
        BenefitPlanResponse.CoverageOptionResponse dto2 = BenefitPlanResponse.CoverageOptionResponse.builder()
                        .optionCode("test-optionCode")
            .optionName("test-optionName")
            .description("test-description")
            .level("test-level")
            .employeeCost(BigDecimal.TEN)
            .employerCost(BigDecimal.TEN)
            .isAvailable(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BenefitPlanResponse.CoverageOptionResponse dto = BenefitPlanResponse.CoverageOptionResponse.builder()
                        .optionCode("test-optionCode")
            .optionName("test-optionName")
            .description("test-description")
            .level("test-level")
            .employeeCost(BigDecimal.TEN)
            .employerCost(BigDecimal.TEN)
            .isAvailable(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}