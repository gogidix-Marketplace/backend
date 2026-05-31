package com.gogidix.customersupport.feedback.application.dto.response;

import com.gogidix.customersupport.feedback.application.dto.response.FeedbackAnalyticsDto;
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
class FeedbackAnalyticsDtoTest {

        @Test
    void testBuilder() {
        FeedbackAnalyticsDto dto = FeedbackAnalyticsDto.builder()
                        .totalFeedback(42L)
            .averageRating(null)
            .totalReviews(42L)
            .pendingReviews(42L)
            .followUpsRequired(42L)
            .followUpsCompleted(42L)
            .csatScore(null)
            .npsScore(42)
            .feedbackByType(Collections.emptyMap())
            .feedbackBySentiment(Collections.emptyMap())
            .averageRatingByAgent(Collections.emptyMap())
            .averageRatingByCategory(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalFeedback());
        assertEquals(42L, dto.getTotalReviews());
        assertEquals(42L, dto.getPendingReviews());
        assertEquals(42L, dto.getFollowUpsRequired());
        assertEquals(42L, dto.getFollowUpsCompleted());
        assertEquals(42, dto.getNpsScore());
    }

    @Test
    void testSettersAndGetters() {
        FeedbackAnalyticsDto dto = new FeedbackAnalyticsDto();
        dto.setNpsScore(99);
        assertEquals(99, dto.getNpsScore());
    }

    @Test
    void testEqualsAndHashCode() {
        FeedbackAnalyticsDto dto1 = FeedbackAnalyticsDto.builder()
                        .totalFeedback(42L)
            .averageRating(null)
            .totalReviews(42L)
            .pendingReviews(42L)
            .followUpsRequired(42L)
            .followUpsCompleted(42L)
            .csatScore(null)
            .npsScore(42)
            .feedbackByType(Collections.emptyMap())
            .feedbackBySentiment(Collections.emptyMap())
            .averageRatingByAgent(Collections.emptyMap())
            .averageRatingByCategory(Collections.emptyMap())
            .build();
        FeedbackAnalyticsDto dto2 = FeedbackAnalyticsDto.builder()
                        .totalFeedback(42L)
            .averageRating(null)
            .totalReviews(42L)
            .pendingReviews(42L)
            .followUpsRequired(42L)
            .followUpsCompleted(42L)
            .csatScore(null)
            .npsScore(42)
            .feedbackByType(Collections.emptyMap())
            .feedbackBySentiment(Collections.emptyMap())
            .averageRatingByAgent(Collections.emptyMap())
            .averageRatingByCategory(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        FeedbackAnalyticsDto dto = FeedbackAnalyticsDto.builder()
                        .totalFeedback(42L)
            .averageRating(null)
            .totalReviews(42L)
            .pendingReviews(42L)
            .followUpsRequired(42L)
            .followUpsCompleted(42L)
            .csatScore(null)
            .npsScore(42)
            .feedbackByType(Collections.emptyMap())
            .feedbackBySentiment(Collections.emptyMap())
            .averageRatingByAgent(Collections.emptyMap())
            .averageRatingByCategory(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}