package com.gogidix.customersupport.qualitymanagement.domain.model;

import com.gogidix.customersupport.qualitymanagement.domain.model.ScorecardTemplate;
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
class ScorecardTemplate_CriteriaTest {

        @Test
    void testBuilder() {
        ScorecardTemplate.Criteria dto = ScorecardTemplate.Criteria.builder()
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
            .criteriaType(ScorecardTemplate.Criteria.CriteriaType.PASS_FAIL)
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
        assertEquals(ScorecardTemplate.Criteria.CriteriaType.PASS_FAIL, dto.getCriteriaType());
    }

    @Test
    void testSettersAndGetters() {
        ScorecardTemplate.Criteria dto = new ScorecardTemplate.Criteria();
        dto.setCriteriaId("val-criteriaId");
        dto.setCriteriaName("val-criteriaName");
        dto.setDescription("val-description");
        dto.setCategoryId("val-categoryId");
        dto.setIsCritical(true);
        dto.setIsRequired(true);
        dto.setOrder(99);
        dto.setScoringGuidance("val-scoringGuidance");
        dto.setCriteriaType(ScorecardTemplate.Criteria.CriteriaType.PASS_FAIL);
        assertEquals("val-criteriaId", dto.getCriteriaId());
        assertEquals("val-criteriaName", dto.getCriteriaName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-categoryId", dto.getCategoryId());
        assertTrue(dto.getIsCritical());
        assertTrue(dto.getIsRequired());
        assertEquals(99, dto.getOrder());
        assertEquals("val-scoringGuidance", dto.getScoringGuidance());
        assertEquals(ScorecardTemplate.Criteria.CriteriaType.PASS_FAIL, dto.getCriteriaType());
    }

    @Test
    void testEqualsAndHashCode() {
        ScorecardTemplate.Criteria dto1 = ScorecardTemplate.Criteria.builder()
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
            .criteriaType(ScorecardTemplate.Criteria.CriteriaType.PASS_FAIL)
            .build();
        ScorecardTemplate.Criteria dto2 = ScorecardTemplate.Criteria.builder()
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
            .criteriaType(ScorecardTemplate.Criteria.CriteriaType.PASS_FAIL)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ScorecardTemplate.Criteria dto = ScorecardTemplate.Criteria.builder()
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
            .criteriaType(ScorecardTemplate.Criteria.CriteriaType.PASS_FAIL)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}