package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.gogidix.customersupport.qualitymanagement.application.dto.CalibrationSessionDto;
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
class CalibrationSessionDto_ReviewerScoreDtoTest {

        @Test
    void testBuilder() {
        CalibrationSessionDto.ReviewerScoreDto dto = CalibrationSessionDto.ReviewerScoreDto.builder()
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
        CalibrationSessionDto.ReviewerScoreDto dto = new CalibrationSessionDto.ReviewerScoreDto();
        dto.setReviewerId("val-reviewerId");
        dto.setReviewerName("val-reviewerName");
        dto.setComments("val-comments");
        assertEquals("val-reviewerId", dto.getReviewerId());
        assertEquals("val-reviewerName", dto.getReviewerName());
        assertEquals("val-comments", dto.getComments());
    }

    @Test
    void testEqualsAndHashCode() {
        CalibrationSessionDto.ReviewerScoreDto dto1 = CalibrationSessionDto.ReviewerScoreDto.builder()
                        .reviewerId("test-reviewerId")
            .reviewerName("test-reviewerName")
            .score(null)
            .maxScore(null)
            .percentageScore(null)
            .comments("test-comments")
            .submittedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CalibrationSessionDto.ReviewerScoreDto dto2 = CalibrationSessionDto.ReviewerScoreDto.builder()
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
        CalibrationSessionDto.ReviewerScoreDto dto = CalibrationSessionDto.ReviewerScoreDto.builder()
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