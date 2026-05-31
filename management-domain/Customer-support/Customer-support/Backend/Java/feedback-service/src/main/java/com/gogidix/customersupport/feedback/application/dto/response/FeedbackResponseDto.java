package com.gogidix.customersupport.feedback.application.dto.response;

import com.gogidix.customersupport.feedback.domain.model.Feedback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Response DTO for feedback
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponseDto {

    private String id;
    private String feedbackId;
    private String tenantId;
    private String ticketId;
    private String chatSessionId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private Feedback.FeedbackType feedbackType;
    private Integer rating;
    private Integer npsScore;
    private Integer csatScore;
    private Map<String, Integer> categories;
    private String comment;
    private Feedback.SentimentType sentiment;
    private String agentId;
    private String agentName;
    private String sourceChannel;
    private Instant submittedAt;
    private Boolean reviewed;
    private Instant reviewedAt;
    private String reviewedBy;
    private List<String> tags;
    private Boolean followUpRequired;
    private Feedback.FollowUpStatus followUpStatus;
    private Boolean responseSent;
    private Instant responseSentAt;
    private Instant createdAt;
    private Instant updatedAt;
}
