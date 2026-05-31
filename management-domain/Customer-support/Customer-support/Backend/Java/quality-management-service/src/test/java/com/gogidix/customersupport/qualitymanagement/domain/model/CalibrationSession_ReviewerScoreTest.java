package com.gogidix.customersupport.qualitymanagement.domain.model;

import com.gogidix.customersupport.qualitymanagement.domain.model.CalibrationSession;
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
class CalibrationSession_ReviewerScoreTest {

        @Test
    void testBuilder() {
        CalibrationSession.ReviewerScore dto = CalibrationSession.ReviewerScore.builder()
                        .reviewerId("test-reviewerId")
            .reviewerName("test-reviewerName")
            .score(null)
            .maxScore(null)
            .percentageScore(null)
            .comments("test-comments")
            .submittedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-reviewerId", dto.getReviewerId());
        assertEquals("test-reviewerName", dto.getReviewerName());
        assertEquals("test-comments", dto.getComments());
    }

    @Test
    void testSettersAndGetters() {
        CalibrationSession.ReviewerScore dto = new CalibrationSession.ReviewerScore();
        dto.setReviewerId("val-reviewerId");
        dto.setReviewerName("val-reviewerName");
        dto.setComments("val-comments");
        assertEquals("val-reviewerId", dto.getReviewerId());
        assertEquals("val-reviewerName", dto.getReviewerName());
        assertEquals("val-comments", dto.getComments());
    }

    @Test
    void testEqualsAndHashCode() {
        CalibrationSession.ReviewerScore dto1 = CalibrationSession.ReviewerScore.builder()
                        .reviewerId("test-reviewerId")
            .reviewerName("test-reviewerName")
            .score(null)
            .maxScore(null)
            .percentageScore(null)
            .comments("test-comments")
            .submittedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CalibrationSession.ReviewerScore dto2 = CalibrationSession.ReviewerScore.builder()
                        .reviewerId("test-reviewerId")
            .reviewerName("test-reviewerName")
            .score(null)
            .maxScore(null)
            .percentageScore(null)
            .comments("test-comments")
            .submittedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CalibrationSession.ReviewerScore dto = CalibrationSession.ReviewerScore.builder()
                        .reviewerId("test-reviewerId")
            .reviewerName("test-reviewerName")
            .score(null)
            .maxScore(null)
            .percentageScore(null)
            .comments("test-comments")
            .submittedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}