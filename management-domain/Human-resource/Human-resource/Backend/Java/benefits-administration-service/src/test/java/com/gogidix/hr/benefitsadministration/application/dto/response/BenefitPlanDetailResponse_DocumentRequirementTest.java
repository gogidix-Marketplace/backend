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
class BenefitPlanDetailResponse_DocumentRequirementTest {

        @Test
    void testBuilder() {
        BenefitPlanDetailResponse.DocumentRequirement dto = BenefitPlanDetailResponse.DocumentRequirement.builder()
                        .documentType("test-documentType")
            .description("test-description")
            .isRequired(true)
            .uploadDeadline("test-uploadDeadline")
            .build();
        assertNotNull(dto);
        assertEquals("test-documentType", dto.getDocumentType());
        assertEquals("test-description", dto.getDescription());
        assertTrue(dto.getIsRequired());
        assertEquals("test-uploadDeadline", dto.getUploadDeadline());
    }

    @Test
    void testSettersAndGetters() {
        BenefitPlanDetailResponse.DocumentRequirement dto = new BenefitPlanDetailResponse.DocumentRequirement();
        dto.setDocumentType("val-documentType");
        dto.setDescription("val-description");
        dto.setIsRequired(true);
        dto.setUploadDeadline("val-uploadDeadline");
        assertEquals("val-documentType", dto.getDocumentType());
        assertEquals("val-description", dto.getDescription());
        assertTrue(dto.getIsRequired());
        assertEquals("val-uploadDeadline", dto.getUploadDeadline());
    }

    @Test
    void testEqualsAndHashCode() {
        BenefitPlanDetailResponse.DocumentRequirement dto1 = BenefitPlanDetailResponse.DocumentRequirement.builder()
                        .documentType("test-documentType")
            .description("test-description")
            .isRequired(true)
            .uploadDeadline("test-uploadDeadline")
            .build();
        BenefitPlanDetailResponse.DocumentRequirement dto2 = BenefitPlanDetailResponse.DocumentRequirement.builder()
                        .documentType("test-documentType")
            .description("test-description")
            .isRequired(true)
            .uploadDeadline("test-uploadDeadline")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BenefitPlanDetailResponse.DocumentRequirement dto = BenefitPlanDetailResponse.DocumentRequirement.builder()
                        .documentType("test-documentType")
            .description("test-description")
            .isRequired(true)
            .uploadDeadline("test-uploadDeadline")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}