package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.gogidix.customersupport.qualitymanagement.application.dto.ScorecardTemplateDto;
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
class ScorecardTemplateDto_CriteriaDtoTest {

        @Test
    void testBuilder() {
        ScorecardTemplateDto.CriteriaDto dto = ScorecardTemplateDto.CriteriaDto.builder()
                        .criteriaId("test-criteriaId")
            .criteriaName("test-criteriaName")
            .description("test-description")
            .categoryId("test-categoryId")
            .maxScore(null)
            .weight(null)
            .isCritical(true)
            .isRequired(true)
            .order(42)
            .scoringGuidance("test-scoringGuidance")
            .examples(Collections.emptyList())
            .redFlags(Collections.emptyList())
            .criteriaType("test-criteriaType")
            .build();
        assertNotNull(dto);
        assertEquals("test-criteriaId", dto.getCriteriaId());
        assertEquals("test-criteriaName", dto.getCriteriaName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-categoryId", dto.getCategoryId());
        assertTrue(dto.getIsCritical());
        assertTrue(dto.getIsRequired());
        assertEquals(42, dto.getOrder());
        assertEquals("test-scoringGuidance", dto.getScoringGuidance());
        assertEquals("test-criteriaType", dto.getCriteriaType());
    }

    @Test
    void testSettersAndGetters() {
        ScorecardTemplateDto.CriteriaDto dto = new ScorecardTemplateDto.CriteriaDto();
        dto.setCriteriaId("val-criteriaId");
        dto.setCriteriaName("val-criteriaName");
        dto.setDescription("val-description");
        dto.setCategoryId("val-categoryId");
        dto.setIsCritical(true);
        dto.setIsRequired(true);
        dto.setOrder(99);
        dto.setScoringGuidance("val-scoringGuidance");
        dto.setCriteriaType("val-criteriaType");
        assertEquals("val-criteriaId", dto.getCriteriaId());
        assertEquals("val-criteriaName", dto.getCriteriaName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-categoryId", dto.getCategoryId());
        assertTrue(dto.getIsCritical());
        assertTrue(dto.getIsRequired());
        assertEquals(99, dto.getOrder());
        assertEquals("val-scoringGuidance", dto.getScoringGuidance());
        assertEquals("val-criteriaType", dto.getCriteriaType());
    }

    @Test
    void testEqualsAndHashCode() {
        ScorecardTemplateDto.CriteriaDto dto1 = ScorecardTemplateDto.CriteriaDto.builder()
                        .criteriaId("test-criteriaId")
            .criteriaName("test-criteriaName")
            .description("test-description")
            .categoryId("test-categoryId")
            .maxScore(null)
            .weight(null)
            .isCritical(true)
            .isRequired(true)
            .order(42)
            .scoringGuidance("test-scoringGuidance")
            .examples(Collections.emptyList())
            .redFlags(Collections.emptyList())
            .criteriaType("test-criteriaType")
            .build();
        ScorecardTemplateDto.CriteriaDto dto2 = ScorecardTemplateDto.CriteriaDto.builder()
                        .criteriaId("test-criteriaId")
            .criteriaName("test-criteriaName")
            .description("test-description")
            .categoryId("test-categoryId")
            .maxScore(null)
            .weight(null)
            .isCritical(true)
            .isRequired(true)
            .order(42)
            .scoringGuidance("test-scoringGuidance")
            .examples(Collections.emptyList())
            .redFlags(Collections.emptyList())
            .criteriaType("test-criteriaType")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ScorecardTemplateDto.CriteriaDto dto = ScorecardTemplateDto.CriteriaDto.builder()
                        .criteriaId("test-criteriaId")
            .criteriaName("test-criteriaName")
            .description("test-description")
            .categoryId("test-categoryId")
            .maxScore(null)
            .weight(null)
            .isCritical(true)
            .isRequired(true)
            .order(42)
            .scoringGuidance("test-scoringGuidance")
            .examples(Collections.emptyList())
            .redFlags(Collections.emptyList())
            .criteriaType("test-criteriaType")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}