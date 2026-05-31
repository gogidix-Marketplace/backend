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
class CalibrationSessionDto_CalibrationReviewDtoTest {

        @Test
    void testBuilder() {
        CalibrationSessionDto.CalibrationReviewDto dto = CalibrationSessionDto.CalibrationReviewDto.builder()
                        .reviewId("test-reviewId")
            .interactionId("test-interactionId")
            .ticketId("test-ticketId")
            .reviewerScores(Collections.emptyList())
            .averageScore(null)
            .scoreVariance(null)
            .standardDeviation(null)
            .reviewerCount(42)
            .isOutlier(true)
            .outlierReason("test-outlierReason")
            .build();
        assertNotNull(dto);
        assertEquals("test-reviewId", dto.getReviewId());
        assertEquals("test-interactionId", dto.getInteractionId());
        assertEquals("test-ticketId", dto.getTicketId());
        assertEquals(42, dto.getReviewerCount());
        assertTrue(dto.getIsOutlier());
        assertEquals("test-outlierReason", dto.getOutlierReason());
    }

    @Test
    void testSettersAndGetters() {
        CalibrationSessionDto.CalibrationReviewDto dto = new CalibrationSessionDto.CalibrationReviewDto();
        dto.setReviewId("val-reviewId");
        dto.setInteractionId("val-interactionId");
        dto.setTicketId("val-ticketId");
        dto.setReviewerCount(99);
        dto.setIsOutlier(true);
        dto.setOutlierReason("val-outlierReason");
        assertEquals("val-reviewId", dto.getReviewId());
        assertEquals("val-interactionId", dto.getInteractionId());
        assertEquals("val-ticketId", dto.getTicketId());
        assertEquals(99, dto.getReviewerCount());
        assertTrue(dto.getIsOutlier());
        assertEquals("val-outlierReason", dto.getOutlierReason());
    }

    @Test
    void testEqualsAndHashCode() {
        CalibrationSessionDto.CalibrationReviewDto dto1 = CalibrationSessionDto.CalibrationReviewDto.builder()
                        .reviewId("test-reviewId")
            .interactionId("test-interactionId")
            .ticketId("test-ticketId")
            .reviewerScores(Collections.emptyList())
            .averageScore(null)
            .scoreVariance(null)
            .standardDeviation(null)
            .reviewerCount(42)
            .isOutlier(true)
            .outlierReason("test-outlierReason")
            .build();
        CalibrationSessionDto.CalibrationReviewDto dto2 = CalibrationSessionDto.CalibrationReviewDto.builder()
                        .reviewId("test-reviewId")
            .interactionId("test-interactionId")
            .ticketId("test-ticketId")
            .reviewerScores(Collections.emptyList())
            .averageScore(null)
            .scoreVariance(null)
            .standardDeviation(null)
            .reviewerCount(42)
            .isOutlier(true)
            .outlierReason("test-outlierReason")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CalibrationSessionDto.CalibrationReviewDto dto = CalibrationSessionDto.CalibrationReviewDto.builder()
                        .reviewId("test-reviewId")
            .interactionId("test-interactionId")
            .ticketId("test-ticketId")
            .reviewerScores(Collections.emptyList())
            .averageScore(null)
            .scoreVariance(null)
            .standardDeviation(null)
            .reviewerCount(42)
            .isOutlier(true)
            .outlierReason("test-outlierReason")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}