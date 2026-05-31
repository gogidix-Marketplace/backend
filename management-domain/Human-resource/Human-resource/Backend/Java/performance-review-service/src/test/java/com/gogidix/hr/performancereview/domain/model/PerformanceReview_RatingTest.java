package com.gogidix.hr.performancereview.domain.model;

import com.gogidix.hr.performancereview.domain.model.PerformanceReview;
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
class PerformanceReview_RatingTest {

        @Test
    void testSettersAndGetters() {
        PerformanceReview.Rating dto = new PerformanceReview.Rating();
        dto.setCategory("val-category");
        dto.setScore(99);
        dto.setMaxScore(99);
        dto.setComments("val-comments");
        assertEquals("val-category", dto.getCategory());
        assertEquals(99, dto.getScore());
        assertEquals(99, dto.getMaxScore());
        assertEquals("val-comments", dto.getComments());
    }

    @Test
    void testEqualsAndHashCode() {
        PerformanceReview.Rating dto1 = new PerformanceReview.Rating();
        PerformanceReview.Rating dto2 = new PerformanceReview.Rating();
        dto1.setCategory("test");
        dto1.setScore(42);
        dto1.setMaxScore(42);
        dto1.setComments("test");
        dto1.setWeight(null);
        dto2.setCategory("test");
        dto2.setScore(42);
        dto2.setMaxScore(42);
        dto2.setComments("test");
        dto2.setWeight(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setCategory(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PerformanceReview.Rating dto = new PerformanceReview.Rating();
        dto.setCategory("test");
        dto.setScore(42);
        dto.setMaxScore(42);
        dto.setComments("test");
        dto.setWeight(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PerformanceReview.Rating dto = new PerformanceReview.Rating();
        dto.setCategory("test");
        dto.setScore(42);
        dto.setMaxScore(42);
        dto.setComments("test");
        dto.setWeight(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}