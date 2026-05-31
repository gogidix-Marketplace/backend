package com.gogidix.customersupport.qualitymanagement.application.service;

import com.gogidix.customersupport.qualitymanagement.application.dto.CalibrationSessionDto;
import com.gogidix.customersupport.qualitymanagement.application.mapper.QualityManagementMapper;
import com.gogidix.customersupport.qualitymanagement.domain.model.CalibrationSession;
import com.gogidix.customersupport.qualitymanagement.domain.repository.CalibrationSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Calibration Session operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CalibrationSessionService {

    private final CalibrationSessionRepository sessionRepository;
    private final QualityManagementMapper mapper;

    private static final String DEFAULT_TENANT_ID = "default";

    /**
     * Create a new calibration session
     */
    @Transactional
    @CacheEvict(value = "calibrationSessions", allEntries = true)
    public CalibrationSessionDto createSession(CalibrationSessionDto.CreateCalibrationSessionRequest request) {
        log.info("Creating calibration session for tenant: {}, name: {}",
                request.getTenantId(), request.getSessionName());

        CalibrationSession session = new CalibrationSession(request.getTenantId());
        session.setSessionName(request.getSessionName());
        session.setSessionCode(request.getSessionCode());
        session.setDescription(request.getDescription());

        if (request.getSessionType() != null) {
            session.setSessionType(CalibrationSession.SessionType.valueOf(request.getSessionType()));
        }

        session.setFacilitatorId(request.getFacilitatorId());
        session.setFacilitatorName(request.getFacilitatorName());
        session.setScheduledDate(request.getScheduledDate());
        session.setScheduledEndDate(request.getScheduledEndDate());
        session.setScorecardTemplateId(request.getScorecardTemplateId());
        session.setTargetInteractionsCount(request.getTargetInteractionsCount());
        session.setMinParticipants(request.getMinParticipants());
        session.setMaxParticipants(request.getMaxParticipants());
        session.setLocation(request.getLocation());
        session.setIsVirtual(request.getIsVirtual());
        session.setMeetingLink(request.getMeetingLink());
        session.setTags(request.getTags() != null ? request.getTags() : new java.util.ArrayList<>());

        // Add participants
        if (request.getParticipants() != null) {
            for (CalibrationSessionDto.ParticipantDto participantDto : request.getParticipants()) {
                CalibrationSession.Participant participant = CalibrationSession.Participant.builder()
                        .participantId(participantDto.getParticipantId())
                        .participantName(participantDto.getParticipantName())
                        .participantEmail(participantDto.getParticipantEmail())
                        .role(participantDto.getRole() != null ?
                                CalibrationSession.Participant.ParticipantRole.valueOf(participantDto.getRole()) : null)
                        .teamId(participantDto.getTeamId())
                        .teamName(participantDto.getTeamName())
                        .hasAttended(false)
                        .build();

                session.addParticipant(participant);
            }
        }

        CalibrationSession saved = sessionRepository.save(session);
        log.info("Created calibration session with ID: {}", saved.getSessionId());

        return mapper.toDto(saved);
    }

    /**
     * Get session by ID
     */
    @Cacheable(value = "calibrationSessions", key = "#sessionId")
    public CalibrationSessionDto getSessionById(String sessionId) {
        log.debug("Fetching calibration session: {}", sessionId);
        return sessionRepository.findBySessionId(sessionId)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Calibration session not found: " + sessionId));
    }

    /**
     * Get all sessions for tenant
     */
    public List<CalibrationSessionDto> getAllSessions(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return sessionRepository.findByTenantIdOrderByScheduledDateDesc(tenantId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get sessions by status
     */
    public List<CalibrationSessionDto> getSessionsByStatus(String tenantId, String status) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        CalibrationSession.SessionStatus sessionStatus = CalibrationSession.SessionStatus.valueOf(status);

        return sessionRepository.findByTenantIdAndSessionStatusOrderByScheduledDateDesc(tenantId, sessionStatus)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get upcoming sessions
     */
    public List<CalibrationSessionDto> getUpcomingSessions(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return sessionRepository.findUpcomingSessions(tenantId, Instant.now())
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get past sessions
     */
    public List<CalibrationSessionDto> getPastSessions(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return sessionRepository.findPastSessions(tenantId, Instant.now())
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get sessions by facilitator
     */
    public List<CalibrationSessionDto> getSessionsByFacilitator(String tenantId, String facilitatorId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return sessionRepository.findByTenantIdAndFacilitatorIdOrderByScheduledDateDesc(tenantId, facilitatorId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Update calibration session
     */
    @Transactional
    @CacheEvict(value = "calibrationSessions", allEntries = true)
    public CalibrationSessionDto updateSession(String sessionId, CalibrationSessionDto.UpdateCalibrationSessionRequest request) {
        log.info("Updating calibration session: {}", sessionId);

        CalibrationSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Calibration session not found: " + sessionId));

        if (request.getSessionName() != null) {
            session.setSessionName(request.getSessionName());
        }
        if (request.getDescription() != null) {
            session.setDescription(request.getDescription());
        }
        if (request.getSessionStatus() != null) {
            session.setSessionStatus(CalibrationSession.SessionStatus.valueOf(request.getSessionStatus()));
        }
        if (request.getFacilitatorId() != null) {
            session.setFacilitatorId(request.getFacilitatorId());
        }
        if (request.getScheduledDate() != null) {
            session.setScheduledDate(request.getScheduledDate());
        }
        if (request.getScheduledEndDate() != null) {
            session.setScheduledEndDate(request.getScheduledEndDate());
        }
        if (request.getScorecardTemplateId() != null) {
            session.setScorecardTemplateId(request.getScorecardTemplateId());
        }
        if (request.getCalibrationThreshold() != null) {
            session.setCalibrationThreshold(request.getCalibrationThreshold());
        }
        if (request.getNotes() != null) {
            session.setNotes(request.getNotes());
        }
        if (request.getFollowUpRequired() != null) {
            session.setFollowUpRequired(request.getFollowUpRequired());
        }
        if (request.getFollowUpDate() != null) {
            session.setFollowUpDate(request.getFollowUpDate());
        }

        // Update participants if provided
        if (request.getParticipants() != null) {
            session.setParticipants(request.getParticipants().stream()
                    .map(pDto -> CalibrationSession.Participant.builder()
                            .participantId(pDto.getParticipantId())
                            .participantName(pDto.getParticipantName())
                            .participantEmail(pDto.getParticipantEmail())
                            .role(pDto.getRole() != null ?
                                    CalibrationSession.Participant.ParticipantRole.valueOf(pDto.getRole()) : null)
                            .teamId(pDto.getTeamId())
                            .teamName(pDto.getTeamName())
                            .hasAttended(pDto.getHasAttended() != null ? pDto.getHasAttended() : false)
                            .joinedAt(pDto.getJoinedAt())
                            .leftAt(pDto.getLeftAt())
                            .complianceScore(pDto.getComplianceScore())
                            .build())
                    .collect(Collectors.toList()));
        }

        // Update calibration reviews if provided
        if (request.getCalibrationReviews() != null) {
            session.setCalibrationReviews(request.getCalibrationReviews().stream()
                    .map(rDto -> {
                        CalibrationSession.CalibrationReview review = CalibrationSession.CalibrationReview.builder()
                                .reviewId(rDto.getReviewId())
                                .interactionId(rDto.getInteractionId())
                                .ticketId(rDto.getTicketId())
                                .averageScore(rDto.getAverageScore())
                                .scoreVariance(rDto.getScoreVariance())
                                .standardDeviation(rDto.getStandardDeviation())
                                .reviewerCount(rDto.getReviewerCount())
                                .isOutlier(rDto.getIsOutlier())
                                .outlierReason(rDto.getOutlierReason())
                                .build();

                        if (rDto.getReviewerScores() != null) {
                            review.setReviewerScores(rDto.getReviewerScores().stream()
                                    .map(rsDto -> CalibrationSession.ReviewerScore.builder()
                                            .reviewerId(rsDto.getReviewerId())
                                            .reviewerName(rsDto.getReviewerName())
                                            .score(rsDto.getScore())
                                            .maxScore(rsDto.getMaxScore())
                                            .percentageScore(rsDto.getPercentageScore())
                                            .comments(rsDto.getComments())
                                            .submittedAt(rsDto.getSubmittedAt())
                                            .build())
                                    .collect(Collectors.toList()));
                        }

                        return review;
                    })
                    .collect(Collectors.toList()));

            // Recalculate calibration metrics
            session.calculateInterRaterReliability();
            session.determineCalibrationResult();
        }

        // Update findings if provided
        if (request.getFindings() != null) {
            session.setFindings(request.getFindings());
        }

        // Update action items if provided
        if (request.getActionItems() != null) {
            session.setActionItems(request.getActionItems().stream()
                    .map(aiDto -> CalibrationSession.ActionItem.builder()
                            .actionItemId(aiDto.getActionItemId())
                            .description(aiDto.getDescription())
                            .assignedTo(aiDto.getAssignedTo())
                            .assignedToName(aiDto.getAssignedToName())
                            .status(aiDto.getStatus() != null ?
                                    CalibrationSession.ActionItem.ActionItemStatus.valueOf(aiDto.getStatus()) : null)
                            .dueDate(aiDto.getDueDate())
                            .completedAt(aiDto.getCompletedAt())
                            .notes(aiDto.getNotes())
                            .build())
                    .collect(Collectors.toList()));
        }

        session.updateTimestamp();

        CalibrationSession saved = sessionRepository.save(session);
        log.info("Updated calibration session: {}", sessionId);

        return mapper.toDto(saved);
    }

    /**
     * Start calibration session
     */
    @Transactional
    @CacheEvict(value = "calibrationSessions", allEntries = true)
    public CalibrationSessionDto startSession(String sessionId) {
        log.info("Starting calibration session: {}", sessionId);

        CalibrationSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Calibration session not found: " + sessionId));

        session.setSessionStatus(CalibrationSession.SessionStatus.IN_PROGRESS);
        session.setActualStartDate(Instant.now());
        session.updateTimestamp();

        CalibrationSession saved = sessionRepository.save(session);
        return mapper.toDto(saved);
    }

    /**
     * Complete calibration session
     */
    @Transactional
    @CacheEvict(value = "calibrationSessions", allEntries = true)
    public CalibrationSessionDto completeSession(String sessionId) {
        log.info("Completing calibration session: {}", sessionId);

        CalibrationSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Calibration session not found: " + sessionId));

        session.setSessionStatus(CalibrationSession.SessionStatus.COMPLETED);
        session.setActualEndDate(Instant.now());

        if (session.getActualStartDate() != null) {
            long durationMinutes = java.time.Duration.between(session.getActualStartDate(), session.getActualEndDate()).toMinutes();
            session.setDurationMinutes((int) durationMinutes);
        }

        session.calculateInterRaterReliability();
        session.determineCalibrationResult();
        session.updateTimestamp();

        CalibrationSession saved = sessionRepository.save(session);
        log.info("Completed calibration session: {} with score: {}", sessionId, saved.getCalibrationScore());

        return mapper.toDto(saved);
    }

    /**
     * Cancel calibration session
     */
    @Transactional
    @CacheEvict(value = "calibrationSessions", allEntries = true)
    public CalibrationSessionDto cancelSession(String sessionId, String reason) {
        log.info("Cancelling calibration session: {}, reason: {}", sessionId, reason);

        CalibrationSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Calibration session not found: " + sessionId));

        session.setSessionStatus(CalibrationSession.SessionStatus.CANCELLED);
        session.setNotes(session.getNotes() != null ? session.getNotes() + "\nCancellation: " + reason : "Cancellation: " + reason);
        session.updateTimestamp();

        CalibrationSession saved = sessionRepository.save(session);
        return mapper.toDto(saved);
    }

    /**
     * Add participant to session
     */
    @Transactional
    @CacheEvict(value = "calibrationSessions", allEntries = true)
    public CalibrationSessionDto addParticipant(String sessionId, CalibrationSessionDto.ParticipantDto participantDto) {
        log.info("Adding participant to calibration session: {}", sessionId);

        CalibrationSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Calibration session not found: " + sessionId));

        CalibrationSession.Participant participant = CalibrationSession.Participant.builder()
                .participantId(participantDto.getParticipantId())
                .participantName(participantDto.getParticipantName())
                .participantEmail(participantDto.getParticipantEmail())
                .role(participantDto.getRole() != null ?
                        CalibrationSession.Participant.ParticipantRole.valueOf(participantDto.getRole()) : null)
                .teamId(participantDto.getTeamId())
                .teamName(participantDto.getTeamName())
                .hasAttended(false)
                .build();

        session.addParticipant(participant);
        CalibrationSession saved = sessionRepository.save(session);

        return mapper.toDto(saved);
    }

    /**
     * Mark participant as attended
     */
    @Transactional
    @CacheEvict(value = "calibrationSessions", allEntries = true)
    public CalibrationSessionDto markParticipantAttended(String sessionId, String participantId) {
        log.info("Marking participant {} as attended for session: {}", participantId, sessionId);

        CalibrationSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Calibration session not found: " + sessionId));

        session.getParticipants().stream()
                .filter(p -> p.getParticipantId().equals(participantId))
                .findFirst()
                .ifPresent(p -> {
                    p.setHasAttended(true);
                    p.setJoinedAt(Instant.now());
                });

        session.updateTimestamp();
        CalibrationSession saved = sessionRepository.save(session);

        return mapper.toDto(saved);
    }

    /**
     * Add calibration review
     */
    @Transactional
    @CacheEvict(value = "calibrationSessions", allEntries = true)
    public CalibrationSessionDto addCalibrationReview(String sessionId, CalibrationSessionDto.CalibrationReviewDto reviewDto) {
        log.info("Adding calibration review to session: {}", sessionId);

        CalibrationSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Calibration session not found: " + sessionId));

        CalibrationSession.CalibrationReview review = CalibrationSession.CalibrationReview.builder()
                .reviewId(reviewDto.getReviewId())
                .interactionId(reviewDto.getInteractionId())
                .ticketId(reviewDto.getTicketId())
                .averageScore(reviewDto.getAverageScore())
                .scoreVariance(reviewDto.getScoreVariance())
                .standardDeviation(reviewDto.getStandardDeviation())
                .reviewerCount(reviewDto.getReviewerCount())
                .isOutlier(reviewDto.getIsOutlier())
                .outlierReason(reviewDto.getOutlierReason())
                .build();

        if (reviewDto.getReviewerScores() != null) {
            review.setReviewerScores(reviewDto.getReviewerScores().stream()
                    .map(rsDto -> CalibrationSession.ReviewerScore.builder()
                            .reviewerId(rsDto.getReviewerId())
                            .reviewerName(rsDto.getReviewerName())
                            .score(rsDto.getScore())
                            .maxScore(rsDto.getMaxScore())
                            .percentageScore(rsDto.getPercentageScore())
                            .comments(rsDto.getComments())
                            .submittedAt(rsDto.getSubmittedAt() != null ? rsDto.getSubmittedAt() : Instant.now())
                            .build())
                    .collect(Collectors.toList()));
        }

        session.addCalibrationReview(review);
        session.calculateInterRaterReliability();
        session.determineCalibrationResult();

        CalibrationSession saved = sessionRepository.save(session);
        return mapper.toDto(saved);
    }

    /**
     * Add action item
     */
    @Transactional
    @CacheEvict(value = "calibrationSessions", allEntries = true)
    public CalibrationSessionDto addActionItem(String sessionId, CalibrationSessionDto.ActionItemDto actionItemDto) {
        log.info("Adding action item to session: {}", sessionId);

        CalibrationSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Calibration session not found: " + sessionId));

        CalibrationSession.ActionItem actionItem = CalibrationSession.ActionItem.builder()
                .actionItemId(actionItemDto.getActionItemId())
                .description(actionItemDto.getDescription())
                .assignedTo(actionItemDto.getAssignedTo())
                .assignedToName(actionItemDto.getAssignedToName())
                .status(actionItemDto.getStatus() != null ?
                        CalibrationSession.ActionItem.ActionItemStatus.valueOf(actionItemDto.getStatus()) :
                        CalibrationSession.ActionItem.ActionItemStatus.PENDING)
                .dueDate(actionItemDto.getDueDate())
                .notes(actionItemDto.getNotes())
                .build();

        session.addActionItem(actionItem);
        if (actionItemDto.getDueDate() != null) {
            session.setFollowUpRequired(true);
        }

        CalibrationSession saved = sessionRepository.save(session);
        return mapper.toDto(saved);
    }

    /**
     * Delete session
     */
    @Transactional
    @CacheEvict(value = "calibrationSessions", allEntries = true)
    public void deleteSession(String sessionId) {
        log.info("Deleting calibration session: {}", sessionId);

        CalibrationSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Calibration session not found: " + sessionId));

        sessionRepository.delete(session);
        log.info("Deleted calibration session: {}", sessionId);
    }
}
