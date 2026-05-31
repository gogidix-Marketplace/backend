package com.gogidix.customersupport.feedback.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Response DTO for feedback analytics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackAnalyticsDto {

    private Long totalFeedback;
    private Double averageRating;
    private Long totalReviews;
    private Long pendingReviews;
    private Long followUpsRequired;
    private Long followUpsCompleted;
    private Double csatScore;
    private Integer npsScore;
    private Map<String, Long> feedbackByType;
    private Map<String, Long> feedbackBySentiment;
    private Map<String, Double> averageRatingByAgent;
    private Map<String, Double> averageRatingByCategory;
}
