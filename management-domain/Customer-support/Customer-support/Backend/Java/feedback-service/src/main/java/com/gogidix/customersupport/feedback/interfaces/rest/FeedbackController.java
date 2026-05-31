package com.gogidix.customersupport.feedback.interfaces.rest;

import com.gogidix.customersupport.feedback.application.dto.request.FeedbackRequestDto;
import com.gogidix.customersupport.feedback.application.dto.response.FeedbackAnalyticsDto;
import com.gogidix.customersupport.feedback.application.dto.response.FeedbackResponseDto;
import com.gogidix.customersupport.feedback.application.service.FeedbackService;
import com.gogidix.customersupport.feedback.domain.model.Feedback;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * REST Controller for Feedback management
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback Management", description = "APIs for managing customer feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping
    @Operation(summary = "Get all feedback", description = "Retrieve all feedback with pagination")
    public ResponseEntity<Page<FeedbackResponseDto>> getAllFeedback(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(feedbackService.getAllFeedback(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get feedback by ID", description = "Retrieve a specific feedback by ID")
    public ResponseEntity<FeedbackResponseDto> getFeedbackById(
            @Parameter(description = "Feedback ID") @PathVariable String id) {
        return ResponseEntity.ok(feedbackService.getFeedbackById(id));
    }

    @GetMapping("/feedback-id/{feedbackId}")
    @Operation(summary = "Get feedback by feedback ID", description = "Retrieve a specific feedback by feedback ID")
    public ResponseEntity<FeedbackResponseDto> getFeedbackByFeedbackId(
            @Parameter(description = "Feedback ID") @PathVariable String feedbackId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByFeedbackId(feedbackId));
    }

    @GetMapping("/ticket/{ticketId}")
    @Operation(summary = "Get feedback by ticket", description = "Retrieve all feedback for a ticket")
    public ResponseEntity<List<FeedbackResponseDto>> getFeedbackByTicketId(
            @Parameter(description = "Ticket ID") @PathVariable String ticketId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByTicketId(ticketId));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get feedback by customer", description = "Retrieve all feedback for a customer")
    public ResponseEntity<List<FeedbackResponseDto>> getFeedbackByCustomerId(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByCustomerId(customerId));
    }

    @GetMapping("/agent/{agentId}")
    @Operation(summary = "Get feedback by agent", description = "Retrieve all feedback for an agent")
    public ResponseEntity<List<FeedbackResponseDto>> getFeedbackByAgentId(
            @Parameter(description = "Agent ID") @PathVariable String agentId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByAgentId(agentId));
    }

    @GetMapping("/type/{feedbackType}")
    @Operation(summary = "Get feedback by type", description = "Retrieve feedback filtered by type")
    public ResponseEntity<List<FeedbackResponseDto>> getFeedbackByType(
            @Parameter(description = "Feedback type") @PathVariable Feedback.FeedbackType feedbackType) {
        return ResponseEntity.ok(feedbackService.getFeedbackByType(feedbackType));
    }

    @GetMapping("/unreviewed")
    @Operation(summary = "Get unreviewed feedback", description = "Retrieve all unreviewed feedback")
    public ResponseEntity<List<FeedbackResponseDto>> getUnreviewedFeedback() {
        return ResponseEntity.ok(feedbackService.getUnreviewedFeedback());
    }

    @GetMapping("/follow-up-required")
    @Operation(summary = "Get feedback requiring follow-up", description = "Retrieve all feedback requiring follow-up")
    public ResponseEntity<List<FeedbackResponseDto>> getFeedbackRequiringFollowUp() {
        return ResponseEntity.ok(feedbackService.getFeedbackRequiringFollowUp());
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get feedback by date range", description = "Retrieve feedback within a date range")
    public ResponseEntity<List<FeedbackResponseDto>> getFeedbackByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate) {
        return ResponseEntity.ok(feedbackService.getFeedbackByDateRange(startDate, endDate));
    }

    @GetMapping("/rating-range")
    @Operation(summary = "Get feedback by rating range", description = "Retrieve feedback within a rating range")
    public ResponseEntity<List<FeedbackResponseDto>> getFeedbackByRatingRange(
            @Parameter(description = "Minimum rating") @RequestParam Integer minRating,
            @Parameter(description = "Maximum rating") @RequestParam Integer maxRating) {
        return ResponseEntity.ok(feedbackService.getFeedbackByRatingRange(minRating, maxRating));
    }

    @PostMapping
    @Operation(summary = "Create feedback", description = "Create a new feedback")
    public ResponseEntity<FeedbackResponseDto> createFeedback(
            @Valid @RequestBody FeedbackRequestDto request) {
        FeedbackResponseDto created = feedbackService.createFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update feedback", description = "Update an existing feedback")
    public ResponseEntity<FeedbackResponseDto> updateFeedback(
            @Parameter(description = "Feedback ID") @PathVariable String id,
            @Valid @RequestBody FeedbackRequestDto request) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, request));
    }

    @PutMapping("/{id}/review")
    @Operation(summary = "Mark feedback as reviewed", description = "Mark feedback as reviewed")
    public ResponseEntity<FeedbackResponseDto> markAsReviewed(
            @Parameter(description = "Feedback ID") @PathVariable String id,
            @Parameter(description = "Reviewed by") @RequestParam String reviewedBy) {
        return ResponseEntity.ok(feedbackService.markAsReviewed(id, reviewedBy));
    }

    @PutMapping("/{id}/follow-up")
    @Operation(summary = "Request follow-up for feedback", description = "Request follow-up for feedback")
    public ResponseEntity<FeedbackResponseDto> requestFollowUp(
            @Parameter(description = "Feedback ID") @PathVariable String id) {
        return ResponseEntity.ok(feedbackService.requestFollowUp(id));
    }

    @PutMapping("/{id}/complete-follow-up")
    @Operation(summary = "Complete follow-up for feedback", description = "Complete follow-up for feedback")
    public ResponseEntity<FeedbackResponseDto> completeFollowUp(
            @Parameter(description = "Feedback ID") @PathVariable String id) {
        return ResponseEntity.ok(feedbackService.completeFollowUp(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete feedback", description = "Delete a feedback")
    public ResponseEntity<Void> deleteFeedback(
            @Parameter(description = "Feedback ID") @PathVariable String id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/analytics")
    @Operation(summary = "Get feedback analytics", description = "Retrieve feedback analytics")
    public ResponseEntity<FeedbackAnalyticsDto> getFeedbackAnalytics() {
        return ResponseEntity.ok(feedbackService.getFeedbackAnalytics());
    }
}
