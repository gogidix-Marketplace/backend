package com.gogidix.transaction.onboarding.dto;

import com.gogidix.transaction.onboarding.domain.entity.OnboardingTracker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DTO Tests")
class DtoTest {

    @Test
    @DisplayName("Should build OnboardingCreateRequest")
    void shouldBuildCreateRequest() {
        UUID txId = UUID.randomUUID();
        OnboardingCreateRequest req = OnboardingCreateRequest.builder()
            .transactionId(txId).entityType(OnboardingTracker.EntityType.MERCHANT)
            .entityId("e1").priority(OnboardingTracker.Priority.HIGH).build();
        assertEquals(txId, req.getTransactionId());
        assertEquals(OnboardingTracker.Priority.HIGH, req.getPriority());
    }

    @Test
    @DisplayName("Should build OnboardingTrackerResponse")
    void shouldBuildResponse() {
        OnboardingTrackerResponse res = OnboardingTrackerResponse.builder()
            .id(UUID.randomUUID()).currentStatus(OnboardingTracker.OnboardingStatus.APPROVED)
            .progressPercentage(100).build();
        assertEquals(OnboardingTracker.OnboardingStatus.APPROVED, res.getCurrentStatus());
    }

    @Test
    @DisplayName("Should build StageTransitionRequest")
    void shouldBuildStageTransition() {
        StageTransitionRequest req = StageTransitionRequest.builder()
            .nextStage(OnboardingTracker.OnboardingStage.APPROVAL)
            .description("moving to approval").build();
        assertEquals(OnboardingTracker.OnboardingStage.APPROVAL, req.getNextStage());
    }

    @Test
    @DisplayName("Should test OnboardingMapper toEntity")
    void shouldTestMapperToEntity() {
        UUID txId = UUID.randomUUID();
        OnboardingCreateRequest req = OnboardingCreateRequest.builder()
            .transactionId(txId).entityType(OnboardingTracker.EntityType.VENDOR)
            .entityId("v1").build();
        OnboardingTracker entity = com.gogidix.transaction.onboarding.mapper.OnboardingMapper.toEntity(req);
        assertEquals(txId, entity.getTransactionId());
        assertEquals(OnboardingTracker.Priority.NORMAL, entity.getPriority());
    }

    @Test
    @DisplayName("Should test OnboardingMapper toResponse")
    void shouldTestMapperToResponse() {
        OnboardingTracker entity = OnboardingTracker.builder()
            .transactionId(UUID.randomUUID()).entityType(OnboardingTracker.EntityType.PARTNER)
            .currentStatus(OnboardingTracker.OnboardingStatus.IN_PROGRESS).build();
        OnboardingTrackerResponse res = com.gogidix.transaction.onboarding.mapper.OnboardingMapper.toResponse(entity);
        assertEquals(OnboardingTracker.OnboardingStatus.IN_PROGRESS, res.getCurrentStatus());
    }
}
