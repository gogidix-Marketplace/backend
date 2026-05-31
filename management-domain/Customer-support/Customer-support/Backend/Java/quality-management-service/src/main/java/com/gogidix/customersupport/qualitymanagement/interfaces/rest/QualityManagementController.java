package com.gogidix.customersupport.qualitymanagement.interfaces.rest;

import com.gogidix.customersupport.qualitymanagement.application.dto.AgentQualityProfileDto;
import com.gogidix.customersupport.qualitymanagement.application.dto.CalibrationSessionDto;
import com.gogidix.customersupport.qualitymanagement.application.dto.ScorecardTemplateDto;
import com.gogidix.customersupport.qualitymanagement.application.service.AgentQualityProfileService;
import com.gogidix.customersupport.qualitymanagement.application.service.CalibrationSessionService;
import com.gogidix.customersupport.qualitymanagement.application.service.ScorecardTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * REST Controller for Quality Management operations (Templates, Calibration, Profiles)
 */
@RestController
@RequestMapping("/api/v1/quality-management")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Quality Management", description = "Quality Management API - Templates, Calibration, Profiles")
public class QualityManagementController {

    private final ScorecardTemplateService templateService;
    private final CalibrationSessionService calibrationService;
    private final AgentQualityProfileService profileService;

    // ==================== SCORECARD TEMPLATES ====================

