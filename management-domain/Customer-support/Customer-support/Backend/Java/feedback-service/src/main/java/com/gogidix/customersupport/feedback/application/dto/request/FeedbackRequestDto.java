package com.gogidix.customersupport.feedback.application.dto.request;

import com.gogidix.customersupport.feedback.domain.model.Feedback;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Request DTO for creating/updating feedback
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequestDto {

    @NotBlank(message = "Ticket ID is required")
    private String ticketId;

    private String chatSessionId;

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    private String customerName;

    private String customerEmail;

    @NotNull(message = "Feedback type is required")
    private Feedback.FeedbackType feedbackType;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating must be at most 10")
    private Integer rating;

    private Integer npsScore;

    private Integer csatScore;

    private Map<String, Integer> categories;

    private String comment;

    private Feedback.SentimentType sentiment;

    private String agentId;

    private String agentName;

    private String sourceChannel;

    private List<String> tags;
}
