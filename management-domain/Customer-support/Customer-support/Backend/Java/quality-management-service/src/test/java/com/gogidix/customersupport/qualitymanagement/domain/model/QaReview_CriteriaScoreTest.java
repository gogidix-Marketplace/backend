package com.gogidix.customersupport.qualitymanagement.domain.model;

import com.gogidix.customersupport.qualitymanagement.domain.model.QaReview;
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
class QaReview_CriteriaScoreTest {

        @Test
    void testBuilder() {
        QaReview.CriteriaScore dto = QaReview.CriteriaScore.builder()
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
        QaReview.CriteriaScore dto = new QaReview.CriteriaScore();
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
        QaReview.CriteriaScore dto1 = QaReview.CriteriaScore.builder()
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
        QaReview.CriteriaScore dto2 = QaReview.CriteriaScore.builder()
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
        QaReview.CriteriaScore dto = QaReview.CriteriaScore.builder()
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