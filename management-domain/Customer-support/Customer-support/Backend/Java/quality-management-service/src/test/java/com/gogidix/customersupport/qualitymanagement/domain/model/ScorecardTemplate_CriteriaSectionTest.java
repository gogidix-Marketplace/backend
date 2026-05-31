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
class ScorecardTemplate_CriteriaSectionTest {

        @Test
    void testBuilder() {
        ScorecardTemplate.CriteriaSection dto = ScorecardTemplate.CriteriaSection.builder()
                        .sectionId("test-sectionId")
            .sectionName("test-sectionName")
            .description("test-description")
            .order(42)
            .weight(null)
            .maxScore(null)
            .criteria(Collections.emptyList())
            .isRequired(true)
            .instructions("test-instructions")
            .build();
        assertNotNull(dto);
        assertEquals("test-sectionId", dto.getSectionId());
        assertEquals("test-sectionName", dto.getSectionName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(42, dto.getOrder());
        assertTrue(dto.getIsRequired());
        assertEquals("test-instructions", dto.getInstructions());
    }

    @Test
    void testSettersAndGetters() {
        ScorecardTemplate.CriteriaSection dto = new ScorecardTemplate.CriteriaSection();
        dto.setSectionId("val-sectionId");
        dto.setSectionName("val-sectionName");
        dto.setDescription("val-description");
        dto.setOrder(99);
        dto.setIsRequired(true);
        dto.setInstructions("val-instructions");
        assertEquals("val-sectionId", dto.getSectionId());
        assertEquals("val-sectionName", dto.getSectionName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getOrder());
        assertTrue(dto.getIsRequired());
        assertEquals("val-instructions", dto.getInstructions());
    }

    @Test
    void testEqualsAndHashCode() {
        ScorecardTemplate.CriteriaSection dto1 = ScorecardTemplate.CriteriaSection.builder()
                        .sectionId("test-sectionId")
            .sectionName("test-sectionName")
            .description("test-description")
            .order(42)
            .weight(null)
            .maxScore(null)
            .criteria(Collections.emptyList())
            .isRequired(true)
            .instructions("test-instructions")
            .build();
        ScorecardTemplate.CriteriaSection dto2 = ScorecardTemplate.CriteriaSection.builder()
                        .sectionId("test-sectionId")
            .sectionName("test-sectionName")
            .description("test-description")
            .order(42)
            .weight(null)
            .maxScore(null)
            .criteria(Collections.emptyList())
            .isRequired(true)
            .instructions("test-instructions")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ScorecardTemplate.CriteriaSection dto = ScorecardTemplate.CriteriaSection.builder()
                        .sectionId("test-sectionId")
            .sectionName("test-sectionName")
            .description("test-description")
            .order(42)
            .weight(null)
            .maxScore(null)
            .criteria(Collections.emptyList())
            .isRequired(true)
            .instructions("test-instructions")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}