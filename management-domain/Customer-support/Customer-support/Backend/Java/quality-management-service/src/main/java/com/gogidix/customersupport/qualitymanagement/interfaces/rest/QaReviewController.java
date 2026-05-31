package com.gogidix.customersupport.qualitymanagement.interfaces.rest;

import com.gogidix.customersupport.qualitymanagement.application.dto.QaReviewDto;
import com.gogidix.customersupport.qualitymanagement.application.service.QaReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * REST Controller for QA Review operations
 */
@RestController
@RequestMapping("/api/v1/qa-reviews")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "QA Reviews", description = "QA Review Management API")
public class QaReviewController {

    private final QaReviewService qaReviewService;

    /**
     * Create a new QA review
     */
    @PostMapping
    @Operation(summary = "Create QA review", description = "Create a new quality assurance review")
    public ResponseEntity<QaReviewDto> createReview(
            @Valid @RequestBody QaReviewDto.CreateQaReviewRequest request) {

        log.info("POST /api/v1/qa-reviews - Creating review for agent: {}, ticket: {}",
                request.getAgentId(), request.getTicketId());

        QaReviewDto created = qaReviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get review by ID
     */
    @GetMapping("/{reviewId}")
    @Operation(summary = "Get QA review", description = "Retrieve a QA review by ID")
    public ResponseEntity<QaReviewDto> getReview(
            @Parameter(description = "Review ID")
            @PathVariable String reviewId) {

        log.info("GET /api/v1/qa-reviews/{}", reviewId);
        QaReviewDto review = qaReviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    /**
     * Get reviews by agent
     */
    @GetMapping("/agent/{agentId}")
    @Operation(summary = "Get agent reviews", description = "Retrieve all QA reviews for a specific agent")
    public ResponseEntity<List<QaReviewDto>> getAgentReviews(
            @Parameter(description = "Agent ID")
            @PathVariable String agentId,
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/qa-reviews/agent/{}", agentId);
        List<QaReviewDto> reviews = qaReviewService.getReviewsByAgent(tenantId, agentId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get reviews by reviewer
     */
    @GetMapping("/reviewer/{reviewerId}")
    @Operation(summary = "Get reviewer's reviews", description = "Retrieve all QA reviews assigned to a specific reviewer")
    public ResponseEntity<List<QaReviewDto>> getReviewerReviews(
            @Parameter(description = "Reviewer ID")
            @PathVariable String reviewerId,
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/qa-reviews/reviewer/{}", reviewerId);
        List<QaReviewDto> reviews = qaReviewService.getReviewsByReviewer(tenantId, reviewerId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get reviews by status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get reviews by status", description = "Retrieve all QA reviews with a specific status")
    public ResponseEntity<List<QaReviewDto>> getReviewsByStatus(
            @Parameter(description = "Review Status (PENDING, IN_PROGRESS, COMPLETED, APPROVED, REJECTED)")
            @PathVariable String status,
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/qa-reviews/status/{}", status);
        List<QaReviewDto> reviews = qaReviewService.getReviewsByStatus(tenantId, status);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get pending reviews
     */
    @GetMapping("/pending")
    @Operation(summary = "Get pending reviews", description = "Retrieve all pending and in-progress QA reviews")
    public ResponseEntity<List<QaReviewDto>> getPendingReviews(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/qa-reviews/pending");
        List<QaReviewDto> reviews = qaReviewService.getPendingReviews(tenantId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get overdue reviews
     */
    @GetMapping("/overdue")
    @Operation(summary = "Get overdue reviews", description = "Retrieve all overdue QA reviews")
    public ResponseEntity<List<QaReviewDto>> getOverdueReviews(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/qa-reviews/overdue");
        List<QaReviewDto> reviews = qaReviewService.getOverdueReviews(tenantId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get reviews by date range
     */
    @GetMapping("/date-range")
    @Operation(summary = "Get reviews by date range", description = "Retrieve QA reviews within a date range")
    public ResponseEntity<List<QaReviewDto>> getReviewsByDateRange(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId,
            @Parameter(description = "Start Date (ISO format)")
            @RequestParam String startDate,
            @Parameter(description = "End Date (ISO format)")
            @RequestParam String endDate) {

        log.info("GET /api/v1/qa-reviews/date-range?startDate={}&endDate={}", startDate, endDate);

        Instant start = Instant.parse(startDate);
        Instant end = Instant.parse(endDate);

        List<QaReviewDto> reviews = qaReviewService.getReviewsByDateRange(tenantId, start, end);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get reviews with pagination
     */
    @GetMapping("/paginated")
    @Operation(summary = "Get reviews paginated", description = "Retrieve QA reviews with pagination")
    public ResponseEntity<Page<QaReviewDto>> getReviewsPaginated(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "reviewDate") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)")
            @RequestParam(defaultValue = "desc") String sortDir) {

        log.info("GET /api/v1/qa-reviews/paginated?page={}&size={}", page, size);
        Page<QaReviewDto> reviews = qaReviewService.getReviewsPaginated(tenantId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get review summaries for agent
     */
    @GetMapping("/agent/{agentId}/summaries")
    @Operation(summary = "Get agent review summaries", description = "Retrieve review summaries for a specific agent")
    public ResponseEntity<List<QaReviewDto.QaReviewSummaryDto>> getReviewSummariesForAgent(
            @Parameter(description = "Agent ID")
            @PathVariable String agentId,
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/qa-reviews/agent/{}/summaries", agentId);
        List<QaReviewDto.QaReviewSummaryDto> summaries = qaReviewService.getReviewSummariesForAgent(tenantId, agentId);
        return ResponseEntity.ok(summaries);
    }

    /**
     * Get review statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get review statistics", description = "Retrieve QA review statistics for a tenant")
    public ResponseEntity<QaReviewService.QaReviewStatisticsDto> getReviewStatistics(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId,
            @Parameter(description = "Start Date (ISO format)")
            @RequestParam String startDate,
            @Parameter(description = "End Date (ISO format)")
            @RequestParam String endDate) {

        log.info("GET /api/v1/qa-reviews/statistics");

        Instant start = Instant.parse(startDate);
        Instant end = Instant.parse(endDate);

        QaReviewService.QaReviewStatisticsDto stats = qaReviewService.getReviewStatistics(tenantId, start, end);
        return ResponseEntity.ok(stats);
    }

    /**
     * Update QA review
     */
    @PutMapping("/{reviewId}")
    @Operation(summary = "Update QA review", description = "Update an existing QA review")
    public ResponseEntity<QaReviewDto> updateReview(
            @Parameter(description = "Review ID")
            @PathVariable String reviewId,
            @Valid @RequestBody QaReviewDto.UpdateQaReviewRequest request) {

        log.info("PUT /api/v1/qa-reviews/{}", reviewId);
        QaReviewDto updated = qaReviewService.updateReview(reviewId, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Submit QA review
     */
    @PostMapping("/{reviewId}/submit")
    @Operation(summary = "Submit QA review", description = "Submit/complete a QA review for approval")
    public ResponseEntity<QaReviewDto> submitReview(
            @Parameter(description = "Review ID")
            @PathVariable String reviewId) {

        log.info("POST /api/v1/qa-reviews/{}/submit", reviewId);
        QaReviewDto submitted = qaReviewService.submitReview(reviewId);
        return ResponseEntity.ok(submitted);
    }

    /**
     * Approve QA review
     */
    @PostMapping("/{reviewId}/approve")
    @Operation(summary = "Approve QA review", description = "Approve a submitted QA review")
    public ResponseEntity<QaReviewDto> approveReview(
            @Parameter(description = "Review ID")
            @PathVariable String reviewId,
            @Parameter(description = "Approver ID")
            @RequestParam String approverId) {

        log.info("POST /api/v1/qa-reviews/{}/approve by: {}", reviewId, approverId);
        QaReviewDto approved = qaReviewService.approveReview(reviewId, approverId);
        return ResponseEntity.ok(approved);
    }

    /**
     * Reject QA review
     */
    @PostMapping("/{reviewId}/reject")
    @Operation(summary = "Reject QA review", description = "Reject a submitted QA review")
    public ResponseEntity<QaReviewDto> rejectReview(
            @Parameter(description = "Review ID")
            @PathVariable String reviewId,
            @Parameter(description = "Rejection reason")
            @RequestParam String reason) {

        log.info("POST /api/v1/qa-reviews/{}/reject", reviewId);
        QaReviewDto rejected = qaReviewService.rejectReview(reviewId, reason);
        return ResponseEntity.ok(rejected);
    }

    /**
     * Delete QA review
     */
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Delete QA review", description = "Delete a QA review")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "Review ID")
            @PathVariable String reviewId) {

        log.info("DELETE /api/v1/qa-reviews/{}", reviewId);
        qaReviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the QA reviews service is healthy")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(
                new HealthResponse("UP", "QA Reviews Service is running")
        );
    }

    /**
     * Exception handler for IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("BAD_REQUEST", ex.getMessage()));
    }

    /**
     * Exception handler for general exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"));
    }

    /**
     * Health response record
     */
    private record HealthResponse(String status, String message) {}

    /**
     * Error response record
     */
    private record ErrorResponse(String code, String message) {}
}