    /**
     * Create scorecard template
     */
    @PostMapping("/templates")
    @Operation(summary = "Create scorecard template", description = "Create a new scorecard template for QA evaluations")
    public ResponseEntity<ScorecardTemplateDto> createTemplate(
            @Valid @RequestBody ScorecardTemplateDto.CreateScorecardTemplateRequest request) {

        log.info("POST /api/v1/quality-management/templates - Creating template: {}", request.getTemplateName());
        ScorecardTemplateDto created = templateService.createTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get template by ID
     */
    @GetMapping("/templates/{templateId}")
    @Operation(summary = "Get scorecard template", description = "Retrieve a scorecard template by ID")
    public ResponseEntity<ScorecardTemplateDto> getTemplate(
            @Parameter(description = "Template ID")
            @PathVariable String templateId) {

        log.info("GET /api/v1/quality-management/templates/{}", templateId);
        ScorecardTemplateDto template = templateService.getTemplateById(templateId);
        return ResponseEntity.ok(template);
    }

    /**
     * Get all templates
     */
    @GetMapping("/templates")
    @Operation(summary = "Get all templates", description = "Retrieve all scorecard templates for a tenant")
    public ResponseEntity<List<ScorecardTemplateDto>> getAllTemplates(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/quality-management/templates");
        List<ScorecardTemplateDto> templates = templateService.getAllTemplates(tenantId);
        return ResponseEntity.ok(templates);
    }

    /**
     * Get active templates
     */
    @GetMapping("/templates/active")
    @Operation(summary = "Get active templates", description = "Retrieve all active scorecard templates")
    public ResponseEntity<List<ScorecardTemplateDto>> getActiveTemplates(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/quality-management/templates/active");
        List<ScorecardTemplateDto> templates = templateService.getActiveTemplates(tenantId);
        return ResponseEntity.ok(templates);
    }

    /**
     * Get templates by type
     */
    @GetMapping("/templates/type/{templateType}")
    @Operation(summary = "Get templates by type", description = "Retrieve scorecard templates by type")
    public ResponseEntity<List<ScorecardTemplateDto>> getTemplatesByType(
            @Parameter(description = "Template Type (CALL_SCORING, CHAT_SCORING, etc.)")
            @PathVariable String templateType,
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/quality-management/templates/type/{}", templateType);
        List<ScorecardTemplateDto> templates = templateService.getTemplatesByType(tenantId, templateType);
        return ResponseEntity.ok(templates);
    }

    /**
     * Get default template
     */
    @GetMapping("/templates/default")
    @Operation(summary = "Get default template", description = "Retrieve the default scorecard template")
    public ResponseEntity<ScorecardTemplateDto> getDefaultTemplate(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/quality-management/templates/default");
        ScorecardTemplateDto template = templateService.getDefaultTemplate(tenantId);
        return ResponseEntity.ok(template);
    }

    /**
     * Update template
     */
    @PutMapping("/templates/{templateId}")
    @Operation(summary = "Update template", description = "Update an existing scorecard template")
    public ResponseEntity<ScorecardTemplateDto> updateTemplate(
            @Parameter(description = "Template ID")
            @PathVariable String templateId,
            @Valid @RequestBody ScorecardTemplateDto.UpdateScorecardTemplateRequest request) {

        log.info("PUT /api/v1/quality-management/templates/{}", templateId);
        ScorecardTemplateDto updated = templateService.updateTemplate(templateId, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Activate template
     */
    @PostMapping("/templates/{templateId}/activate")
    @Operation(summary = "Activate template", description = "Activate a scorecard template")
    public ResponseEntity<ScorecardTemplateDto> activateTemplate(
            @Parameter(description = "Template ID")
            @PathVariable String templateId) {

        log.info("POST /api/v1/quality-management/templates/{}/activate", templateId);
        ScorecardTemplateDto activated = templateService.activateTemplate(templateId);
        return ResponseEntity.ok(activated);
    }

    /**
     * Deactivate template
     */
    @PostMapping("/templates/{templateId}/deactivate")
    @Operation(summary = "Deactivate template", description = "Deactivate a scorecard template")
    public ResponseEntity<ScorecardTemplateDto> deactivateTemplate(
            @Parameter(description = "Template ID")
            @PathVariable String templateId) {

        log.info("POST /api/v1/quality-management/templates/{}/deactivate", templateId);
        ScorecardTemplateDto deactivated = templateService.deactivateTemplate(templateId);
        return ResponseEntity.ok(deactivated);
    }

    /**
     * Set as default template
     */
    @PostMapping("/templates/{templateId}/set-default")
    @Operation(summary = "Set as default", description = "Set a template as the default template")
    public ResponseEntity<ScorecardTemplateDto> setAsDefault(
            @Parameter(description = "Template ID")
            @PathVariable String templateId,
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("POST /api/v1/quality-management/templates/{}/set-default", templateId);
        ScorecardTemplateDto updated = templateService.setAsDefault(templateId, tenantId);
        return ResponseEntity.ok(updated);
    }

    /**
     * Approve template
     */
    @PostMapping("/templates/{templateId}/approve")
    @Operation(summary = "Approve template", description = "Approve a scorecard template for use")
    public ResponseEntity<ScorecardTemplateDto> approveTemplate(
            @Parameter(description = "Template ID")
            @PathVariable String templateId,
            @Parameter(description = "Approver ID")
            @RequestParam String approverId) {

        log.info("POST /api/v1/quality-management/templates/{}/approve by: {}", templateId, approverId);
        ScorecardTemplateDto approved = templateService.approveTemplate(templateId, approverId);
        return ResponseEntity.ok(approved);
    }

    /**
     * Delete template
     */
    @DeleteMapping("/templates/{templateId}")
    @Operation(summary = "Delete template", description = "Delete a scorecard template")
    public ResponseEntity<Void> deleteTemplate(
            @Parameter(description = "Template ID")
            @PathVariable String templateId) {

        log.info("DELETE /api/v1/quality-management/templates/{}", templateId);
        templateService.deleteTemplate(templateId);
        return ResponseEntity.noContent().build();
    }

    // ==================== CALIBRATION SESSIONS ====================

    /**
     * Create calibration session
     */
    @PostMapping("/calibration")
    @Operation(summary = "Create calibration session", description = "Create a new calibration session")
    public ResponseEntity<CalibrationSessionDto> createCalibrationSession(
            @Valid @RequestBody CalibrationSessionDto.CreateCalibrationSessionRequest request) {

        log.info("POST /api/v1/quality-management/calibration - Creating session: {}", request.getSessionName());
        CalibrationSessionDto created = calibrationService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get calibration session by ID
     */
    @GetMapping("/calibration/{sessionId}")
    @Operation(summary = "Get calibration session", description = "Retrieve a calibration session by ID")
    public ResponseEntity<CalibrationSessionDto> getCalibrationSession(
            @Parameter(description = "Session ID")
            @PathVariable String sessionId) {

        log.info("GET /api/v1/quality-management/calibration/{}", sessionId);
        CalibrationSessionDto session = calibrationService.getSessionById(sessionId);
        return ResponseEntity.ok(session);
    }

    /**
     * Get all calibration sessions
     */
    @GetMapping("/calibration")
    @Operation(summary = "Get calibration sessions", description = "Retrieve all calibration sessions for a tenant")
    public ResponseEntity<List<CalibrationSessionDto>> getAllCalibrationSessions(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/quality-management/calibration");
        List<CalibrationSessionDto> sessions = calibrationService.getAllSessions(tenantId);
        return ResponseEntity.ok(sessions);
    }

    /**
     * Get upcoming calibration sessions
     */
    @GetMapping("/calibration/upcoming")
    @Operation(summary = "Get upcoming sessions", description = "Retrieve upcoming calibration sessions")
    public ResponseEntity<List<CalibrationSessionDto>> getUpcomingSessions(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/quality-management/calibration/upcoming");
        List<CalibrationSessionDto> sessions = calibrationService.getUpcomingSessions(tenantId);
        return ResponseEntity.ok(sessions);
    }

    /**
     * Get past calibration sessions
     */
    @GetMapping("/calibration/past")
    @Operation(summary = "Get past sessions", description = "Retrieve past calibration sessions")
    public ResponseEntity<List<CalibrationSessionDto>> getPastSessions(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/quality-management/calibration/past");
        List<CalibrationSessionDto> sessions = calibrationService.getPastSessions(tenantId);
        return ResponseEntity.ok(sessions);
    }

    /**
     * Update calibration session
     */
    @PutMapping("/calibration/{sessionId}")
    @Operation(summary = "Update calibration session", description = "Update an existing calibration session")
    public ResponseEntity<CalibrationSessionDto> updateCalibrationSession(
            @Parameter(description = "Session ID")
            @PathVariable String sessionId,
            @Valid @RequestBody CalibrationSessionDto.UpdateCalibrationSessionRequest request) {

        log.info("PUT /api/v1/quality-management/calibration/{}", sessionId);
        CalibrationSessionDto updated = calibrationService.updateSession(sessionId, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Start calibration session
     */
    @PostMapping("/calibration/{sessionId}/start")
    @Operation(summary = "Start calibration session", description = "Start a calibration session")
    public ResponseEntity<CalibrationSessionDto> startCalibrationSession(
            @Parameter(description = "Session ID")
            @PathVariable String sessionId) {

        log.info("POST /api/v1/quality-management/calibration/{}/start", sessionId);
        CalibrationSessionDto started = calibrationService.startSession(sessionId);
        return ResponseEntity.ok(started);
    }

    /**
     * Complete calibration session
     */
    @PostMapping("/calibration/{sessionId}/complete")
    @Operation(summary = "Complete calibration session", description = "Complete a calibration session")
    public ResponseEntity<CalibrationSessionDto> completeCalibrationSession(
            @Parameter(description = "Session ID")
            @PathVariable String sessionId) {

        log.info("POST /api/v1/quality-management/calibration/{}/complete", sessionId);
        CalibrationSessionDto completed = calibrationService.completeSession(sessionId);
        return ResponseEntity.ok(completed);
    }

    /**
     * Cancel calibration session
     */
    @PostMapping("/calibration/{sessionId}/cancel")
    @Operation(summary = "Cancel calibration session", description = "Cancel a calibration session")
    public ResponseEntity<CalibrationSessionDto> cancelCalibrationSession(
            @Parameter(description = "Session ID")
            @PathVariable String sessionId,
            @Parameter(description = "Cancellation reason")
            @RequestParam String reason) {

        log.info("POST /api/v1/quality-management/calibration/{}/cancel", sessionId);
        CalibrationSessionDto cancelled = calibrationService.cancelSession(sessionId, reason);
        return ResponseEntity.ok(cancelled);
    }

    /**
     * Delete calibration session
     */
    @DeleteMapping("/calibration/{sessionId}")
    @Operation(summary = "Delete calibration session", description = "Delete a calibration session")
    public ResponseEntity<Void> deleteCalibrationSession(
            @Parameter(description = "Session ID")
            @PathVariable String sessionId) {

        log.info("DELETE /api/v1/quality-management/calibration/{}", sessionId);
        calibrationService.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    // ==================== AGENT QUALITY PROFILES ====================

    /**
     * Get agent quality profile
     */
    @GetMapping("/profiles/{agentId}")
    @Operation(summary = "Get agent profile", description = "Retrieve quality profile for an agent")
    public ResponseEntity<AgentQualityProfileDto> getAgentProfile(
            @Parameter(description = "Agent ID")
            @PathVariable String agentId,
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/quality-management/profiles/{}", agentId);
        AgentQualityProfileDto profile = profileService.getProfileByAgentId(tenantId, agentId);
        return ResponseEntity.ok(profile);
    }

    /**
     * Get all profiles
     */
    @GetMapping("/profiles")
    @Operation(summary = "Get all profiles", description = "Retrieve all agent quality profiles for a tenant")
    public ResponseEntity<List<AgentQualityProfileDto>> getAllProfiles(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/quality-management/profiles");
        List<AgentQualityProfileDto> profiles = profileService.getAllProfiles(tenantId);
        return ResponseEntity.ok(profiles);
    }

    /**
     * Get top agents (leaderboard)
     */
    @GetMapping("/profiles/leaderboard")
    @Operation(summary = "Get quality leaderboard", description = "Retrieve top performing agents")
    public ResponseEntity<List<AgentQualityProfileDto.QualityLeaderboardDto>> getLeaderboard(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId,
            @Parameter(description = "Team ID (optional, filters by team)")
            @RequestParam(required = false) String teamId) {

        log.info("GET /api/v1/quality-management/profiles/leaderboard");
        List<AgentQualityProfileDto.QualityLeaderboardDto> leaderboard = profileService.getLeaderboard(tenantId, teamId);
        return ResponseEntity.ok(leaderboard);
    }

    /**
     * Get quality metrics
     */
    @GetMapping("/profiles/metrics")
    @Operation(summary = "Get quality metrics", description = "Retrieve aggregate quality metrics for a tenant")
    public ResponseEntity<AgentQualityProfileDto.QualityMetricsDto> getQualityMetrics(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/quality-management/profiles/metrics");
        AgentQualityProfileDto.QualityMetricsDto metrics = profileService.getQualityMetrics(tenantId);
        return ResponseEntity.ok(metrics);
    }

    /**
     * Recalculate agent profile
     */
    @PostMapping("/profiles/{agentId}/recalculate")
    @Operation(summary = "Recalculate agent profile", description = "Trigger recalculation of an agent's quality profile")
    public ResponseEntity<AgentQualityProfileDto> recalculateProfile(
            @Parameter(description = "Agent ID")
            @PathVariable String agentId,
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("POST /api/v1/quality-management/profiles/{}/recalculate", agentId);
        AgentQualityProfileDto profile = profileService.recalculateAgentProfile(tenantId, agentId);
        return ResponseEntity.ok(profile);
    }

    /**
     * Update profile goal
     */
    @PostMapping("/profiles/{agentId}/goal")
    @Operation(summary = "Update quality goal", description = "Update quality goal for an agent")
    public ResponseEntity<AgentQualityProfileDto> updateGoal(
            @Parameter(description = "Agent ID")
            @PathVariable String agentId,
            @Parameter(description = "Quality Goal")
            @RequestParam Double goal,
            @Parameter(description = "Target Date (ISO format)")
            @RequestParam String targetDate,
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("POST /api/v1/quality-management/profiles/{}/goal", agentId);
        Instant target = Instant.parse(targetDate);
        AgentQualityProfileDto profile = profileService.updateGoal(tenantId, agentId, goal, target);
        return ResponseEntity.ok(profile);
    }

    /**
     * Add coaching note
     */
    @PostMapping("/profiles/{agentId}/coaching-notes")
    @Operation(summary = "Add coaching note", description = "Add a coaching note to an agent's profile")
    public ResponseEntity<AgentQualityProfileDto> addCoachingNote(
            @Parameter(description = "Agent ID")
            @PathVariable String agentId,
            @Parameter(description = "Note content")
            @RequestParam String note,
            @Parameter(description = "Category")
            @RequestParam String category,
            @Parameter(description = "Created By (User ID)")
            @RequestParam String createdBy,
            @Parameter(description = "Created By Name")
            @RequestParam String createdByName,
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("POST /api/v1/quality-management/profiles/{}/coaching-notes", agentId);
        AgentQualityProfileDto profile = profileService.addCoachingNote(tenantId, agentId, note, category, createdBy, createdByName);
        return ResponseEntity.ok(profile);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the quality management service is healthy")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(
                new HealthResponse("UP", "Quality Management Service is running")
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
