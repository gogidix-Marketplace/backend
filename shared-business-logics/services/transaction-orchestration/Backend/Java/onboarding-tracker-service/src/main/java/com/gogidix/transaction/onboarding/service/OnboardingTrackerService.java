package com.gogidix.transaction.onboarding.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.transaction.onboarding.domain.entity.OnboardingStageHistory;
import com.gogidix.transaction.onboarding.domain.entity.OnboardingTracker;
import com.gogidix.transaction.onboarding.domain.repository.OnboardingStageHistoryRepository;
import com.gogidix.transaction.onboarding.domain.repository.OnboardingTrackerRepository;
import com.gogidix.transaction.onboarding.dto.*;
import com.gogidix.transaction.onboarding.exception.DuplicateOnboardingException;
import com.gogidix.transaction.onboarding.exception.InvalidTransitionException;
import com.gogidix.transaction.onboarding.exception.OnboardingNotFoundException;
import com.gogidix.transaction.onboarding.mapper.OnboardingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnboardingTrackerService {

    private final OnboardingTrackerRepository trackerRepository;
    private final OnboardingStageHistoryRepository historyRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public OnboardingTrackerResponse createOnboarding(OnboardingCreateRequest request) {
        log.info("Creating onboarding for entity: {} - {}", request.getEntityType(), request.getEntityId());

        // Check for idempotency
        if (request.getIdempotencyKey() != null) {
            trackerRepository.findByIdempotencyKey(request.getIdempotencyKey())
                .ifPresent(existing -> {
                    throw new DuplicateOnboardingException("Onboarding already exists with idempotency key: " + request.getIdempotencyKey());
                });
        }

        // Check for existing onboarding
        trackerRepository.findByEntityTypeAndEntityId(request.getEntityType(), request.getEntityId())
            .ifPresent(existing -> {
                if (existing.getCurrentStatus() != OnboardingTracker.OnboardingStatus.COMPLETED &&
                    existing.getCurrentStatus() != OnboardingTracker.OnboardingStatus.CANCELLED) {
                    throw new DuplicateOnboardingException("Active onboarding already exists for this entity");
                }
            });

        OnboardingTracker tracker = OnboardingMapper.toEntity(request);

        // Set initial values
        tracker.setCurrentStatus(OnboardingTracker.OnboardingStatus.INITIATED);
        tracker.setCurrentStage(OnboardingTracker.OnboardingStage.REGISTRATION);
        tracker.setProgressPercentage(0);
        tracker.setCompletedSteps(0);

        // Serialize metadata
        serializeMetadata(tracker, request.getMetadata());

        OnboardingTracker saved = trackerRepository.save(tracker);

        // Create initial history entry
        createStageHistory(saved, null, saved.getCurrentStage(), "Onboarding initiated");

        // Publish event
        publishOnboardingEvent("ONBOARDING_CREATED", saved);

        log.info("Created onboarding tracker with ID: {} for transaction: {}", saved.getId(), saved.getTransactionId());
        return OnboardingMapper.toResponse(saved);
    }

    @Transactional
    public OnboardingTrackerResponse transitionToNextStage(UUID trackerId, StageTransitionRequest request) {
        log.info("Transitioning tracker {} to next stage", trackerId);

        OnboardingTracker tracker = trackerRepository.findById(trackerId)
            .orElseThrow(() -> new OnboardingNotFoundException("Onboarding tracker not found: " + trackerId));

        OnboardingTracker.OnboardingStage currentStage = tracker.getCurrentStage();
        OnboardingTracker.OnboardingStage nextStage = request.getNextStage();

        // Validate transition
        if (!isValidTransition(currentStage, nextStage)) {
            throw new InvalidTransitionException(
                String.format("Invalid transition from %s to %s", currentStage, nextStage));
        }

        // Store previous stage
        tracker.setPreviousStage(currentStage);
        tracker.setCurrentStage(nextStage);
        tracker.setLastStateChange(LocalDateTime.now());

        // Update progress
        updateProgress(tracker);

        // Update status if provided
        if (request.getStatus() != null) {
            tracker.setCurrentStatus(request.getStatus());
        } else if (nextStage == OnboardingTracker.OnboardingStage.COMPLETED) {
            tracker.setCurrentStatus(OnboardingTracker.OnboardingStatus.COMPLETED);
            tracker.setCompletedAt(LocalDateTime.now());
        } else {
            tracker.setCurrentStatus(OnboardingTracker.OnboardingStatus.IN_PROGRESS);
        }

        // Update stage description
        tracker.setStageDescription(request.getDescription());

        // Handle failure
        if (request.getStatus() == OnboardingTracker.OnboardingStatus.FAILED) {
            tracker.setErrorMessage(request.getErrorMessage());
            tracker.setRetryCount(tracker.getRetryCount() + 1);
        }

        // Serialize stage data
        if (request.getStageData() != null && !request.getStageData().isEmpty()) {
            try {
                String existingData = tracker.getOnboardingData();
                Map<String, Object> combinedData = existingData != null ?
                    objectMapper.readValue(existingData, new TypeReference<Map<String, Object>>() {}) : new HashMap<>();
                combinedData.putAll(request.getStageData());
                tracker.setOnboardingData(objectMapper.writeValueAsString(combinedData));
            } catch (JsonProcessingException e) {
                log.warn("Failed to serialize stage data", e);
            }
        }

        OnboardingTracker saved = trackerRepository.save(tracker);

        // Create history entry
        createStageHistory(saved, currentStage, nextStage, request.getDescription(),
            request.getStatus() != null ? request.getStatus().name() : "IN_PROGRESS");

        // Publish event
        publishOnboardingEvent("STAGE_TRANSITION", saved);

        log.info("Transitioned tracker {} from {} to {}", trackerId, currentStage, nextStage);
        return OnboardingMapper.toResponse(saved);
    }

    @Transactional
    public OnboardingTrackerResponse updateOnboardingData(UUID trackerId, Map<String, Object> data) {
        log.info("Updating onboarding data for tracker: {}", trackerId);

        OnboardingTracker tracker = trackerRepository.findById(trackerId)
            .orElseThrow(() -> new OnboardingNotFoundException("Onboarding tracker not found: " + trackerId));

        try {
            String existingData = tracker.getOnboardingData();
            Map<String, Object> combinedData = existingData != null ?
                objectMapper.readValue(existingData, new TypeReference<Map<String, Object>>() {}) : new HashMap<>();
            combinedData.putAll(data);
            tracker.setOnboardingData(objectMapper.writeValueAsString(combinedData));

            OnboardingTracker saved = trackerRepository.save(tracker);
            return OnboardingMapper.toResponse(saved);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize onboarding data", e);
            throw new RuntimeException("Failed to update onboarding data", e);
        }
    }

    @Transactional(readOnly = true)
    public OnboardingTrackerResponse getOnboardingById(UUID trackerId) {
        OnboardingTracker tracker = trackerRepository.findById(trackerId)
            .orElseThrow(() -> new OnboardingNotFoundException("Onboarding tracker not found: " + trackerId));
        return OnboardingMapper.toResponse(tracker);
    }

    @Transactional(readOnly = true)
    public OnboardingTrackerResponse getOnboardingByTransactionId(UUID transactionId) {
        OnboardingTracker tracker = trackerRepository.findByTransactionId(transactionId)
            .orElseThrow(() -> new OnboardingNotFoundException("Onboarding tracker not found for transaction: " + transactionId));
        return OnboardingMapper.toResponse(tracker);
    }

    @Transactional(readOnly = true)
    public OnboardingTrackerResponse getOnboardingByEntity(OnboardingTracker.EntityType entityType, String entityId) {
        OnboardingTracker tracker = trackerRepository.findByEntityTypeAndEntityId(entityType, entityId)
            .orElseThrow(() -> new OnboardingNotFoundException("Onboarding tracker not found for entity: " + entityType + " - " + entityId));
        return OnboardingMapper.toResponse(tracker);
    }

    @Transactional(readOnly = true)
    public Page<OnboardingTrackerResponse> searchOnboardings(OnboardingSearchRequest searchRequest, Pageable pageable) {
        Page<OnboardingTracker> trackers = trackerRepository.searchOnboardings(
            searchRequest.getTransactionId(),
            searchRequest.getEntityType(),
            searchRequest.getEntityId(),
            searchRequest.getMerchantId(),
            searchRequest.getStatus(),
            searchRequest.getStage(),
            searchRequest.getAssignedTo(),
            searchRequest.getStartDate(),
            searchRequest.getEndDate(),
            pageable
        );
        return trackers.map(OnboardingMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<OnboardingStageHistoryResponse> getStageHistory(UUID trackerId) {
        List<OnboardingStageHistory> history = historyRepository.findByTrackerIdOrderByTimestampAsc(trackerId);
        return history.stream()
            .map(OnboardingMapper::toHistoryResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OnboardingTrackerResponse> getStuckOnboardings(int hoursThreshold) {
        LocalDateTime staleThreshold = LocalDateTime.now().minusHours(hoursThreshold);
        List<OnboardingTracker> trackers = trackerRepository.findStuckOnboardings(
            OnboardingTracker.OnboardingStatus.IN_PROGRESS, staleThreshold);
        return trackers.stream()
            .map(OnboardingMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OnboardingTrackerResponse> getPendingOnboardings() {
        List<OnboardingTracker> trackers = trackerRepository.findPendingOnboardings();
        return trackers.stream()
            .map(OnboardingMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OnboardingTrackerResponse> getHighPriorityOnboardings() {
        List<OnboardingTracker> trackers = trackerRepository.findHighPriorityOnboardings();
        return trackers.stream()
            .map(OnboardingMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public OnboardingTrackerResponse assignToUser(UUID trackerId, String userId) {
        OnboardingTracker tracker = trackerRepository.findById(trackerId)
            .orElseThrow(() -> new OnboardingNotFoundException("Onboarding tracker not found: " + trackerId));

        tracker.setAssignedTo(userId);
        OnboardingTracker saved = trackerRepository.save(tracker);

        publishOnboardingEvent("ONBOARDING_ASSIGNED", saved);

        log.info("Assigned onboarding tracker {} to user {}", trackerId, userId);
        return OnboardingMapper.toResponse(saved);
    }

    @Transactional
    public OnboardingTrackerResponse updatePriority(UUID trackerId, OnboardingTracker.Priority priority) {
        OnboardingTracker tracker = trackerRepository.findById(trackerId)
            .orElseThrow(() -> new OnboardingNotFoundException("Onboarding tracker not found: " + trackerId));

        tracker.setPriority(priority);
        OnboardingTracker saved = trackerRepository.save(tracker);

        publishOnboardingEvent("PRIORITY_UPDATED", saved);

        log.info("Updated priority for tracker {} to {}", trackerId, priority);
        return OnboardingMapper.toResponse(saved);
    }

    @Transactional
    public void cancelOnboarding(UUID trackerId, String reason) {
        OnboardingTracker tracker = trackerRepository.findById(trackerId)
            .orElseThrow(() -> new OnboardingNotFoundException("Onboarding tracker not found: " + trackerId));

        tracker.setCurrentStatus(OnboardingTracker.OnboardingStatus.CANCELLED);
        tracker.setErrorMessage(reason);
        trackerRepository.save(tracker);

        publishOnboardingEvent("ONBOARDING_CANCELLED", tracker);

        log.info("Cancelled onboarding tracker {} reason: {}", trackerId, reason);
    }

    private boolean isValidTransition(OnboardingTracker.OnboardingStage from, OnboardingTracker.OnboardingStage to) {
        // Define valid transitions
        Map<OnboardingTracker.OnboardingStage, List<OnboardingTracker.OnboardingStage>> validTransitions = Map.of(
            OnboardingTracker.OnboardingStage.REGISTRATION, List.of(
                OnboardingTracker.OnboardingStage.KYC_VERIFICATION
            ),
            OnboardingTracker.OnboardingStage.KYC_VERIFICATION, List.of(
                OnboardingTracker.OnboardingStage.BUSINESS_VERIFICATION,
                OnboardingTracker.OnboardingStage.REGISTRATION
            ),
            OnboardingTracker.OnboardingStage.BUSINESS_VERIFICATION, List.of(
                OnboardingTracker.OnboardingStage.DOCUMENT_UPLOAD,
                OnboardingTracker.OnboardingStage.REGISTRATION
            ),
            OnboardingTracker.OnboardingStage.DOCUMENT_UPLOAD, List.of(
                OnboardingTracker.OnboardingStage.DOCUMENT_VERIFICATION
            ),
            OnboardingTracker.OnboardingStage.DOCUMENT_VERIFICATION, List.of(
                OnboardingTracker.OnboardingStage.BANK_ACCOUNT_SETUP,
                OnboardingTracker.OnboardingStage.DOCUMENT_UPLOAD
            ),
            OnboardingTracker.OnboardingStage.BANK_ACCOUNT_SETUP, List.of(
                OnboardingTracker.OnboardingStage.COMPLIANCE_CHECK
            ),
            OnboardingTracker.OnboardingStage.COMPLIANCE_CHECK, List.of(
                OnboardingTracker.OnboardingStage.RISK_ASSESSMENT
            ),
            OnboardingTracker.OnboardingStage.RISK_ASSESSMENT, List.of(
                OnboardingTracker.OnboardingStage.APPROVAL
            ),
            OnboardingTracker.OnboardingStage.APPROVAL, List.of(
                OnboardingTracker.OnboardingStage.ACTIVATION,
                OnboardingTracker.OnboardingStage.REGISTRATION
            ),
            OnboardingTracker.OnboardingStage.ACTIVATION, List.of(
                OnboardingTracker.OnboardingStage.COMPLETED
            )
        );

        List<OnboardingTracker.OnboardingStage> allowed = validTransitions.get(from);
        return allowed != null && allowed.contains(to);
    }

    private void updateProgress(OnboardingTracker tracker) {
        List<OnboardingTracker.OnboardingStage> allStages = Arrays.asList(
            OnboardingTracker.OnboardingStage.values()
        );

        int currentIndex = allStages.indexOf(tracker.getCurrentStage());
        int totalStages = allStages.size() - 1; // Exclude COMPLETED from count

        tracker.setCompletedSteps(currentIndex);
        tracker.setTotalSteps(totalStages);
        tracker.setProgressPercentage((currentIndex * 100) / totalStages);

        // Update completed stages
        List<String> completed = new ArrayList<>();
        for (int i = 0; i < currentIndex; i++) {
            completed.add(allStages.get(i).name());
        }
        try {
            tracker.setCompletedStages(objectMapper.writeValueAsString(completed));
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize completed stages", e);
        }
    }

    private void createStageHistory(OnboardingTracker tracker, OnboardingTracker.OnboardingStage fromStage,
                                   OnboardingTracker.OnboardingStage toStage, String description) {
        createStageHistory(tracker, fromStage, toStage, description, "SUCCESS");
    }

    private void createStageHistory(OnboardingTracker tracker, OnboardingTracker.OnboardingStage fromStage,
                                   OnboardingTracker.OnboardingStage toStage, String description, String status) {
        OnboardingStageHistory history = OnboardingStageHistory.builder()
            .trackerId(tracker.getId())
            .fromStage(fromStage)
            .toStage(toStage)
            .status(status)
            .stageDescription(description)
            .timestamp(LocalDateTime.now())
            .build();

        historyRepository.save(history);
    }

    private void serializeMetadata(OnboardingTracker tracker, Map<String, Object> metadata) {
        if (metadata != null && !metadata.isEmpty()) {
            try {
                tracker.setMetadata(objectMapper.writeValueAsString(metadata));
            } catch (JsonProcessingException e) {
                log.warn("Failed to serialize metadata", e);
            }
        }
    }

    private void publishOnboardingEvent(String eventType, OnboardingTracker tracker) {
        // Kafka removed for local development - just logging the event
        log.debug("Onboarding event: {} for tracker: {}, transactionId: {}, stage: {}, status: {}",
            eventType, tracker.getId(), tracker.getTransactionId(), tracker.getCurrentStage(), tracker.getCurrentStatus());
    }
}
