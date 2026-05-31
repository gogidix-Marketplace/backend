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
class BenefitPlanDetailResponse_EligibilityRequirementTest {

        @Test
    void testBuilder() {
        BenefitPlanDetailResponse.EligibilityRequirement dto = BenefitPlanDetailResponse.EligibilityRequirement.builder()
                        .requirement("test-requirement")
            .description("test-description")
            .isRequired(true)
            .validationRule("test-validationRule")
            .build();
        assertNotNull(dto);
        assertEquals("test-requirement", dto.getRequirement());
        assertEquals("test-description", dto.getDescription());
        assertTrue(dto.getIsRequired());
        assertEquals("test-validationRule", dto.getValidationRule());
    }

    @Test
    void testSettersAndGetters() {
        BenefitPlanDetailResponse.EligibilityRequirement dto = new BenefitPlanDetailResponse.EligibilityRequirement();
        dto.setRequirement("val-requirement");
        dto.setDescription("val-description");
        dto.setIsRequired(true);
        dto.setValidationRule("val-validationRule");
        assertEquals("val-requirement", dto.getRequirement());
        assertEquals("val-description", dto.getDescription());
        assertTrue(dto.getIsRequired());
        assertEquals("val-validationRule", dto.getValidationRule());
    }

    @Test
    void testEqualsAndHashCode() {
        BenefitPlanDetailResponse.EligibilityRequirement dto1 = BenefitPlanDetailResponse.EligibilityRequirement.builder()
                        .requirement("test-requirement")
            .description("test-description")
            .isRequired(true)
            .validationRule("test-validationRule")
            .build();
        BenefitPlanDetailResponse.EligibilityRequirement dto2 = BenefitPlanDetailResponse.EligibilityRequirement.builder()
                        .requirement("test-requirement")
            .description("test-description")
            .isRequired(true)
            .validationRule("test-validationRule")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BenefitPlanDetailResponse.EligibilityRequirement dto = BenefitPlanDetailResponse.EligibilityRequirement.builder()
                        .requirement("test-requirement")
            .description("test-description")
            .isRequired(true)
            .validationRule("test-validationRule")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}