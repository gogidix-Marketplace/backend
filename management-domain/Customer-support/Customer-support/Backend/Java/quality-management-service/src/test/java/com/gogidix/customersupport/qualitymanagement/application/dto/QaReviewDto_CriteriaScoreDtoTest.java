package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.gogidix.customersupport.qualitymanagement.application.dto.QaReviewDto;
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
class QaReviewDto_CriteriaScoreDtoTest {

        @Test
    void testBuilder() {
        QaReviewDto.CriteriaScoreDto dto = QaReviewDto.CriteriaScoreDto.builder()
                        .criteriaId("test-criteriaId")
            .criteriaName("test-criteriaName")
            .category("test-category")
            .score(null)
            .maxScore(null)
            .weight(null)
            .comments("test-comments")
            .isCritical(true)
            .passed(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-criteriaId", dto.getCriteriaId());
        assertEquals("test-criteriaName", dto.getCriteriaName());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-comments", dto.getComments());
        assertTrue(dto.getIsCritical());
        assertTrue(dto.getPassed());
    }

    @Test
    void testSettersAndGetters() {
        QaReviewDto.CriteriaScoreDto dto = new QaReviewDto.CriteriaScoreDto();
        dto.setCriteriaId("val-criteriaId");
        dto.setCriteriaName("val-criteriaName");
        dto.setCategory("val-category");
        dto.setComments("val-comments");
        dto.setIsCritical(true);
        dto.setPassed(true);
        assertEquals("val-criteriaId", dto.getCriteriaId());
        assertEquals("val-criteriaName", dto.getCriteriaName());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-comments", dto.getComments());
        assertTrue(dto.getIsCritical());
        assertTrue(dto.getPassed());
    }

    @Test
    void testEqualsAndHashCode() {
        QaReviewDto.CriteriaScoreDto dto1 = QaReviewDto.CriteriaScoreDto.builder()
                        .criteriaId("test-criteriaId")
            .criteriaName("test-criteriaName")
            .category("test-category")
            .score(null)
            .maxScore(null)
            .weight(null)
            .comments("test-comments")
            .isCritical(true)
            .passed(true)
            .build();
        QaReviewDto.CriteriaScoreDto dto2 = QaReviewDto.CriteriaScoreDto.builder()
                        .criteriaId("test-criteriaId")
            .criteriaName("test-criteriaName")
            .category("test-category")
            .score(null)
            .maxScore(null)
            .weight(null)
            .comments("test-comments")
            .isCritical(true)
            .passed(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        QaReviewDto.CriteriaScoreDto dto = QaReviewDto.CriteriaScoreDto.builder()
                        .criteriaId("test-criteriaId")
            .criteriaName("test-criteriaName")
            .category("test-category")
            .score(null)
            .maxScore(null)
            .weight(null)
            .comments("test-comments")
            .isCritical(true)
            .passed(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}